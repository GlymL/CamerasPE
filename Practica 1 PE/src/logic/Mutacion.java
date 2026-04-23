package logic;

import java.util.ArrayList;
import java.util.Random;

import mapaApp.GeneradorMapa;

public class Mutacion {
    private final EnumMutacion ec;
    private final double mut;

    private final Random r = new Random();
    public Mutacion(EnumMutacion ec, double mut){
        this.ec = ec;
        this.mut = mut;
    }

    public void mutar(ArrayList<Fitness> pop, GeneradorMapa map, double bloating){
        CromosomaRanger crom;
        for (int i = 0; i < pop.size()/2; i++){
            int rand = r.nextInt(0, pop.size());
            Fitness f = pop.get(rand);
            switch(ec){
            case EnumMutacion.SUBARBOL -> crom = f.getCrom().mutacionSubArbol(mut);
            case EnumMutacion.FUNCIONAL -> crom = f.getCrom().mutacionFuncional(mut);
            case EnumMutacion.TERMINAL -> crom = f.getCrom().mutacionTerminal(mut);
            case EnumMutacion.HOIST -> crom = f.getCrom().mutacionHoist(mut);
            case EnumMutacion.ALEATORIA -> crom = f.getCrom().mutacionRandom(mut);
            default -> {
                System.err.println();
                return;
                }
            }
            pop.set(rand, new Fitness(crom, map, bloating));
        }
    }
}
