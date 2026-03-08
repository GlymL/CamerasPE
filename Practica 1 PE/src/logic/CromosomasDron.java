package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CromosomasDron{

    private Integer[] cromosoma;
    private int drones;
    private int camaras;

    public CromosomasDron(int camaras, int drones){
        this.camaras = camaras;
        this.drones = drones;
        cromosoma = new Integer[camaras+(drones-1)];
        randomInitialize();
    }

    private CromosomasDron(int camaras, int drones, Integer[] cromosoma){
        this.drones = drones;
        this.camaras = camaras;
        cromosoma = cromosoma.clone();
    }

    public void randomInitialize() {
        for(int i = 0; i < cromosoma.length; i++){
            cromosoma[i] = i;
        }
       ArrayList<Integer> al = new ArrayList<Integer>(Arrays.asList(cromosoma));

        Collections.shuffle(al);

        for(int i = 0; i < cromosoma.length; i++){
            cromosoma[i] = al.get(i);
        }
    }

    public int[][] rutas(){
        int[][] ret = new int[drones][camaras + 1];
        for(int i = 0; i < drones; i++){
            Arrays.fill(ret[i], -1);
        }
        int dron = 0, iter = 0;
        for(int i = 0; i < cromosoma.length; i++){
            if(cromosoma[i] >= camaras){
                dron++;
                iter = 0;
            }else{
                ret[dron][iter] = cromosoma[i];
                iter++;
            }
        }
        return ret;
    }
    
    
    public CromosomasDron clone() {
       return new CromosomasDron(camaras, drones, cromosoma);
    }

    public CromosomasDron[] crucePMX(CromosomasDron crom, double mutRatio) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'crucePMX'");
    }

    public CromosomasDron[] cruceOP(CromosomasDron crom, double mutRatio) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cruceOP'");
    }

    public CromosomasDron[] cruceOXPP(CromosomasDron crom, double mutRatio) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cruceOXPP'");
    }

    public CromosomasDron[] cruceCX(CromosomasDron crom, double mutRatio) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cruceCX'");
    }

    public CromosomasDron[] cruceCO(CromosomasDron crom, double mutRatio) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cruceCO'");
    }

    public CromosomasDron[] cruceERX(CromosomasDron crom, double mutRatio) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cruceERX'");
    }

    public CromosomasDron[] cruceCUSTOM(CromosomasDron crom, double mutRatio) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cruceCUSTOM'");
    }

    public CromosomasDron mutacionInsercion(double mut) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mutacionInsercion'");
    }

    public CromosomasDron mutacionIntercambio(double mut) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mutacionIntercambio'");
    }

    public CromosomasDron mutacionInversion(double mut) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mutacionInversion'");
    }

    public CromosomasDron mutacionHeuristica(double mut) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mutacionHeuristica'");
    }

    public CromosomasDron mutacionCustom(double mut) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mutacionCustom'");
    }
}
