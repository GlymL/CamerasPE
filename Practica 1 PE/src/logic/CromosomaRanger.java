package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CromosomaRanger{
    //IMPLEMENTAR CLONE DE ASTNODE (HECHO)
    private final ASTNode cromosoma;

    private static final Random r = new Random();

    public CromosomaRanger(ASTNode cromosoma){
        this.cromosoma = cromosoma;
    }

    public int getNumberOfNodes() {
        return cromosoma.getNumberOfNodes();
    }

    public void execute(RoverState state) {
        cromosoma.execute(state);
    }

    @Override
    public CromosomaRanger clone() {
       return new CromosomaRanger(cromosoma.clone());
    }

    public void reset() {
        cromosoma.reset();
    }

    CromosomaRanger[] cruce(CromosomaRanger otro, double cross_ratio) {
        if(r.nextDouble() > cross_ratio){
            return new CromosomaRanger[]{ this.clone(), otro.clone() };
        }

        ASTNode hijo1 = this.cromosoma.clone();
        ASTNode hijo2 = otro.cromosoma.clone();

        ASTNode cruce1 = hijo1.selectRandomNode();
        ASTNode cruce2 = hijo2.selectRandomNode();

        ASTNode temporal = cruce1.clone();

        if (cruce2 == hijo2 && cruce1 == hijo1) {
            hijo1 = hijo2;
            hijo2 = temporal;
        }
        else if (cruce1 == hijo1) {
            hijo1 = cruce2;
            hijo2.changeNode(cruce2, temporal);
        }
        else if (cruce2 == hijo2) {
            temporal = cruce2.clone();

            hijo2 = cruce1;
            hijo1.changeNode(cruce1, temporal);
        }
        else {
            hijo1.changeNode(cruce1, cruce2);
            hijo2.changeNode(cruce2, temporal);
        }

        CromosomaRanger[] ret = new CromosomaRanger[2];
        ret[0] = new CromosomaRanger(hijo1);
        ret[1] = new CromosomaRanger(hijo2);

        return ret;
    }

    public CromosomaRanger mutacionSubArbol(double mut) {
        ASTNode copy = cromosoma.clone();
        if (r.nextDouble() < mut) {
            ASTNode random_child = copy.selectRandomNode();

            int rchild_height = random_child.getHeight();
            int max_height = Math.min(rchild_height, 10);
            int min_height = Math.max(1, rchild_height - r.nextInt(rchild_height));

            int new_height = Math.min((r.nextInt(2) == 0 ? max_height : min_height), 5);

            TreeGenerator tg = new TreeGenerator(new_height, 1);
            int opt = r.nextInt(2);

            ASTNode new_node;

            if (opt == 0)
                new_node = tg.generateFull(0, new_height);
            else
                new_node = tg.generateGrow(0, new_height);

            if (random_child == copy) {
                copy = new_node;
            } else {
                copy.changeNode(random_child, new_node);
            }
        }
        return new CromosomaRanger(copy);
    }

    public CromosomaRanger mutacionFuncional(double mut) {
        ASTNode copy = cromosoma.clone();
        if(r.nextDouble() < mut){
            // Buscar todas las funciones en el árbol
            List<FunctionNode> functionNodes = new ArrayList<>();
            findAllFunctionNodes(copy, functionNodes);
            
            // Si hay funciones, seleccionar una al azar y mutarla
            if (!functionNodes.isEmpty()) {
                FunctionNode functionNode = functionNodes.get(r.nextInt(functionNodes.size()));
                Function currentFunction = (Function) functionNode.content;
                
                // Crear una nueva función diferente con el mismo número de operandos
                SpatialSensor newSensor;
                FunctionOperator newOperator;
                int newValue;
                
                // Generar sensor diferente
                do {
                    newSensor = SpatialSensor.randomSensor();
                } while (newSensor == currentFunction.getSensor());
                
                // Generar operador diferente
                do {
                    FunctionOperator[] operators = FunctionOperator.values();
                    newOperator = operators[r.nextInt(operators.length)];
                } while (newOperator == currentFunction.getOperator());
                
                // Generar valor diferente
                int[] posibleValues = {10, 50, 100};
                do {
                    newValue = posibleValues[r.nextInt(posibleValues.length)];
                } while (newValue == currentFunction.getValue());
                
                // Crear la nueva función
                Function newFunction = new Function(newSensor, newOperator, newValue);
                functionNode.setContent(newFunction);
            }
        }
        return new CromosomaRanger(copy);
    }

    public CromosomaRanger mutacionTerminal(double mut) {
        ASTNode copy = cromosoma.clone();
        if(r.nextDouble() < mut){
            List<TerminalNode> terminalNodes = new ArrayList<>();
            findAllTerminalNodes(copy, terminalNodes);

            if (!terminalNodes.isEmpty()) {
                TerminalNode tnode = terminalNodes.get(r.nextInt(terminalNodes.size()));
                Action a;

                do {
                    a = Action.randomAction();
                } while (a == tnode.getAction());
                
                tnode = new TerminalNode(a);
            }
        }
        return new CromosomaRanger(copy);
    }

    public CromosomaRanger mutacionHoist(double mut) {
        ASTNode copy = cromosoma.clone();
        if(r.nextDouble() < mut && copy.getHeight() > 1){
            ASTNode child = copy.selectRandomNode();
            copy = child;
        }

        return new CromosomaRanger(copy);
    }

    public CromosomaRanger mutacionRandom(double mut) {
        return switch (r.nextInt(4)) {
            case 0 -> mutacionFuncional(mut);
            case 1 -> mutacionSubArbol(mut);
            case 2 -> mutacionTerminal(mut);
            case 3 -> mutacionHoist(mut);
            default -> mutacionSubArbol(mut);
        };
    }

    private void findAllFunctionNodes(ASTNode node, List<FunctionNode> functionNodes) {
        if (node instanceof FunctionNode) {
            FunctionNode functionNode = (FunctionNode) node;
            functionNodes.add(functionNode);
            
            // Buscar recursivamente en los hijos
            findAllFunctionNodes(functionNode.getIf(), functionNodes);
            findAllFunctionNodes(functionNode.getElse(), functionNodes);
        } 
        else if (node instanceof BlockNode) {
            BlockNode blockNode = (BlockNode) node;
            ASTNode[] children = blockNode.getNodes();
            
            // Buscar recursivamente en todos los hijos
            for (ASTNode child : children) {
                findAllFunctionNodes(child, functionNodes);
            }
        }
    }

    private void findAllTerminalNodes(ASTNode node, List<TerminalNode> terminalNodes) {
        if (node instanceof TerminalNode)
            terminalNodes.add((TerminalNode) node);
        else if (node instanceof FunctionNode) {
            findAllTerminalNodes( ((FunctionNode) node).getIf(), terminalNodes);
            findAllTerminalNodes( ((FunctionNode) node).getElse(), terminalNodes);
        }
        else {
            BlockNode blockNode = (BlockNode) node;
            ASTNode[] children = blockNode.getNodes();
            
            // Buscar recursivamente en todos los hijos
            for (ASTNode child : children) {
                findAllTerminalNodes(child, terminalNodes);
            }
        }
    }

    @Override
    public String toString() {
        return cromosoma.toString();
    }
}
