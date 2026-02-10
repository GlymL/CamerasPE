package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import mapaApp.MapaCamaras;

public class Population {
    private ArrayList<CromFit> population;
    private MapaCamaras mapa;
    private double fTotal;

    private double crossoverRatio;
    private double mutationRatio;
    private Random rand = new Random();


    public Population(MapaCamaras mapa, int popSize, double crossRatio, double mutRatio) {
        this.mapa = mapa;
        this.population = new ArrayList<>();
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
            Fitness f = new Fitness(mapa, c.decode());
            population.add(new CromFit(c, f));
        }
    }

    public void evaluateAll() {
        fTotal = 0.0;
        for (CromFit cf : population) {
            cf.setFit(new Fitness(mapa, cf.getCrom().decode()));
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
        Collections.sort(population, (a, b) -> b.compareTo(a));
    }


    public ArrayList<CromFit> rouletteSelection(int selectionSize) {
        ArrayList<CromFit> selected = new ArrayList<>();
        ArrayList<Double> cumulative = new ArrayList<>();
        
        double sum = 0;
        for (CromFit cf : population) {
            sum += cf.getApt();
            cumulative.add(sum);
        }

        for (int i = 0; i < selectionSize; i++) {
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

    public void applyCrossover() {
        for (int i = 0; i < population.size() / 2; i++) {
            if (rand.nextDouble() < crossoverRatio) {
                int c1 = rand.nextInt(population.size());
                int c2 = rand.nextInt(population.size());
                Cromosomas[] children = population.get(c1).getCrom().crossover(
                                        population.get(c2).getCrom());
                population.set(c1, new CromFit(children[0], new Fitness(mapa, children[0].decode())));
                population.set(c2, new CromFit(children[1], new Fitness(mapa, children[1].decode())));
            }
        }
    }

    public void applyMutation() {
        for (CromFit cf : population) {
            cf.getCrom().mutarCromosoma(mutationRatio);
            cf.setFit(new Fitness(mapa, cf.getCrom().decode()));
        }
    }


    public void evolve() {
        ArrayList<CromFit> selected = rouletteSelection(population.size());

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
        for (CromFit cf : population) sum += cf.getFit().getPunt();
        return sum / population.size();
    }



}
