package logic;

import java.util.ArrayList;
import java.util.Random;

public class Cruce {
    private final EnumCruce ec;
    private final double cross;

    private final Random r = new Random();
    public Cruce(EnumCruce ec, double cross){
        this.ec = ec;
        this.cross = cross;
    }

    public ArrayList<FitnessDron> cruzar(ArrayList<FitnessDron> pop, AEstrellaPrecalc ac){
        CromosomasDron[] crom;
        ArrayList<FitnessDron> ret = new ArrayList<>();
        for (int i = 0; i < pop.size()/2; i++){
            FitnessDron f1 = pop.get(r.nextInt(0, pop.size()));
            FitnessDron f2 = pop.get(r.nextInt(0, pop.size()));
            switch(ec){
            case EnumCruce.PMX -> crom = f1.getCrom().crucePMX(f2.getCrom(), cross);
            case EnumCruce.OP -> crom = f1.getCrom().cruceOP(f2.getCrom(), cross);
            case EnumCruce.OXPP -> crom = f1.getCrom().cruceOXPP(f2.getCrom(), cross);
            case EnumCruce.CX -> crom = f1.getCrom().cruceCX(f2.getCrom(), cross);
            case EnumCruce.CO -> crom = f1.getCrom().cruceCO(f2.getCrom(), cross);
            case EnumCruce.ERX -> crom = f1.getCrom().cruceERX(f2.getCrom(), cross);
            case EnumCruce.CUSTOM -> crom = f1.getCrom().cruceCUSTOM(f2.getCrom(), cross);
            default -> {
                System.err.println();
                return null;
                }
            }
            ret.add(new FitnessDron(crom[0], ac));
            ret.add(new FitnessDron(crom[1], ac));
        }
        return ret;
    }
}
