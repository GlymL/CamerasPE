package logic;

import java.util.Random;

public class TreeGenerator {
    private int maxDepth;
    private int minDepth;

    public TreeGenerator(int maxd, int mind) {
        maxDepth = maxd;
        minDepth = mind;
    }

    //Ramped and Half
    public ASTNode[] randomInit(int population_size) {
        int per_level = population_size / (maxDepth - minDepth + 1);
        ASTNode[] population = new ASTNode[population_size];
        int idx = 0;

        for (int i = minDepth; i <= maxDepth; i++) {
            for (int j = 1; j <= per_level; j++) {
                if (j <= per_level / 2) {
                    population[idx++] = generateFull(0, i);
                } else {
                    population[idx++] = generateGrow(0, i);
                }
            }
        }

        if (idx < population_size)
            population[idx] = generateFull(0, maxDepth);

        return population;
    }

    private ASTNode generateFull(int actual, int max) {
        ASTNode res;

        if (actual == max) {
            res = new TerminalNode(Action.randomAction());
        }
        else {
            Random r = new Random();

            //Nodo Condicional
            if ((r.nextInt() % 2) == 0) {
                int umbral = switch (r.nextInt() % 3) {
                    case 0 -> 10;
                    case 1 -> 50;
                    case 2 -> 100;
                    default -> 50;
                };

                ASTNode right = generateFull(actual + 1, max);
                ASTNode left = generateFull(actual + 1, max);

                return res = new FunctionNode(new Function(SpatialSensor.randomSensor(), FunctionOperator.LESS, umbral),
                    right, left);
            }
            //Nodo bloque
            else {
                int num_hijos = r.nextInt(2, 4);

                ASTNode[] hijos = new ASTNode[num_hijos];

                for (int i = 0; i < num_hijos; i++) {
                    hijos[i] = generateFull(actual + 1, max);
                }

                return new BlockNode(hijos);
            }
        }

        return res;
    }

    private ASTNode generateGrow(int actual, int max) {
        ASTNode res;

        if (actual == max) {
            res = new TerminalNode(Action.randomAction());
        }
        else {
            Random r = new Random();

            int option = r.nextInt() % 3;

            //Nodo Condicional
            if (option == 0) {
                int umbral = switch (r.nextInt() % 3) {
                    case 0 -> 10;
                    case 1 -> 50;
                    case 2 -> 100;
                    default -> 50;
                };

                ASTNode right = generateGrow(actual + 1, max);
                ASTNode left = generateGrow(actual + 1, max);

                return res = new FunctionNode(new Function(SpatialSensor.randomSensor(), FunctionOperator.LESS, umbral),
                    right, left);
            }
            //Nodo bloque
            else if (option == 1) {
                int num_hijos = r.nextInt(2, 4);

                ASTNode[] hijos = new ASTNode[num_hijos];

                for (int i = 0; i < num_hijos; i++) {
                    hijos[i] = generateGrow(actual + 1, max);
                }

                return new BlockNode(hijos);
            }
            //Nodo terminal
            else {
                res = new TerminalNode(Action.randomAction());
            }
        }

        return res;
    }

    public static void main(String[] args) {
        TreeGenerator tg = new TreeGenerator(3, 1);

        ASTNode[] trees = tg.randomInit(37);

    }
}
