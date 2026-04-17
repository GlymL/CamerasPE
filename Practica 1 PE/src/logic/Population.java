package logic;

import java.util.ArrayList;
import mapaApp.GeneradorMapa;

public class Population {
    
    private double fTotal;
    private final ArrayList<Fitness> generation;
    private final Cruce cr;
    private final double elitismo;
    private final Selection s;
    private final Mutacion m;


    private final RoverState state;
    private final TreeGenerator tg;

    private final boolean opt;


    public Population(GeneradorMapa gc, int popSize, 
        double crossRatio, double mutRatio, EnumCruce cr, 
        EnumMutacion mut, EnumSelection enumS, double elitismo, int n_drones, TreeGenerator tg, boolean opt) {
            generation = new ArrayList<>();
        this.cr = new Cruce(cr, crossRatio);
        this.elitismo = elitismo;
        // this.precalc = ae;
        m = new Mutacion(mut, mutRatio);
        s = new Selection(enumS);
        // initializeRandom(popSize, precalc, gc, n_drones);
        evaluateAll();
        sortByFitness();
        this.opt = opt;
    }

    private void initializeRandom(int popSize) {
        ASTNode[] trees = tg.randomInit(popSize);

        for (ASTNode tree : trees) {
            CromosomasRanger c = new CromosomasRanger(tree);

            generation.add(new Fitness(c));
        }
        // for (int i = 0; i < popSize; i++) {
        //     CromosomasRanger c;
        //     c = new CromosomasRanger(gc.getCameras().length, n_drones);
        //     c.randomInitialize();

        //     Fitness f = new Fitness(c, precalc);
        //     generation.add(f);
        // }
    }

    public void evaluateAll() {
        fTotal = 0.0;
        for (Fitness cf : generation) {
            cf.calculateFitness(state);
            fTotal += cf.getFitness();
        }
        calculateAptitudes();
    }

    private void calculateAptitudes(){
        fTotal = 0.0;
        for (Fitness cf : generation) {
            fTotal += cf.getFitness();
        }
        for(Fitness cf : generation){
            double apt = fTotal/cf.getFitness();
            cf.setAptitude(apt);
        }
    }

    public double getTotalFitness() {
        return fTotal;
    }


    public synchronized void sortByFitness() {
        generation.sort((a, b) -> a.compareTo(b));
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
        
        generation.clear();
        generation.addAll(selected);

        cr.cruzar(generation, precalc);
        
        m.mutar(generation, precalc);

        if(opt)
            generation.set(0, generation.get(0).untangle());
        
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

    public int[][] mapBest(){
        sortByFitness();
        return generation.get(0).getCrom().rutas();
    }

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
