package logic;

import java.util.ArrayList;
import mapaApp.GeneradorCamaras;

public class Population {
    
    private double fTotal;
    private final ArrayList<FitnessDron> generation;
    private final Cruce cr;
    private final double elitismo;
    private final Selection s;
    private final Mutacion m;
    private final AEstrellaPrecalc precalc;


    public Population(GeneradorCamaras gc, int popSize, 
        double crossRatio, double mutRatio, EnumCruce cr, 
        EnumMutacion mut, EnumSelection enumS, double elitismo, int n_drones, AEstrellaPrecalc ae) {
            generation = new ArrayList<>();
        this.cr = new Cruce(cr, crossRatio);
        this.elitismo = elitismo;
        this.precalc = ae;
        m = new Mutacion(mut, mutRatio);
        s = new Selection(enumS);
        initializeRandom(popSize, precalc, gc, n_drones);
        evaluateAll();
        sortByFitness();
    }

    private void initializeRandom(int popSize, AEstrellaPrecalc precalc, GeneradorCamaras gc, int n_drones) {
        
        for (int i = 0; i < popSize; i++) {
            CromosomasDron c;
            c = new CromosomasDron(gc.getCameras().length, n_drones);
            c.randomInitialize();

            FitnessDron f = new FitnessDron(c, precalc);
            generation.add(f);
        }
    }

    public void evaluateAll() {
        fTotal = 0.0;
        for (FitnessDron cf : generation) {
            cf.calculateFitness();
            fTotal += cf.getFitness();
        }
        calculateAptitudes();
    }

    private void calculateAptitudes(){
        for(FitnessDron cf : generation){
            double apt = fTotal/cf.getFitness();
            cf.setAptitude(apt);
        }
    }

    public double getTotalFitness() {
        return fTotal;
    }


    public synchronized void sortByFitness() {
        generation.sort((a, b) -> b.compareTo(a));
    }




    public void evolve() {

        ArrayList<FitnessDron> selected;
        ArrayList<FitnessDron> elite = new ArrayList<>();

        sortByFitness();
        if(elitismo > 0){
            for(int i = 0; i < elitismo*generation.size(); i++){
                elite.add(generation.get(i).clone());
                System.out.println("Elite: " + generation.get(i).getFitness());
            }
        }

        selected = s.select(generation);
        
        generation.clear();
        generation.addAll(selected);

        cr.cruzar(generation, precalc);
        
        m.mutar(generation, precalc);
        
        evaluateAll();
        
        sortByFitness();

        for(int i = 0; i < elite.size(); i++){
            generation.set(generation.size() - i - 1, elite.get(i).clone());
        }
        
        evaluateAll();
        
        sortByFitness();

    }


    public void reportBest() {
        if (generation.isEmpty()) {
            System.out.println("generation is empty!");
            return;
        }
        FitnessDron best = generation.get(0);
        System.out.println("Best fitness: " + best.getFitness());
    }

    public int[][] mapBest(){
        sortByFitness();
        return generation.get(0).getCrom().rutas();
    }

    public double averageFitness() {
        double sum = 0;
        for (FitnessDron cf : generation) 
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

    

    public FitnessDron best() {
        sortByFitness();
        return generation.get(0);
    }

    double averageAptitude() {
        double sum = 0;
        for (FitnessDron cf : generation) 
            sum += cf.getAptitude();
        return sum / generation.size();
    }


}
