package logic;

import java.util.Arrays;

import logic.AEstrella.Pair;
import logic.AEstrella.Ret;
import mapaApp.GeneradorCamaras;


public class AEstrellaPrecalc {

    private final GeneradorCamaras gc;
    private final AEstrella ae; 
    private final Double[][] precalc;
    private final Pair[][][] paths;
    private final Double[] precalcBase;
    private final Pair[][] pathsBase;

    public AEstrellaPrecalc(GeneradorCamaras gc, AEstrella aEstrella) {
        this.gc = gc;
        this.ae = aEstrella;
        precalc = new Double[gc.getCameras().length][gc.getCameras().length];
        precalcBase = new Double[gc.getCameras().length];
        paths = new Pair[gc.getCameras().length][gc.getCameras().length][];
        pathsBase = new Pair[gc.getCameras().length][];
        fill();
    }



    private void fill(){
        for (int i = 0; i < precalc.length; i++){
            for(int j = 0; j < precalc[i].length; j++){
                if(precalc[j][i] != null){
                    precalc[i][j] = precalc[j][i];
                    if(precalc[i][j] != -1.0)
                        paths[i][j] = paths[j][i].clone();
                }else{
                    Ret r = ae.aStarSearch(gc.getCameras()[i], gc.getCameras()[j]);

                    precalc[i][j] = r.p1;
                    paths[i][j] = r.p2;
                    
                }
                System.out.print(precalc[i][j] + " ");
            }
            Ret r = ae.aStarSearch(gc.getCameras()[i], gc.getBase());

            precalcBase[i] = r.p1;
            pathsBase[i] = r.p2;
            System.out.println();
        }
        System.out.println();

        for(Pair p : gc.getCameras()){
            System.out.print(p.first  + " " + p.second);
            System.out.println();
        }
        System.out.println();
    }

    public Double getPrecalc(int i, int j) {
        return precalc[i][j];
    }



    public double getInit(int i) {
    
        return precalcBase[i];
    }

    public Pair[] getPathBetween(Pair from, Pair to) {
        // Find indices
        Pair[] listaCamaras = gc.getCameras();
        int i = Arrays.asList(listaCamaras).indexOf(from);
        int j = Arrays.asList(listaCamaras).indexOf(to);

        if (from.equals(gc.getBase())) {
            j = Arrays.asList(listaCamaras).indexOf(to);
            return pathsBase[j];
        } else {
            return paths[i][j];
        }
    }


}
