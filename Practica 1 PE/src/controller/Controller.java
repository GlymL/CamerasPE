package controller;


import logic.*;
import mapaApp.MapaCamaras;
import view.EvolutionFullGUI;

public class Controller {

    private EvolutionEngine engine;
    private EvolutionFullGUI gui;
    private Controller inst;

 
    public Controller(){
        if(inst == null){
            this.engine = new EvolutionEngine();
            this.gui = new EvolutionFullGUI(this);
            inst = this;
        }
        this.gui.setVisible(true);
    }

    public void execute(
                    int generations,
                    int mapa,
                    int popSize,
                    double crossRatio,
                    double mutRatio,
                    boolean ponder,
                    EnumCruce monopoint,
                    double elitismo,
                    EnumSelection mutationStrategy,
                    boolean binario,
                    EnumMutacion m) {

        MapaCamaras mapaCamaras = new MapaCamaras(mapa);
        Population population = new Population(
                mapaCamaras,
                popSize,
                crossRatio,
                mutRatio,
                ponder,
                monopoint,
                elitismo,
                binario,
                m
        );

        this.engine.start(this,
                population,
                mutationStrategy,
                generations);
    }

    public void updateChart(double maxGen, double pEv, double bestFitness, double avgFitness) {
        gui.updateChart(maxGen, pEv, bestFitness, avgFitness);
    }

    public void clearChart() {
        gui.clearChart();
    }

    public void updateMap(int[][] visitado, Cromosoma c){
        gui.updateMap(visitado, c);

    }
}
