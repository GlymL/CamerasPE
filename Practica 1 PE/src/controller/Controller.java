package controller;


import logic.*;
import mapaApp.MapaCamaras;
import view.ConsoleEvolutionListener;
import view.EvolutionFullGUI;

public class Controller {

    private final EvolutionEngine engine;
    private final EvolutionFullGUI gui;

 
    public Controller(EvolutionFullGUI gui,
                      int generations,
                      int mapa,
                      int popSize,
                      double crossRatio,
                      double mutRatio,
                      boolean ponder,
                      boolean monopoint,
                      Selection mutationStrategy) {

        MapaCamaras mapaCamaras = new MapaCamaras(mapa);

        Population population = new Population(
                mapaCamaras,
                popSize,
                crossRatio,
                mutRatio,
                ponder,
                monopoint,
                0.02
        );

        this.engine = new EvolutionEngine(
                this,
                population,
                mutationStrategy,
                generations
        );

        this.gui = gui;
    }

    public void start() {
        engine.run(new ConsoleEvolutionListener());
    }

    public void updateChart(double maxGen, double pEv, double bestFitness, double avgFitness) {
        gui.updateChart(maxGen, pEv, bestFitness, avgFitness);
    }
}
