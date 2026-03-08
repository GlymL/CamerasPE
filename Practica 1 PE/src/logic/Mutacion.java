package logic;

import java.util.ArrayList;
import java.util.Random;

import mapaApp.GeneradorCamaras;

public class Mutacion {
    private EnumMutacion ec;
    private double mut;

    private Random r = new Random();
    public Mutacion(EnumMutacion ec, double mut){
        this.ec = ec;
        this.mut = mut;
    }

    public ArrayList<FitnessDron> mutar(ArrayList<FitnessDron> pop, GeneradorCamaras gc){
        CromosomasDron crom;
        ArrayList<FitnessDron> ret = new ArrayList<FitnessDron>();
        for (int i = 0; i < pop.size()/2; i++){
            FitnessDron f = pop.get(r.nextInt(0, pop.size()));
            switch(ec){
            case EnumMutacion.INSERCION:
                crom = f.getCrom().mutacionInsercion(mut);
                break;
            case EnumMutacion.INTERCAMBIO:
                crom = f.getCrom().mutacionIntercambio(mut);
                break; 
            case EnumMutacion.INVERSION:
                crom = f.getCrom().mutacionInversion(mut);
                break;
            case EnumMutacion.HEURISTICA:
                crom = f.getCrom().mutacionHeuristica(mut);
                break;
            case EnumMutacion.CUSTOM:
                crom = f.getCrom().mutacionCustom(mut);
                break; 
            default:
                System.err.println();
                return null;
            }
            ret.add(new FitnessDron(crom, gc));
        }
        return ret;
    }
}
