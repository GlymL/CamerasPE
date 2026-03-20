package logic;

import java.util.ArrayList;
import java.util.Random;

public class Selection {
    
    private final EnumSelection es;
    private final Random rand = new Random();

    public Selection(EnumSelection es){
        this.es = es;
    }

    public ArrayList<FitnessDron> select(ArrayList<FitnessDron> population){
        
        ArrayList<FitnessDron> ret;

        switch (es){
        case EnumSelection.RULETA -> ret = rouletteSelection(population);
        case EnumSelection.TRUNCAMIENTO -> ret = truncSelection(population);
        case EnumSelection.TORNEO -> ret = torneoSelection(population);
        case EnumSelection.RESTOS -> ret = restosSelection(population);
        case EnumSelection.ESTOCASTICO -> ret = estocasticoSelection(population);
        case EnumSelection.RANKING -> ret = rankingSelection(population);
        default -> {
            System.err.println(("es en Selection es nulo"));
            return null;
            }

        }
        return ret;

    }

    
    private ArrayList<FitnessDron> rouletteSelection(ArrayList<FitnessDron> population) {
        ArrayList<FitnessDron> selected = new ArrayList<>();
        ArrayList<Double> cumulative = new ArrayList<>();
        double sum = 0;

        for (FitnessDron cf : population) {
            sum += cf.getAptitude();
            cumulative.add(sum);
        }

        if (sum == 0) {
            for (FitnessDron _ : population) {
                selected.add(population.get(rand.nextInt(population.size())).clone());
            }
            return selected;
        }

        for (FitnessDron _ : population) {
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


    private ArrayList<FitnessDron> truncSelection(ArrayList<FitnessDron> population) {
        ArrayList<FitnessDron> selected = new ArrayList<>();
        while(selected.size() < population.size()){
            int i = 0;
            while(selected.size() < population.size() && i < population.size()/10){
                    selected.add(population.get(i).clone());
                    i++;
                }
            }
        return selected;
    }

    private ArrayList<FitnessDron> torneoSelection(ArrayList<FitnessDron> population) {
        ArrayList<FitnessDron> selected = new ArrayList<>();
        for (FitnessDron _ : population) {
            ArrayList<FitnessDron> torneo = new ArrayList<>();
            FitnessDron c1 = population.get(rand.nextInt(population.size()));
            torneo.add(c1);
            FitnessDron c2 = population.get(rand.nextInt(population.size()));
            torneo.add(c2);
            FitnessDron c3 = population.get(rand.nextInt(population.size()));
            torneo.add(c3);
            torneo.sort((a, b) -> a.compareTo(b));
            selected.add(torneo.get(0).clone());
        }
        return selected;
    }

    private ArrayList<FitnessDron> estocasticoSelection(ArrayList<FitnessDron> population) {
        ArrayList<FitnessDron> selected = new ArrayList<>();
        ArrayList<Double> cumulative = new ArrayList<>();

        double sum = 0.0;
        for (FitnessDron cf : population) {
            sum += cf.getAptitude();
        }

        double cumsum = 0.0;
        for (FitnessDron cf : population) {
            cumsum += Math.max(1.0, cf.getAptitude()) / sum;
            cumulative.add(cumsum);
        }

        int n = population.size();
        double step = 1.0 / n;
        double r = rand.nextDouble() * step;

        int j = 0;
        for (int i = 0; i < n; i++) {
            double a = r + i * step;
            while (a > cumulative.get(j)) {
                j++;
            }
            selected.add(population.get(j).clone());
        }

        return selected;
    }

   private ArrayList<FitnessDron> restosSelection(ArrayList<FitnessDron> population) {
        ArrayList<FitnessDron> selected = new ArrayList<>();
        int n = population.size();
        double sum = 0.0;
        for (FitnessDron cf : population) {
            sum += cf.getAptitude();
        }

        ArrayList<Double> fractional = new ArrayList<>();

        for (FitnessDron cf : population) {
            double expected = (cf.getAptitude() / sum) * n;
            int copies = (int) Math.floor(expected);

            for (int j = 0; j < copies; j++) {
                selected.add(cf.clone());
            }

            fractional.add(expected - copies);
        }

        while (selected.size() < n) {
            double r = rand.nextDouble();
            double acc = 0.0;
            for (int i = 0; i < population.size(); i++) {
                acc += fractional.get(i);
                if (r <= acc) {
                    selected.add(population.get(i).clone());
                    break;
                }
            }
        }

        return selected;
    }

    private ArrayList<FitnessDron> rankingSelection(ArrayList<FitnessDron> population) {

        int n = population.size();
        double s = 1.5;

        population.sort((a, b) -> Double.compare(b.getAptitude(), a.getAptitude()));

        double[] probs = new double[n];


        for (int i = 0; i < n; i++) {
            probs[i] = (2 - s) / n + (2.0 * (n - i - 1) * (s - 1)) / (n * (n - 1));
        }

        double[] cumulative = new double[n];
        cumulative[0] = probs[0];

        for (int i = 1; i < n; i++) {
            cumulative[i] = cumulative[i - 1] + probs[i];
        }

        ArrayList<FitnessDron> selected = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            double r = rand.nextDouble();

            for (int j = 0; j < n; j++) {
                if (r <= cumulative[j]) {
                    selected.add(population.get(j).clone());
                    break;
                }
            }
        }

        return selected;
    }
}
