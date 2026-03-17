package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CromosomasDron{

    private final Integer[] cromosoma;
    private final int drones;
    private final int camaras;

    private static final Random r = new Random();

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
       ArrayList<Integer> al = new ArrayList<>(Arrays.asList(cromosoma));

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
        for (Integer cromosoma1 : cromosoma) {
            if (cromosoma1 >= camaras) {
                dron++;
                if(dron >= drones)
                    break;
                iter = 0;
            } else {
                ret[dron][iter] = cromosoma1;
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
        c1 = r.nextInt(cromosoma.length);
        c2 = r.nextInt(cromosoma.length);
        Integer[] arr1 = new Integer[cromosoma.length];
        Integer[] arr2 = new Integer[cromosoma.length];
        int iter1 = Math.min(c1, c2);
        int iter2 = Math.max(c1, c2);
        ArrayList<Integer> padre1 = new ArrayList<>(Arrays.asList(cromosoma));
        ArrayList<Integer> padre2 = new ArrayList<>(Arrays.asList(crom.cromosoma));
        for(int i = iter1; i < iter2; i++){
            arr1[i] = this.cromosoma[i];
            arr2[i] = this.cromosoma[i];
        }
        ArrayList<Integer> hijo1 = new ArrayList<>(Arrays.asList(arr1));
        ArrayList<Integer> hijo2 = new ArrayList<>(Arrays.asList(arr2));

        for(int i = 0; i < this.cromosoma.length; i++){
            if(iter1 <= i && i <= iter2){
                continue;
            }
            Integer cand1 = crom.cromosoma[i];
            while(hijo1.contains(cand1)){
                int indexNuevo = padre2.indexOf(cand1);
                cand1 = this.cromosoma[indexNuevo];
            }
            arr1[i] = cand1;
            hijo1.set(i, cand1);

            Integer cand2 = this.cromosoma[i];
            while(hijo2.contains(cand2)){
                int indexNuevo = padre1.indexOf(cand2);
                cand2 = this.cromosoma[indexNuevo];
            }
            arr2[i] = cand2;
            hijo2.set(i, cand2);
        }

        CromosomasDron[] cd = new CromosomasDron[2];

        cd[0] = new CromosomasDron(camaras, drones, arr1);
        cd[1] = new CromosomasDron(camaras, drones, arr2);
        return cd;

    }

    public CromosomasDron[] cruceOP(CromosomasDron crom, double mutRatio) {
        int c1 = 0, c2 = 0;
        while (Math.abs(c1 - c2) <= 2){
            c1 = r.nextInt(cromosoma.length);
            c2 = r.nextInt(cromosoma.length);
        }
        int iter1 = Math.min(c1, c2);
        int iter2 = Math.max(c1, c2);
        Integer[] arr1 = new Integer[cromosoma.length];
        Integer[] arr2 = new Integer[cromosoma.length];
        for(int i = iter1; i < iter2; i++){
            arr1[i] = cromosoma[i];
            arr2[i] = crom.cromosoma[i];
        }

        // Paso 2: Extraer elementos en orden circular (desde corte2 + 1)
        Integer[] arraux1 = new Integer[cromosoma.length - (iter2-iter1)];
        Integer[] arraux2 = new Integer[cromosoma.length - (iter2-iter1)];
        Set<Integer> set1 = new HashSet<>();
        for(int i = iter1; i < iter2; i++){
            set1.add(arr1[i]);
        }
        Set<Integer> set2 = new HashSet<>();
        for(int i = iter1; i < iter2; i++){
            set2.add(arr2[i]);
        }
        int iter = 0;
        int i = 0;
        while(i < arr1.length){
            int index = (iter2 + i) % cromosoma.length;

            if(!set1.contains(crom.cromosoma[index])){
                arraux1[iter++] = crom.cromosoma[index];
            }
            i++;
        }

        i = 0; iter = 0;

        while(i < arr1.length){
            int index = (iter2 + i) % cromosoma.length;
            if(!set2.contains(cromosoma[index])){
                arraux2[iter++] = cromosoma[index];
            }
            i++;
        }

        // Paso 3: Rellenar los huecos en orden circular
        int index_rell1 = 0;
        int index_rell2 = 0;

        int pos = iter2;

        while(index_rell1 < arraux1.length){
            if(arr1[pos] == null){
                arr1[pos] = arraux1[index_rell1++];
            }
            pos = (pos + 1) % cromosoma.length;
        }

        pos = iter2;
        
        while(index_rell2 < arraux2.length){
            if(arr2[pos] == null){
                arr2[pos] = arraux2[index_rell2++];
            }
            pos = (pos + 1) % cromosoma.length;
        }

        CromosomasDron[] ret = new CromosomasDron[2];
        ret[0] = new CromosomasDron(camaras, drones, arr1);
        ret[1] = new CromosomasDron(camaras, drones, arr2);  

        return ret;
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
        Integer[] copy = cromosoma.clone();
        if(r.nextDouble() < mut){
            int c1 = r.nextInt(cromosoma.length);
            int c2 = r.nextInt(cromosoma.length);
            int iter1 = Math.min(c1, c2);
            int iter2 = Math.max(c1, c2);

            int aux = 0;
            for(int i = iter1; i <= iter2; i ++){
                if(i == iter1){
                    aux = copy[i];
                    copy[i] = copy[iter2];
                }else{
                    int aux2 = aux;
                    aux = copy[i];
                    copy[i] = aux2;
                }
            }
        }
        return new CromosomasDron(camaras, drones, copy);
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
