package controller;


import logic.*;
import mapaApp.GeneradorMapa;
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
                    GeneradorMapa gc,
                    int popSize,
                    double crossRatio,
                    double mutRatio,
                    EnumMutacion enumMut,
                    EnumSelection enumSel,
                    double elitismo,
                    double bloating,
                    int prof_inicial
                    ) {

        Population population = new Population(
            gc,
            popSize,
            crossRatio,
            mutRatio,
            elitismo,
            bloating,
            enumMut,
            enumSel,
            new TreeGenerator(generations, prof_inicial)
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

    public void best(Fitness best) {
        gui.result(best);
    }
}
