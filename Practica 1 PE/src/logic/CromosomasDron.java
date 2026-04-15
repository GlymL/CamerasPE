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
    
    public CromosomasDron(int camaras, int drones, Integer[] cromosoma){
        this.drones = drones;
        this.camaras = camaras;
        this.cromosoma = cromosoma.clone();
    }

    public int getLength() {
        return cromosoma.length;
    }
    public int getCamaras(){
        return camaras;
    }
    public int getDrones() {
        return drones;
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

    public CromosomasDron[] crucePMX(CromosomasDron crom, double crossRatio) {
        if(r.nextDouble() > crossRatio){
            return new CromosomasDron[]{ this.clone(), crom.clone() };
        }
        int c1, c2;
        c1 = r.nextInt(cromosoma.length - 1);
        c2 = r.nextInt(cromosoma.length - 1);
        Integer[] arr1 = new Integer[cromosoma.length];
        Integer[] arr2 = new Integer[cromosoma.length];
        int iter1 = Math.min(c1, c2);
        int iter2 = Math.max(c1, c2);
        ArrayList<Integer> padre1 = new ArrayList<>(Arrays.asList(cromosoma));
        ArrayList<Integer> padre2 = new ArrayList<>(Arrays.asList(crom.cromosoma));
        for(int i = iter1; i <= iter2; i++){
            arr1[i] = crom.cromosoma[i];
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

    public CromosomasDron[] cruceOX(CromosomasDron crom, double crossRatio) {
        if(r.nextDouble() > crossRatio){
            return new CromosomasDron[]{ this.clone(), crom.clone() };
        }
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

    public CromosomasDron[] cruceOXOP(CromosomasDron crom, double crossRatio) {
        if (r.nextDouble() > crossRatio) {
            return new CromosomasDron[]{ this.clone(), crom.clone() };
        }

        int n = cromosoma.length;

        Integer[] hijo1 = new Integer[n];
        Integer[] hijo2 = new Integer[n];

        boolean[] selected = new boolean[n];

        for (int i = 0; i < n / 2; i++) {
            int pos;
            do {
                pos = r.nextInt(n);
            } while (selected[pos]);
            selected[pos] = true;
        }

        for (int i = 0; i < n; i++) {
            if (selected[i]) {
                hijo1[i] = cromosoma[i];
            }
        }

        int j = 0;
        for (int i = 0; i < n; i++) {
            if (hijo1[i] == null) {
                while (contains(hijo1, crom.cromosoma[j])) {
                    j++;
                }
                hijo1[i] = crom.cromosoma[j++];
            }
        }

        for (int i = 0; i < n; i++) {
            if (selected[i]) {
                hijo2[i] = crom.cromosoma[i];
            }
        }

        j = 0;
        for (int i = 0; i < n; i++) {
            if (hijo2[i] == null) {
                while (contains(hijo2, cromosoma[j])) {
                    j++;
                }
                hijo2[i] = cromosoma[j++];
            }
        }

        return new CromosomasDron[]{
            new CromosomasDron(camaras, drones, hijo1),
            new CromosomasDron(camaras, drones, hijo2)
        };
    }

    private boolean contains(Integer[] arr, int val) {
        for (Integer x : arr) {
            if (x != null && x == val) return true;
        }
        return false;
    }

    public CromosomasDron[] cruceCX(CromosomasDron crom, double crossRatio) {
        if(r.nextDouble() > crossRatio){
            return new CromosomasDron[]{ this.clone(), crom.clone() };
        }
        Integer[] hijo1 = new Integer[cromosoma.length];
        Integer[] hijo2 = new Integer[cromosoma.length];
        ArrayList<Integer> padre1 = new ArrayList<>(Arrays.asList(cromosoma));
        int indexActual = 0;
        while(hijo1[indexActual] == null){
            hijo1[indexActual] = this.cromosoma[indexActual];
            hijo2[indexActual] = crom.cromosoma[indexActual];

            int valor = crom.cromosoma[indexActual];
            indexActual = padre1.indexOf(valor);
        }

        for(int i = 0; i < cromosoma.length; i++){
            if(hijo1[i] == null){
                hijo1[i] = crom.cromosoma[i];
                hijo2[i] = cromosoma[i];
            }
        }

        CromosomasDron[] ret = new CromosomasDron[2];
        ret[0] = new CromosomasDron(camaras, drones, hijo1);
        ret[1] = new CromosomasDron(camaras, drones, hijo2);  

        return ret;
    }

    public CromosomasDron[] cruceCO(CromosomasDron crom, double crossRatio) {
        if(r.nextDouble() > crossRatio){
            return new CromosomasDron[]{ this.clone(), crom.clone() };
        }

        class Ordinal {
            private static Integer[] toOrdinal(Integer[] arr) {
                Integer[] res = new Integer[arr.length];
                ArrayList<Integer> indexGiver = new ArrayList<>(arr.length);

                for (int i = 0; i < arr.length; i++) {
                    indexGiver.add(i);
                }

                int i = 0;

                while (i < res.length && !indexGiver.isEmpty()) {
                    Integer elem = arr[i];

                    if (!indexGiver.contains(elem)) {
                        throw new UnsupportedOperationException("|||||  Trouble with IndexGiver found");
                    }
                    
                    int index = indexGiver.indexOf(elem);

                    res[i] = index;
                    indexGiver.remove((int) index);

                    i++;
                }

                return res;
            }

            private static Integer[] toPerm(Integer[] arr) {
                Integer[] res = new Integer[arr.length];
                ArrayList<Integer> indexGiver = new ArrayList<>(arr.length);

                for (int i = 0; i < arr.length; i++) {
                    indexGiver.add(i);
                }

                int i = 0;

                while (i < res.length && !indexGiver.isEmpty()) {
                    Integer index = arr[i];

                    if (indexGiver.size() < index) {
                        throw new UnsupportedOperationException("|||||  Trouble with IndexGiver perm");
                    }
                    
                    int elem = indexGiver.get(index);

                    res[i] = elem;
                    indexGiver.remove((int) index);

                    i++;
                }

                return res;
            }
        }

        Integer[] padre1Ordinal = Ordinal.toOrdinal(this.cromosoma);
        Integer[] padre2Ordinal = Ordinal.toOrdinal(crom.cromosoma);
        Integer[] hijo1Ordinal = new Integer[cromosoma.length];
        Integer[] hijo2Ordinal = new Integer[cromosoma.length];

        int cross_point = r.nextInt(cromosoma.length);

        for (int i = 0; i < cross_point; i++) {
            hijo1Ordinal[i] = padre1Ordinal[i];
            hijo2Ordinal[i] = padre2Ordinal[i];
        }

        for (int i = cross_point; i < cromosoma.length; i++) {
            hijo1Ordinal[i] = padre2Ordinal[i];
            hijo2Ordinal[i] = padre1Ordinal[i];
        }

        Integer[] hijo1 = Ordinal.toPerm(hijo1Ordinal);
        Integer[] hijo2 = Ordinal.toPerm(hijo2Ordinal);

        //throw new UnsupportedOperationException("Unimplemented method 'cruceCO'");
        CromosomasDron[] ret = new CromosomasDron[2];
        ret[0] = new CromosomasDron(camaras, drones, hijo1);
        ret[1] = new CromosomasDron(camaras, drones, hijo2);  


        return ret;
    }

    public CromosomasDron[] cruceERX(CromosomasDron crom, double crossRatio) {

        if(r.nextDouble() > crossRatio){
            return new CromosomasDron[]{ this.clone(), crom.clone() };
        }

        Integer[] hijo1 = new Integer[cromosoma.length];
        Integer[] hijo2 = new Integer[cromosoma.length];
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cruceERX'");
        
    }


    // 0 3 4 5 1 2
    // 1 3 2 4 0 5
    
    public CromosomasDron[] cruceCUSTOM(CromosomasDron crom, double crossRatio) {
        if (r.nextDouble() > crossRatio) {
            return new CromosomasDron[]{ this.clone(), crom.clone() };
        }

        int n = cromosoma.length;

        Integer[] hijo1 = new Integer[n];
        Integer[] hijo2 = new Integer[n];

        boolean[] visited = new boolean[n];

        ArrayList<Integer> padre1 = new ArrayList<>(Arrays.asList(this.cromosoma));
        ArrayList<Integer> padre2 = new ArrayList<>(Arrays.asList(crom.cromosoma));

        boolean takeFromParent1 = true;

        for (int start = 0; start < n; start++) {

            if (visited[start]) continue;

            int index = start;


            do {
                visited[index] = true;

                if (takeFromParent1) {
                    hijo1[index] = this.cromosoma[index];
                    hijo2[index] = crom.cromosoma[index];
                } else {
                    hijo1[index] = crom.cromosoma[index];
                    hijo2[index] = this.cromosoma[index];
                }

                int value = crom.cromosoma[index];
                index = padre1.indexOf(value);

            } while (index != start);


            takeFromParent1 = !takeFromParent1;
        }

        return new CromosomasDron[]{
            new CromosomasDron(camaras, drones, hijo1),
            new CromosomasDron(camaras, drones, hijo2)
        };
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
        Integer[] copy = cromosoma.clone();
        if(r.nextDouble() < mut){
            int c1 = r.nextInt(cromosoma.length);
            int c2 = r.nextInt(cromosoma.length);
            int aux = copy[c1];
            copy[c1] = copy[c2];
            copy[c2] = aux;
        }
        return new CromosomasDron(camaras, drones, copy);
    }

    public CromosomasDron mutacionInversion(double mut) {
         Integer[] copy = cromosoma.clone();
        if(r.nextDouble() < mut){
            int c1 = r.nextInt(cromosoma.length);
            int c2 = r.nextInt(cromosoma.length);
            int iter1 = Math.min(c1, c2);
            int iter2 = Math.max(c1, c2);
            Integer[] arraux = new Integer[iter2 - iter1 + 1];
            int aux = 0;
            for(int i = iter1; i <= iter2; i ++){
                arraux[aux++] = copy[i];
            }

             for (Integer arraux1 : arraux) {
                 copy[iter2--] = arraux1;
             }
        }
        return new CromosomasDron(camaras, drones, copy);
    }

    public CromosomasDron mutacionHeuristica(double mut, AEstrellaPrecalc ae) {
        int n = 3;
        int[] individuals = new int[n];
        if(r.nextDouble() < mut){
            for(int i = 0; i < n; i++){
                int posible = r.nextInt(cromosoma.length);
                while(individuals[0] == posible || individuals[1] == posible){
                    posible = r.nextInt(cromosoma.length);
                }
                individuals[i] = posible;
            }
            Integer[][] permutations = permutaciones(individuals);
            Fitness[] cd = new Fitness[permutations.length];
            int iter = 0;
            for (Integer[] elem : permutations) {
                Integer[] crom = new Integer[cromosoma.length];
                for(int i = 0; i < cromosoma.length; i++){
                    if(i == individuals[0]){
                        crom[i] = elem[0];
                    }else if (i == individuals[1]){
                        crom[i] = elem[1];
                    }else if (i == individuals[2]){
                        crom[i] = elem[2];
                    }else{
                        crom[i] = cromosoma[i];
                    }
                }
                cd[iter++] = new Fitness(new CromosomasDron(camaras, drones, crom), ae);
            }
            Fitness best = null;
            for(Fitness f : cd){
                f.calculateFitness();
                if(best == null)
                    best = f;
                if(best.getFitness() > f.getFitness())
                    best = f;
            }
            return best.getCrom();
        }
        return this.clone();
    }

    public Integer[][] permutaciones(int[] individuals) {
    return new Integer[][] {
        {cromosoma[individuals[0]], cromosoma[individuals[1]], cromosoma[individuals[2]]},
        {cromosoma[individuals[0]], cromosoma[individuals[2]], cromosoma[individuals[1]]},
        {cromosoma[individuals[1]], cromosoma[individuals[0]], cromosoma[individuals[2]]},
        {cromosoma[individuals[1]], cromosoma[individuals[2]], cromosoma[individuals[0]]},
        {cromosoma[individuals[2]], cromosoma[individuals[0]], cromosoma[individuals[1]]},
        {cromosoma[individuals[2]], cromosoma[individuals[1]], cromosoma[individuals[0]]}
    };
}

    public CromosomasDron mutacionCustom(double mut) {
        Integer[] copy = cromosoma.clone();

        if(r.nextDouble() < mut){
            double forSwapRatio = 0.4, whileSwapRatio = Math.min(0.9, forSwapRatio * 1.5);

            int i = 0, j = 0;

            while (i < copy.length) {
                double rand1 = r.nextDouble();

                if (rand1 < forSwapRatio) {
                    j = i + 1;

                    while (j < copy.length) {
                        double rand2 = r.nextDouble();

                        if (rand2 < whileSwapRatio) {
                            int aux = copy[i];
                            copy[i] = copy[j];
                            copy[j] = aux;

                            break;
                        }

                        j++;
                    }
                }

                i++;
            }
        }
        return new CromosomasDron(camaras, drones, copy);
        //throw new UnsupportedOperationException("Unimplemented method 'mutacionCustom'");
    }
}
