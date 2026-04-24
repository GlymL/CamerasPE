package logic;

import mapaApp.GeneradorMapa;
import mapaApp.MapaRover;
import java.util.ArrayList;
import java.util.List;

public class Fitness implements Comparable<Fitness>{
    private final CromosomaRanger c;
    // private final TreeGenerator tg;
    private final GeneradorMapa map;
    private double fitness;
    private double aptitude;
    private double bloating;
    private int muestras;


    public Fitness(CromosomaRanger c, GeneradorMapa map, double bloating){
        this.c = c.clone();
        this.map = map;
        this.bloating = bloating;
    }

    // public Fitness(CromosomasRanger c, double fitness, double aptitude) {
    //     this.c = c;
    //     this.fitness = fitness;
    //     this.aptitude = aptitude;
    // }

    public void calculateFitness() {
        //Aplicar penalizacion de giro cada 4 ciclos, no a partir de los 4 ciclos.
        //Ejemplo. Penalizamos el giro 4, el 8, el 12, pero no el 5, el 6, el 11...
        int fitnessTotal = 0;

        for (int i = 0; i < 3; i++) {
            int fitnessMapa = 0;
            RoverState state = new RoverState(new GeneradorMapa(map.getSeed() + i, map.getMapaRover()));

            c.reset(); // Resetear el estado del cromosoma antes de cada simulación

            while (state.isAlive() && !state.isFinished() && state.getTicks() < 150) {
                state.setAccionTomada(false);
                c.execute(state);
                
                state.incrTicks();
            }

            fitnessMapa = state.getMuestras() * 500 + state.getCeldasExploradas() * 20 + state.getVisualRewarding() * 2
                - state.getArenas() * 30 - state.getColisiones() * 10 - state.applyPereza();

            fitnessTotal += fitnessMapa;

            if (i == 0)
                muestras = state.getMuestras();
        }

        double fitnessBase = fitnessTotal / 3;

        //Aplicamos Impuesto por Bloating
        fitness = fitnessBase - c.getNumberOfNodes() * bloating;
    }

    @Override
    public int compareTo(Fitness a2) {
        return Double.compare(a2.aptitude, aptitude);
    }

    public double getFitness() {
        return fitness;
    }

    public int getMuestras() {
        return muestras;
    }

    public int getNumNodos() {
        return c.getNumberOfNodes();
    }

    public CromosomaRanger getCrom(){
        return c.clone();
    }

    public Fitness clone(){
        return new Fitness(c, map, bloating);
    }
    public void setAptitude(double apt){
        this.aptitude = apt;
    }

    public double getAptitude(){
        return aptitude;
    }

    @Override
    public String toString() {
        return this.c.toString();
    }
}
