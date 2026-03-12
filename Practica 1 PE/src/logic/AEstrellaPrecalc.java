package logic;

import logic.AEstrella.Pair;
import mapaApp.GeneradorCamaras;


public class AEstrellaPrecalc {

    private final GeneradorCamaras gc;
    private final AEstrella ae; 
    private final Double[][] precalc;
    private final Double[] precalcBase;

    public AEstrellaPrecalc(GeneradorCamaras gc, AEstrella aEstrella) {
        this.gc = gc;
        this.ae = aEstrella;
        precalc = new Double[gc.getCameras().length][gc.getCameras().length];
        precalcBase = new Double[gc.getCameras().length];
        fill();
    }



    private void fill(){
        for (int i = 0; i < precalc.length; i++){
            for(int j = 0; j < precalc[i].length; j++){
                if(precalc[j][i] != null){
                    precalc[i][j] = precalc[j][i];
                }else{
                    precalc[i][j] = ae.aStarSearch(gc.getCameras()[i], gc.getCameras()[j]);
                }
                System.out.print(precalc[i][j] + " ");
            }
            precalcBase[i] = ae.aStarSearch(gc.getCameras()[i], gc.getBase());
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



}
