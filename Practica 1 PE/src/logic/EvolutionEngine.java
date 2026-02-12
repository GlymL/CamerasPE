package logic;

import java.util.ArrayList;

public class EvolutionEngine {

    private final Population population;
    private final Selection mutationStrategy;
    private final int generations;

    private ArrayList<Double> avgFitnessHistory = new ArrayList<>();
    private ArrayList<Double> bestFitnessHistory = new ArrayList<>();

    public EvolutionEngine(Population population,
                           Selection mutationStrategy,
                           int generations) {
        this.population = population;
        this.mutationStrategy = mutationStrategy;
        this.generations = generations;
    }

    public void run(EvolutionListener listener) {

        for (int gen = 0; gen <= generations; gen++) {
            population.evolve(mutationStrategy);
            avgFitnessHistory.add(population.averageFitness());
            bestFitnessHistory.add(population.bestFitness());
            if (listener != null) {
                listener.onGeneration(gen, population);
            }
        }
    }
}
