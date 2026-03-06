package logic;

import java.lang.module.FindException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Random;

import logic.FitnessDron;
import mapaApp.GeneradorCamaras;
import mapaApp.MapaCamaras;

public class Population {
    private double mutationRatio;
    private EnumMutacion mut;
    
    private double fTotal;
    private ArrayList<FitnessDron> population;
    private Cruce cr;
    private double elitismo;
    private Selection s;
    private GeneradorCamaras gc;


    public Population(MapaCamaras mapa, int popSize, 
        double crossRatio, double mutRatio, EnumCruce cr, 
        double elitismo, boolean binario, EnumMutacion m, EnumSelection enumS) {
        this.cr = new Cruce(cr, crossRatio);
        this.elitismo = elitismo;
        this.mut = m;
        mutationRatio = mutRatio;
        s = new Selection(enumS);
        initializeRandom(popSize, mapa);
        evaluateAll();
        sortByFitness();
    }

    private void initializeRandom(int popSize, MapaCamaras mc) {
        gc = new GeneradorCamaras(3000, mc);
        for (int i = 0; i < popSize; i++) {
            CromosomasDron c;
            c = new CromosomasDron(mc.getNumCams(), 0);
            c.randomInitialize();
            FitnessDron f = new FitnessDron(c, gc);
            population.add(f);
        }
    }

    public void evaluateAll() {
        fTotal = 0.0;
        for (FitnessDron cf : population) {
            cf.calculateFitness();
        }
    }

    public double getTotalFitness() {
        return fTotal;
    }


    public void sortByFitness() {
        population.sort((a, b) -> b.compareTo(a));
    }


    public void applyMutation() {
        for (FitnessDron cf : population) {
            if(mut == EnumMutacion.BITGEN)
                cf.getCrom().mutarBitgen(mutationRatio);
            else
                cf.getCrom().mutarGauss(mutationRatio);
            if(binario)
                cf.setFit(new FitnessBinario(mapa, cf.getCrom(), ponderado));
            else
                cf.setFit(new FitnessReal(mapa, cf.getCrom(), ponderado));
        }
    }


    public void evolve(EnumSelection m) {

        ArrayList<FitnessDron> selected = new ArrayList<>();
        ArrayList<FitnessDron> elite = new ArrayList<>();

        sortByFitness();
        if(elitismo > 0){
            for(int i = 0; i < elitismo*population.size(); i++){
                elite.add(population.get(i).clone());
            }
        }

        selected = s.select(population);
        
        population.clear();
        population.addAll(selected);

        cr.cruzar(population, gc);
        
        applyMutation();
        
        evaluateAll();
        
        sortByFitness();

        for(int i = 0; i < elite.size(); i++){
            population.set(population.size() - i - 1, elite.get(i).clone());
        }
        
        evaluateAll();
        
        sortByFitness();

    }


    public void reportBest() {
        if (population.isEmpty()) {
            System.out.println("Population is empty!");
            return;
        }
        FitnessDron best = population.get(0);
        System.out.println("Best fitness: " + best.getFitness());
    }

    public int[][] mapBest(){
        sortByFitness();
        return null /*population.get(0).getFit().print*/ ;
    }

    public double averageFitness() {
        double sum = 0;
        for (FitnessDron cf : population) 
            sum += cf.getFitness();
        return sum / population.size();
    }
    
    public double bestFitness(){
        sortByFitness();
        return population.get(0).getFitness();
    }

    // public Cromosoma bestCrom(){
    //     sortByFitness(); 
    //     return population.get(0).getCrom();
    // }


}
