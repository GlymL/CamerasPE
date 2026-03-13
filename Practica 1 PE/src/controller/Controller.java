package controller;


import logic.*;
import mapaApp.GeneradorCamaras;
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
                    GeneradorCamaras gc,
                    int popSize,
                    double crossRatio,
                    double mutRatio,
                    EnumCruce enumCr,
                    EnumMutacion enumMut,
                    EnumSelection enumSel,
                    double elitismo,
                    int n_drones, AEstrellaPrecalc ae
                    ) {

        Population population = new Population(
            gc,
            popSize,
            crossRatio,
            mutRatio,
            enumCr,
            enumMut,
            enumSel,
            elitismo,
            n_drones, ae
        );

        this.engine.start(this,
                population,
                generations);
    }

    public void updateChart(double maxGen, double pEv, double bestFitness, double avgFitness) {
        gui.updateChart(maxGen, pEv, bestFitness, avgFitness);
    }

    public void clearChart() {
        gui.clearChart();
    }

    public void updateMap(int[][] bestCrom){
        gui.updateMap(bestCrom);

    }
}
