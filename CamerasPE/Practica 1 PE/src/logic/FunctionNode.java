package logic;

import java.util.Random;

public class FunctionNode extends ASTNode {
    boolean evaluated;
    boolean option;
    ASTNode if_;
    ASTNode else_;
    
    public FunctionNode(Function f, ASTNode ifChild, ASTNode elseChild) {
        super(f);
        if_ = ifChild;
        else_ = elseChild;

        evaluated = false;
    }

    public SpatialSensor getSensor() {
        return ((Function) content).getSensor();
    }

    public ASTNode getIf() {
        return if_;
    }

    public ASTNode getElse() {
        return else_;
    }

    public ASTNode getCorrectChild(RoverState state) {
        Function f = (Function) content;
        if (f.evaluate(state)) {
            return if_;
        } else {
            return else_;
        }
    }

    @Override
    public void execute(Object params) {
        RoverState state = (RoverState) params;
        Function f = (Function) content;

        if (!evaluated) {
            option = f.evaluate(state);
            evaluated = true;
        }

        if (option) {
            if (!if_.isFinished()) {
                if_.execute(params);
            }
        } else {
            if (!else_.isFinished()) {
                else_.execute(params);
            }
        }
    }

    @Override
    public boolean isFinished() {
        boolean child_finished = (option && if_.isFinished()) || (!option && else_.isFinished());

        return child_finished && evaluated;
    }

    @Override
    public int getNumberOfNodes() {
        return 1 + if_.getNumberOfNodes() + else_.getNumberOfNodes();
    }

    @Override
    public ASTNode clone() {
        return new FunctionNode((Function) this.content, if_.clone(), else_.clone());
    }

    @Override
    public ASTNode selectRandomNode() {
        Random r = new Random();
        int opt = r.nextInt(3);

        if (opt == 0)
            return if_;
        else if (opt == 1)
            return else_;
        else
            return this;
    }

    @Override
    public boolean changeNode(ASTNode node, ASTNode change) {
        //Si llegas a esta función, sabes que no eres el tú el nodo a cambiar. (COMPROBAR)

        if (node == if_) {
            if_ = change;
            return true;
        } else if (node == else_) {
            else_ = change;
            return true;
        }

        //Si llegas aquí, el nodo a cambiar no es ningún hijo directo.
        if (changeNode(if_, change))
            return true;
        else
            return changeNode(else_, change);

    }

    @Override
    public int getHeight() {
        return 1 + Math.max(if_.getHeight(), else_.getHeight());
    }

    @Override
    public void reset() {
        evaluated = false;
        option = false;
        if_.reset();
        else_.reset();
    }

    @Override
    public String toString() {
        Function f = (Function) content;
        return "if (" + f.getSensor() + " " + f.getOperator() + " " + f.getValue() + ") { \n" + if_.toString() + " } else { \n" + else_.toString() + " }";
    }
}
