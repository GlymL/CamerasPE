package logic;

import java.util.ArrayList;
import java.util.Random;

import mapaApp.GeneradorMapa;

public class Cruce {
    private final double cross;

    private final Random r = new Random();
    public Cruce(double cross){
        this.cross = cross;
    }

    public void cruzar(ArrayList<Fitness> pop, GeneradorMapa map, double bloating){
        for (int i = 0; i < pop.size()/2; i++){
            int r1 = r.nextInt(0, pop.size()), r2 = r.nextInt(0, pop.size());
            Fitness f1 = pop.get(r1);
            Fitness f2 = pop.get(r2);
            
            CromosomaRanger[] hijos = f1.getCrom().cruce(f2.getCrom(), cross);

            pop.set(r1, new Fitness(hijos[0], map, bloating));
            pop.set(r2, new Fitness(hijos[1], map, bloating));
        }
    }
}
