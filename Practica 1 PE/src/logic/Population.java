package logic;

import java.util.ArrayList;
import java.util.Random;
import mapaApp.MapaCamaras;

public class Population {
    private ArrayList<CromFit> population;
    private MapaCamaras mapa;
    private double fTotal;

    private double crossoverRatio;
    private double mutationRatio;
    private Random rand = new Random();
    private boolean ponderado;
    private boolean monopunto;


    public Population(MapaCamaras mapa, int popSize, double crossRatio, double mutRatio, boolean ponder, boolean monop) {
        this.mapa = mapa;
        this.population = new ArrayList<>();
        this.ponderado = ponder;
        this.monopunto = monop;
        crossoverRatio = crossRatio;
        mutationRatio = mutRatio;
        initializeRandom(popSize);
        evaluateAll();
        calculateAptitudes();
        sortByFitness();
    }

    private void initializeRandom(int popSize) {
        for (int i = 0; i < popSize; i++) {
            Cromosomas c = new Cromosomas(mapa.getFilas(), mapa.getCols(), mapa.getNumCams());
            c.randomInitialize();
            Fitness f = new Fitness(mapa, c.decode(), ponderado);
            population.add(new CromFit(c, f));
        }
    }

    public void evaluateAll() {
        fTotal = 0.0;
        for (CromFit cf : population) {
            cf.setFit(new Fitness(mapa, cf.getCrom().decode(), ponderado));
            if(cf.getFit().getPunt() > 0)
                fTotal += cf.getFit().getPunt();
        }
    }

    public double getTotalFitness() {
        return fTotal;
    }

    public void calculateAptitudes() {
        for (CromFit cf : population) {
            cf.calculateAptitude(fTotal);
        }
    }

    public void sortByFitness() {
        population.sort((a, b) -> b.compareTo(a));
    }


    public ArrayList<CromFit> rouletteSelection(int selectionSize) {
        ArrayList<CromFit> selected = new ArrayList<>();
        ArrayList<Double> cumulative = new ArrayList<>();
        
        double sum = 0;
        for (CromFit cf : population) {
            sum += Math.max(0, cf.getApt());
            cumulative.add(sum);
        }

        if (sum == 0) {
            for (int i = 0; i < selectionSize; i++) {
                selected.add(population.get(rand.nextInt(population.size())).clone());
            }
            return selected;
        }

        for (int i = 0; i < selectionSize; i++) {
            double r = rand.nextDouble();
            for (int j = 0; j < cumulative.size(); j++) {
                if (r <= cumulative.get(j)) {
                    selected.add(population.get(j).clone());
                    break;
                }
            }
        }
        return selected;
    }

    public ArrayList<CromFit> truncSelection(int size, int elements) {
        ArrayList<CromFit> selected = new ArrayList<>();
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

    public ArrayList<CromFit> torneoSelection(int size) {
        ArrayList<CromFit> selected = new ArrayList<>();
        for(int i = 0; i < size; i++){
            ArrayList<CromFit> torneo = new ArrayList<>();
            CromFit c1 = population.get(rand.nextInt(population.size()));
            torneo.add(c1);
            CromFit c2 = population.get(rand.nextInt(population.size()));
            torneo.add(c2);
            CromFit c3 = population.get(rand.nextInt(population.size()));
            torneo.add(c3);
            torneo.sort((a, b) -> b.compareTo(a));
            selected.add(torneo.get(0).clone());
        }
        return selected;
    }

    public void applyCrossover() {
        for (int i = 0; i < population.size() / 2; i++) {
            if(monopunto){
                int c1 = rand.nextInt(population.size());
                int c2 = rand.nextInt(population.size());
                Cromosomas[] children = population.get(c1).getCrom().cruceMonop(population.get(c2).getCrom(), crossoverRatio);
                population.set(c1, new CromFit(children[0], new Fitness(mapa, children[0].decode(), ponderado)));
                population.set(c2, new CromFit(children[1], new Fitness(mapa, children[1].decode(), ponderado)));
            }else{
                if (rand.nextDouble() < crossoverRatio) {
                    int c1 = rand.nextInt(population.size());
                    int c2 = rand.nextInt(population.size());
                    Cromosomas[] children = population.get(c1).getCrom().cruceUnif(population.get(c2).getCrom());
                    population.set(c1, new CromFit(children[0], new Fitness(mapa, children[0].decode(), ponderado)));
                    population.set(c2, new CromFit(children[1], new Fitness(mapa, children[1].decode(), ponderado)));
                }
            }
        }
    }

    public void applyMutation() {
        for (CromFit cf : population) {
            cf.getCrom().mutarCromosoma(mutationRatio);
            cf.setFit(new Fitness(mapa, cf.getCrom().decode(), ponderado));
        }
    }


    public void evolve(Selection m) {

        ArrayList<CromFit> selected = new ArrayList<>();

        if (m.equals(Selection.RULETA))
            selected = rouletteSelection(population.size());
        else if (m.equals(Selection.TORNEO))
            selected = torneoSelection(population.size());
        else if (m.equals(Selection.ESTOCASTICO))
            selected = torneoSelection(population.size());
        else if (m.equals(Selection.TRUNCAMIENTO))
            selected = truncSelection(population.size(), population.size()/10);
        else if (m.equals(Selection.RESTOS))
            selected = torneoSelection(population.size());

        population.clear();
        population.addAll(selected);

        applyCrossover();

        applyMutation();

        evaluateAll();

        calculateAptitudes();

        sortByFitness();
    }


    public void reportBest() {
        if (population.isEmpty()) {
            System.out.println("Population is empty!");
            return;
        }
        CromFit best = population.get(0);
        System.out.println("Best fitness: " + best.getFit().getPunt());
        best.getFit().print();
    }

    public double averageFitness() {
        double sum = 0;
        for (CromFit cf : population) 
            sum += cf.getFit().getPunt();
        return sum / population.size();
    }
    
    public double bestFitness(){
        sortByFitness();
        return population.get(0).getApt();
    }



}
