package logic;

import mapaApp.GeneradorMapa;
import mapaApp.MapaRover;

public class Fitness implements Comparable<Fitness>{
    private final CromosomaRanger c;
    // private final TreeGenerator tg;
    private final GeneradorMapa map;
    private double fitness;
    private double aptitude;
    private double bloating;


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

            while (state.isAlive() && !state.isFinished()) {
                state.setAccionTomada(false);
                c.execute(state);
                
                state.incrTicks();
            }

            fitnessMapa = state.getMuestras() * 500 + state.getCeldasExploradas() * 20 + state.getVisualRewarding()
                - state.getArenas() * 30 - state.getColisiones() * 10 - state.applyPereza();

            fitnessTotal += fitnessMapa;
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
}
