package launch;

import mapaApp.MapaCamaras;
import logic.Population;

public class App {
    public static void main(String[] args) throws Exception {
        
        MapaCamaras mc = new MapaCamaras(1);
        Population pop = new Population(mc, 100, .8,.02);
        int maxGenerations = 5000;
       
        for (int gen = 0; gen <= maxGenerations; gen++) {
            pop.evolve();
            if (gen % 500 == 0) { // print every 500 generations
                System.out.println("\nGeneration: " + gen + " Avg fitness: " + pop.averageFitness());
                pop.reportBest();
            }   
        }
    }
}
