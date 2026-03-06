package logic;

import java.lang.module.FindException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Random;

import logic.FitnessDron;
import mapaApp.GeneradorCamaras;
import mapaApp.MapaCamaras;

public class Population {
    private MapaCamaras mapa;
    private double fTotal;

    private ArrayList<FitnessDron> population;
    private double crossoverRatio;
    private double mutationRatio;
    private Random rand = new Random(3000);
    private boolean ponderado;
    private Cruce cruce;
    private Mutacion mut;
    private double elitismo;
    private boolean binario;


    public Population(MapaCamaras mapa, int popSize, 
        double crossRatio, double mutRatio, boolean ponder, Cruce cruce, double elitismo, boolean binario, Mutacion m) {
        this.mapa = mapa;
        this.ponderado = ponder;
        this.cruce = cruce;
        this.elitismo = elitismo;
        this.binario = binario;
        this.mut = m;
        crossoverRatio = crossRatio;
        mutationRatio = mutRatio;
        initializeRandom(popSize, this.binario);
        evaluateAll();
        sortByFitness();
    }

    private void initializeRandom(int popSize, boolean binario) {
        GeneradorCamaras gc = new GeneradorCamaras(3000, mapa);
        for (int i = 0; i < popSize; i++) {
            CromosomasDron c;
            c = new CromosomasDron(mapa.getNumCams(), 0);
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


    public ArrayList<FitnessDron> rouletteSelection(int size) {
        ArrayList<FitnessDron> selected = new ArrayList<>();
        ArrayList<Double> cumulative = new ArrayList<>();
        double minFit = population.stream()
                                .mapToDouble(cf -> cf.getFit().getPunt())
                                .min()
                                .orElse(0.0);

        double shift = (minFit < 0) ? -minFit : 0.0;
        double sum = 0;

        for (FitnessDron cf : population) {
            double value = cf.getFit().getPunt() + shift;  // all >= 0
            sum += value;
            cumulative.add(sum);
        }

        // fallback if sum == 0
        if (sum == 0) {
            for (int i = 0; i < size; i++) {
                selected.add(population.get(rand.nextInt(population.size())).clone());
            }
            return selected;
        }

        // now do standard roulette selection
        for (int i = 0; i < size; i++) {
            double r = rand.nextDouble() * sum;
            for (int j = 0; j < cumulative.size(); j++) {
                if (r <= cumulative.get(j)) {
                    selected.add(population.get(j).clone());
                    break;
                }
            }
        }
        return selected;
    }

    public ArrayList<FitnessDron> truncSelection(int size, int elements) {
        ArrayList<FitnessDron> selected = new ArrayList<>();
        sortByFitness();
        while(selected.size() < size){
            int i = 0;
            while(selected.size() < size && i < elements){
                    selected.add(population.get(i).clone());
                    i++;
                }
            }
        return selected;
    }

    public ArrayList<FitnessDron> torneoSelection(int size) {
        ArrayList<FitnessDron> selected = new ArrayList<>();
        for(int i = 0; i < size; i++){
            ArrayList<FitnessDron> torneo = new ArrayList<>();
            FitnessDron c1 = population.get(rand.nextInt(population.size()));
            torneo.add(c1);
            FitnessDron c2 = population.get(rand.nextInt(population.size()));
            torneo.add(c2);
            FitnessDron c3 = population.get(rand.nextInt(population.size()));
            torneo.add(c3);
            torneo.sort((a, b) -> b.compareTo(a));
            selected.add(torneo.get(0).clone());
        }
        return selected;
    }

    public ArrayList<FitnessDron> estocasticoSelection(int size){
        ArrayList<FitnessDron> selected = new ArrayList<>();
        ArrayList<Double> cumulative = new ArrayList<>();
        
        double sum = 0;
        for (FitnessDron cf : population) {
            sum += Math.max(0, cf.getApt());
            cumulative.add(sum);
        }

        if (sum == 0) {
            for (int i = 0; i < size; i++) {
                selected.add(population.get(rand.nextInt(population.size())).clone());
            }
            return selected;
        }

        double r = rand.nextDouble();
        int inicio = 0;
        for (int i = 0; i < size; i++) {
            double a = (r + i)/size;
            for (int j = inicio; j < cumulative.size(); j++) {
                if (a <= cumulative.get(j)) {
                    selected.add(population.get(j).clone());
                    inicio = j;
                    break;
                }
            }
        }
        return selected;
    }

    public ArrayList<FitnessDron> restosSelection(int size) {
        ArrayList<FitnessDron> selected = new ArrayList<>();

        for(int i = 0; i < size; i++){
            int copias = (int)Math.floor(population.get(i).getApt()*size);
            for(int j = 0; j < copias; j++){
                selected.add(population.get(i).clone());
            }
        }
        ArrayList<FitnessDron> trunc = torneoSelection(size - selected.size());
        for(FitnessDron cf : trunc){
            selected.add(cf.clone());
        }
        return selected;
    }

    public void applyCrossover() {
        for (int i = 0; i < population.size() / 2; i++) {
            if(cruce == Cruce.MONOPUNTO){
                int c1 = rand.nextInt(population.size());
                int c2 = rand.nextInt(population.size());
                Cromosoma[] children = (Cromosoma[])population.get(c1).getCrom().cruceMonop(population.get(c2).getCrom(), crossoverRatio);
                if(binario){
                    population.set(c1, new FitnessDron(children[0], new FitnessBinario(mapa, children[0], ponderado)));
                    population.set(c2, new FitnessDron(children[1], new FitnessBinario(mapa, children[1], ponderado)));
                } else{
                    population.set(c1, new FitnessDron(children[0], new FitnessReal(mapa, children[0], ponderado)));
                    population.set(c2, new FitnessDron(children[1], new FitnessReal(mapa, children[1], ponderado)));
                }
            }else if (cruce == Cruce.UNIFICADO){
                if (rand.nextDouble() < crossoverRatio) {
                    int c1 = rand.nextInt(population.size());
                    int c2 = rand.nextInt(population.size());
                    Cromosoma[] children = (Cromosoma[])population.get(c1).getCrom().cruceUnif(population.get(c2).getCrom());
                    if(binario){
                        population.set(c1, new FitnessDron(children[0], new FitnessBinario(mapa, children[0], ponderado)));
                        population.set(c2, new FitnessDron(children[1], new FitnessBinario(mapa, children[1], ponderado)));
                    } else{
                        population.set(c1, new FitnessDron(children[0], new FitnessReal(mapa, children[0], ponderado)));
                        population.set(c2, new FitnessDron(children[1], new FitnessReal(mapa, children[1], ponderado)));
                    }
                }
            } else if (cruce == Cruce.ARITMETICO){
                int c1 = rand.nextInt(population.size());
                int c2 = rand.nextInt(population.size());
                Cromosoma[] children = (Cromosoma[])population.get(c1).getCrom().cruceArit(population.get(c2).getCrom(), crossoverRatio);
                population.set(c1, new FitnessDron(children[0], new FitnessReal(mapa, children[0], ponderado)));
                population.set(c2, new FitnessDron(children[1], new FitnessReal(mapa, children[1], ponderado)));
            }
            else{
                int c1 = rand.nextInt(population.size());
                int c2 = rand.nextInt(population.size());
                Cromosoma[] children = (Cromosoma[])population.get(c1).getCrom().cruceBLX(population.get(c2).getCrom(), crossoverRatio);
                if(binario){
                    population.set(c1, new FitnessDron(children[0], new FitnessBinario(mapa, children[0], ponderado)));
                    population.set(c2, new FitnessDron(children[1], new FitnessBinario(mapa, children[1], ponderado)));
                } else{
                    population.set(c1, new FitnessDron(children[0], new FitnessReal(mapa, children[0], ponderado)));
                    population.set(c2, new FitnessDron(children[1], new FitnessReal(mapa, children[1], ponderado)));
                }
            }
        }
    }

    public void applyMutation() {
        for (FitnessDron cf : population) {
            if(mut == Mutacion.BITGEN)
                cf.getCrom().mutarBitgen(mutationRatio);
            else
                cf.getCrom().mutarGauss(mutationRatio);
            if(binario)
                cf.setFit(new FitnessBinario(mapa, cf.getCrom(), ponderado));
            else
                cf.setFit(new FitnessReal(mapa, cf.getCrom(), ponderado));
        }
    }


    public void evolve(Selection m) {

        ArrayList<FitnessDron> selected = new ArrayList<>();
        ArrayList<FitnessDron> elite = new ArrayList<>();

        sortByFitness();
        if(elitismo > 0){
            for(int i = 0; i < elitismo*population.size(); i++){
                elite.add(population.get(i).clone());
            }
        }


        if (m.equals(Selection.RULETA))
            selected = rouletteSelection(population.size());
        else if (m.equals(Selection.TORNEO))
            selected = torneoSelection(population.size());
        else if (m.equals(Selection.ESTOCASTICO))
            selected = estocasticoSelection(population.size());
        else if (m.equals(Selection.TRUNCAMIENTO))
            selected = truncSelection(population.size(), population.size()/10);
        else if (m.equals(Selection.RESTOS))
            selected = restosSelection(population.size());

        
        population.clear();
        population.addAll(selected);

        applyCrossover();
        
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
        System.out.println("Best fitness: " + best.getFit().getPunt());
        best.getFit().print();
    }

    public int[][] mapBest(){
        sortByFitness();
        return population.get(0).getFit().print();
    }

    public double averageFitness() {
        double sum = 0;
        for (FitnessDron cf : population) 
            sum += cf.getFit().getPunt();
        return sum / population.size();
    }
    
    public double bestFitness(){
        sortByFitness();
        return population.get(0).getFit().getPunt();
    }

    public Cromosoma bestCrom(){
        sortByFitness();
        return population.get(0).getCrom();
    }


}
