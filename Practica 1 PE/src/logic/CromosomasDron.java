package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

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
        this.cromosoma = cromosoma.clone();
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
        int c1, c2;
        Random r = new Random();
        c1 = r.nextInt(cromosoma.length);
        c2 = r.nextInt(cromosoma.length);
        Integer[] arr1 = new Integer[cromosoma.length];
         Arrays.fill(arr1, -1);
        Integer[] arr2 = new Integer[cromosoma.length];
         Arrays.fill(arr2, -1);
        int iter1 = Math.min(c1, c2);
        int iter2 = Math.max(c1, c2);
        ArrayList<Integer> hs1 = new ArrayList<Integer>(cromosoma.length);
        ArrayList<Integer> hs2 = new ArrayList<Integer>(cromosoma.length);
        for(int i = iter1; i < iter2; i++){
            hs1.set(i, this.cromosoma[i]);
            hs2.set(i, crom.cromosoma[i]);
        }

        for(int i = 0; i < this.cromosoma[i]; i++){
            if(iter1 < i && i < iter2){
                continue;
            }
            Integer cand1 = crom.cromosoma[i];
            while(hs1.contains(cand1)){
                int indexNuevo = hs2.indexOf(cand1);
                cand1 = this.cromosoma[indexNuevo];
            }
            hs1.set(i, cand1);

            Integer cand2 = this.cromosoma[i];
            while(hs2.contains(cand2)){
                int indexNuevo = hs1.indexOf(cand2);
                cand2 = this.cromosoma[indexNuevo];
            }
            hs2.set(i, cand2);
        }

        CromosomasDron[] cd = new CromosomasDron[2];

        int index1 = 0;
        for (final Integer value: hs1) {
            arr1[index1++] = value;
        }

        int index2 = 0;
        for (final Integer value: hs2) {
            arr2[index2++] = value;
        }

        cd[0] = new CromosomasDron(camaras, drones, arr1);
        cd[1] = new CromosomasDron(camaras, drones, arr2);
        return cd;

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
