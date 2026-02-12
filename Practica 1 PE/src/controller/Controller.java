package controller;


import logic.*;
import mapaApp.MapaCamaras;
import view.ConsoleEvolutionListener;

public class Controller {

    private final EvolutionEngine engine;


    public Controller(int generations,
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
                monopoint
        );

        this.engine = new EvolutionEngine(
                population,
                mutationStrategy,
                generations
        );
    }

    public void start() {
        engine.run(new ConsoleEvolutionListener());
    }
}
