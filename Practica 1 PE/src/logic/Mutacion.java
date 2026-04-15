package logic;

import java.util.ArrayList;
import java.util.Random;

public class Mutacion {
    private final EnumMutacion ec;
    private final double mut;

    private final Random r = new Random();
    public Mutacion(EnumMutacion ec, double mut){
        this.ec = ec;
        this.mut = mut;
    }

    public void mutar(ArrayList<Fitness> pop, AEstrellaPrecalc ae){
        CromosomasDron crom;
        for (int i = 0; i < pop.size()/2; i++){
            int rand = r.nextInt(0, pop.size());
            Fitness f = pop.get(rand);
            switch(ec){
            case EnumMutacion.INSERCION -> crom = f.getCrom().mutacionInsercion(mut);
            case EnumMutacion.INTERCAMBIO -> crom = f.getCrom().mutacionIntercambio(mut);
            case EnumMutacion.INVERSION -> crom = f.getCrom().mutacionInversion(mut);
            case EnumMutacion.HEURISTICA -> crom = f.getCrom().mutacionHeuristica(mut, ae);
            case EnumMutacion.CUSTOM -> crom = f.getCrom().mutacionCustom(mut);
            default -> {
                System.err.println();
                return;
                }
            }
            pop.set(rand, new Fitness(crom, ae));
        }
    }
}
