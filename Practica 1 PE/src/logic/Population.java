package logic;

import java.util.ArrayList;

import mapaApp.GeneradorCamaras;
import mapaApp.MapaCamaras;

public class Population {
    
    private double fTotal;
    private ArrayList<FitnessDron> generation;
    private Cruce cr;
    private double elitismo;
    private Selection s;
    private Mutacion m;
    private GeneradorCamaras gc;


    public Population(MapaCamaras mapa, int popSize, 
        double crossRatio, double mutRatio, EnumCruce cr, 
        EnumMutacion mut, EnumSelection enumS, double elitismo, int n_drones) {
            generation = new ArrayList<FitnessDron>();
        this.cr = new Cruce(cr, crossRatio);
        this.elitismo = elitismo;
        m = new Mutacion(mut, mutRatio);
        s = new Selection(enumS);

        initializeRandom(popSize, mapa);
        evaluateAll();
        sortByFitness();
    }

    private void initializeRandom(int popSize, MapaCamaras mc) {
        gc = new GeneradorCamaras(3000, mc);
        for (int i = 0; i < popSize; i++) {
            CromosomasDron c;
            c = new CromosomasDron(mc.getNumCams(), 3);
            c.randomInitialize();
            FitnessDron f = new FitnessDron(c, gc);
            generation.add(f);
        }
    }

    public void evaluateAll() {
        fTotal = 0.0;
        for (FitnessDron cf : generation) {
            cf.calculateFitness();
        }
    }

    public double getTotalFitness() {
        return fTotal;
    }


    public void sortByFitness() {
        generation.sort((a, b) -> b.compareTo(a));
    }




    public void evolve() {

        ArrayList<FitnessDron> selected = new ArrayList<>();
        ArrayList<FitnessDron> elite = new ArrayList<>();

        sortByFitness();
        if(elitismo > 0){
            for(int i = 0; i < elitismo*generation.size(); i++){
                elite.add(generation.get(i).clone());
            }
        }

        selected = s.select(generation);
        
        generation.clear();
        generation.addAll(selected);

        cr.cruzar(generation, gc);
        
        m.mutar(generation, gc);
        
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
        return null /*generation.get(0).getFit().print*/ ;
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

    public FitnessDron best() {
        sortByFitness();
        return generation.get(0);
    }


}
