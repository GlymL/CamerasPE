package logic;

import java.util.ArrayList;

import controller.Controller;

public class EvolutionEngine {

    private final Population population;
    private final Selection mutationStrategy;
    private final int generations;
    private final Controller controller;

    private ArrayList<Double> avgFitnessHistory = new ArrayList<>();
    private ArrayList<Double> bestFitnessHistory = new ArrayList<>();

    public EvolutionEngine(Controller controller,
                           Population population,
                           Selection mutationStrategy,
                           int generations) {
        this.population = population;
        this.mutationStrategy = mutationStrategy;
        this.generations = generations;
        this.controller = controller;
    }

    public void run(EvolutionListener listener) {

        double max_fitness = 0;
        for (int gen = 0; gen <= generations; gen++) {
            population.evolve(mutationStrategy);
            avgFitnessHistory.add(population.averageFitness());
            bestFitnessHistory.add(population.bestFitness());
            max_fitness = Math.max(max_fitness, population.bestFitness());
            if (listener != null) {
                listener.onGeneration(gen, population);
            }

            controller.updateChart(max_fitness, 0, bestFitnessHistory.getLast(), avgFitnessHistory.getLast());
        }
        System.out.println(max_fitness);
    }
}
