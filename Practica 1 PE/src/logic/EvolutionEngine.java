package logic;

import controller.Controller;
import java.util.ArrayList;

public class EvolutionEngine {

    private Population population;
    private int generations;
    private Controller controller;

    private final ArrayList<Double> avgFitnessHistory = new ArrayList<>();
    private final ArrayList<Double> bestFitnessHistory = new ArrayList<>();

    public EvolutionEngine(){

    }

    public void start(Controller controller,
                           Population population,
                           int generations)  {
        this.population = population;
        this.generations = generations;
        this.controller = controller;
        
        run(null);
    }

    public void run(EvolutionListener listener) {
        controller.clearChart();
        System.out.println("Iniciando evolución por " + generations + " generaciones...");
        double min_fitness = Double.MAX_VALUE;
        for (int gen = 0; gen < generations; gen++) {
            System.out.println("Generación " + (gen + 1) + "/" + generations);
            population.evolve();
            avgFitnessHistory.add(population.averageFitness());
            bestFitnessHistory.add(population.bestFitness());
            double best = population.bestFitness();
            min_fitness = Math.min(min_fitness, best);
            double bestapt = population.bestAptitude();
            double avgapt = population.averageAptitude();
            if (listener != null) {
                listener.onGeneration(gen, population);
            }
            double bf = !bestFitnessHistory.isEmpty() ? bestFitnessHistory.getLast() : 0;
            double af = !avgFitnessHistory.isEmpty() ? avgFitnessHistory.getLast() : 0;
            double pEv = bestapt/avgapt > 0 ? bestapt/avgapt : 0;

            double currentMin = min_fitness;
            controller.updateChart(currentMin, pEv, bf, af);
            // controller.updateMap(getBestMap());
        }
        // int[][] ret = getBestMap();
        // for (int[] re : ret) {
        //     for (int r : re) {
        //         System.out.print(r + " ");
        //     }
        //     System.out.println();
            
        // }
        controller.best(population.best());

        System.out.println("Evolución finalizada. Mejor fitness: " + population.bestFitness());

    }
    // public int[][] getBestMap(){
    //     sortByFitness();
    //     return population.get(0).rutas();
    // }
}