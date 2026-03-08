package logic;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import controller.Controller;

public class EvolutionEngine {

    private Population population;
    private int generations;
    private Controller controller;

    private ArrayList<Double> avgFitnessHistory = new ArrayList<>();
    private ArrayList<Double> bestFitnessHistory = new ArrayList<>();

    public EvolutionEngine(){

    }

    public void start(Controller controller,
                           Population population,
                           int generations) {
        this.population = population;
        this.generations = generations;
        this.controller = controller;

        this.run(null);
    }

    public void run(EvolutionListener listener) {
        controller.clearChart();

        double max_fitness = 0;
        for (int gen = 0; gen < generations; gen++) {
            population.evolve();
            avgFitnessHistory.add(population.averageFitness());
            bestFitnessHistory.add(population.bestFitness());
            max_fitness = Math.max(max_fitness, population.bestFitness());
            if (listener != null) {
                listener.onGeneration(gen, population);
            }

            double bf = bestFitnessHistory.size() > 0 ? bestFitnessHistory.getLast() : 0;
            double af = avgFitnessHistory.size() > 0 ? avgFitnessHistory.getLast() : 0;
            double pEv = bf/af > 0 ? bf/af : 0;
            controller.updateChart(max_fitness, pEv, bf, af);
            controller.updateMap(getBestMap(), population.best());
        }
        System.out.println(max_fitness);

        JOptionPane.showMessageDialog(
            null, 
            "El mejor fitness es: " + max_fitness,
            "Mejor Fitness",
            JOptionPane.INFORMATION_MESSAGE
        );


    }
    public int[][] getBestMap(){
        return population.mapBest();
    }
}