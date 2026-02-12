package view;

import logic.EvolutionListener;
import logic.Population;

public class ConsoleEvolutionListener implements EvolutionListener {

    @Override
    public void onGeneration(int generation, Population population) {

        if (generation % 100 == 0) {
            System.out.println("\nGeneration: " + generation);
            System.out.println("Average fitness: " + population.averageFitness());
            population.reportBest();
        }
    }
}