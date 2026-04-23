package logic;

import mapaApp.GeneradorMapa;
import mapaApp.MapaRover;

public class Fitness implements Comparable<Fitness>{
    private final CromosomasRanger c;
    // private final TreeGenerator tg;
    private final GeneradorMapa map;
    private double fitness;
    private double aptitude;


    public Fitness(CromosomasRanger c, GeneradorMapa map){
        this.c = c;
        this.map = map;
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

            fitnessTotal += fitnessMapa;
        }

        double fitnessBase = fitnessTotal / 3;

        fitness = fitnessBase;
    }

    @Override
    public int compareTo(Fitness a2) {
        return Double.compare(a2.aptitude, aptitude);
    }

    public double getFitness() {
        return fitness;
    }

    public CromosomasRanger getCrom(){
        return c.clone();
    }

    public Fitness clone(){
        return new Fitness(c, map);
    }
    public void setAptitude(double apt){
        this.aptitude = apt;
    }

    public double getAptitude(){
        return aptitude;
    }
}
