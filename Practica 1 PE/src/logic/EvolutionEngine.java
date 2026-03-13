package logic;

import controller.Controller;
import java.util.ArrayList;
import javax.swing.JOptionPane;

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
        
        double min_fitness = Double.MAX_VALUE;
        for (int gen = 0; gen < generations; gen++) {
            population.evolve();
            avgFitnessHistory.add(population.averageFitness());
            bestFitnessHistory.add(population.bestFitness());
            double best = population.bestFitness();
            min_fitness = Math.min(min_fitness, best);
            if (listener != null) {
                listener.onGeneration(gen, population);
            }
            double bf = !bestFitnessHistory.isEmpty() ? bestFitnessHistory.getLast() : 0;
            double af = !avgFitnessHistory.isEmpty() ? avgFitnessHistory.getLast() : 0;
            double pEv = bf/af > 0 ? bf/af : 0;

            double currentMin = min_fitness;
            controller.updateChart(currentMin, pEv, bf, af);
            controller.updateMap(getBestMap(), population.best());
        }
        System.out.println(min_fitness);

        JOptionPane.showMessageDialog(
            null, 
            "El mejor fitness es: " + min_fitness,
            "Mejor Fitness",
            JOptionPane.INFORMATION_MESSAGE
        );


    }
    public int[][] getBestMap(){
        return population.mapBest();
    }
}