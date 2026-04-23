package logic;

import java.util.ArrayList;
import mapaApp.GeneradorMapa;

public class Population {
    //REVISAR SI EL FITNESS SE COMPARA BIEN
    
    private int pop_size;
    private double fTotal;
    private final ArrayList<Fitness> generation;
    private final Cruce cr;
    private final double elitismo;
    private final Selection s;
    private final Mutacion m;

    private final double bloating;

    private final TreeGenerator tg;
    private final GeneradorMapa map;

    public Population(GeneradorMapa gc, int popSize, double crossRatio, double mutRatio, double elitismo, double bloating,
        EnumMutacion mut, EnumSelection enumS, TreeGenerator tg) {

        generation = new ArrayList<>(popSize);
        pop_size = popSize;

        map = gc;

        this.cr = new Cruce(crossRatio);
        this.elitismo = elitismo;
        this.bloating = bloating;
        m = new Mutacion(mut, mutRatio);
        s = new Selection(enumS);
        this.tg = tg;


        initFitness();
        evaluateAll();
        sortByFitness();
    }

    public void evaluateAll() {
        fTotal = 0.0;
        for (Fitness cf : generation) {
            cf.calculateFitness();
            fTotal += cf.getFitness();
        }
        calculateAptitudes();
    }

    private void calculateAptitudes(){
        double minFitness = Double.POSITIVE_INFINITY;
        for (Fitness cf : generation) {
            minFitness = Math.min(minFitness, cf.getFitness());
        }

        double offset = minFitness <= 0 ? 1 - minFitness : 0;

        fTotal = 0.0;
        for (Fitness cf : generation) {
            fTotal += cf.getFitness() + offset;
        }

        if (fTotal <= 0) {
            for (Fitness cf : generation) {
                cf.setAptitude(1.0);
            }
            return;
        }

        for (Fitness cf : generation) {
            cf.setAptitude((cf.getFitness() + offset) / fTotal);
        }
    }

    public double getTotalFitness() {
        return fTotal;
    }


    public synchronized void sortByFitness() {
        generation.sort((a, b) -> a.compareTo(b));
    }

    private void initFitness() {
        ASTNode[] crom_infos = tg.randomInit(pop_size);
        
        for (ASTNode c : crom_infos) {
            generation.add(new Fitness(new CromosomaRanger(c), map, bloating));
        }
    }

    public void evolve() {
        ArrayList<Fitness> selected;
        ArrayList<Fitness> elite = new ArrayList<>();

        sortByFitness();
        if(elitismo > 0){
            for(int i = 0; i < elitismo*generation.size(); i++){
                elite.add(generation.get(i).clone());
            }
        }

        selected = s.select(generation);
        if (selected == null || selected.isEmpty()) {
            selected = new ArrayList<>();
            for (Fitness cf : generation) {
                selected.add(cf.clone());
            }
        }

        generation.clear();
        generation.addAll(selected);

        cr.cruzar(generation, map, bloating);
        
        m.mutar(generation, map, bloating);

        
        evaluateAll();
        
        sortByFitness();

        for(int i = 0; i < elite.size(); i++){
            generation.set(generation.size() - i - 1, elite.get(i).clone());
        }


        calculateAptitudes();
        sortByFitness();

    }


    public void reportBest() {
        if (generation.isEmpty()) {
            System.out.println("generation is empty!");
            return;
        }
        Fitness best = generation.get(0);
        System.out.println("Best fitness: " + best.getFitness());
    }

    // public int[][] mapBest(){
    //     sortByFitness();
    //     return generation.get(0).getCrom().rutas();
    // }

    public double averageFitness() {
        double sum = 0;
        for (Fitness cf : generation) 
            sum += cf.getFitness();
        return sum / generation.size();
    }
    
    public double bestFitness(){
        sortByFitness();
        return generation.get(0).getFitness();
    }

    public double bestAptitude(){
        sortByFitness();
        return generation.get(0).getAptitude();
    }

    

    public Fitness best() {
        sortByFitness();
        return generation.get(0);
    }

    double averageAptitude() {
        double sum = 0;
        for (Fitness cf : generation) 
            sum += cf.getAptitude();
        return sum / generation.size();
    }


}
