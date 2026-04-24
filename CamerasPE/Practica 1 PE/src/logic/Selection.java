package logic;

import java.util.ArrayList;
import java.util.Random;

public class Selection {
    
    private final EnumSelection es;
    private final Random rand = new Random();

    public Selection(EnumSelection es){
        this.es = es;
    }

    public ArrayList<Fitness> select(ArrayList<Fitness> population){
        
        ArrayList<Fitness> ret;

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

    
    private ArrayList<Fitness> rouletteSelection(ArrayList<Fitness> population) {
        ArrayList<Fitness> selected = new ArrayList<>();
        ArrayList<Double> cumulative = new ArrayList<>();
        double sum = 0;

        for (Fitness cf : population) {
            sum += cf.getAptitude();
            cumulative.add(sum);
        }

        if (sum == 0) {
            for (Fitness cf : population) {
                selected.add(population.get(rand.nextInt(population.size())).clone());
            }
            return selected;
        }

        for (Fitness cf : population) {
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


    private ArrayList<Fitness> truncSelection(ArrayList<Fitness> population) {
        ArrayList<Fitness> selected = new ArrayList<>();
        while(selected.size() < population.size()){
            int i = 0;
            while(selected.size() < population.size() && i < population.size()/10){
                    selected.add(population.get(i).clone());
                    i++;
                }
            }

        //int cutoff = Math.max(1, population.size() / 10);
        // while (selected.size() < population.size()) {
        //     for (int i = 0; i < cutoff && selected.size() < population.size(); i++) {
        //         selected.add(population.get(i).clone());
        //     }
        // }
        return selected;
    }

    private ArrayList<Fitness> torneoSelection(ArrayList<Fitness> population) {
        ArrayList<Fitness> selected = new ArrayList<>();
        for (Fitness cf : population) {
            ArrayList<Fitness> torneo = new ArrayList<>();
            Fitness c1 = population.get(rand.nextInt(population.size()));
            torneo.add(c1);
            Fitness c2 = population.get(rand.nextInt(population.size()));
            torneo.add(c2);
            Fitness c3 = population.get(rand.nextInt(population.size()));
            torneo.add(c3);
            torneo.sort((a, b) -> a.compareTo(b));
            selected.add(torneo.get(0).clone());
        }
        return selected;
    }

    private ArrayList<Fitness> estocasticoSelection(ArrayList<Fitness> population) {
        ArrayList<Fitness> selected = new ArrayList<>();
        ArrayList<Double> cumulative = new ArrayList<>();

        double sum = 0.0;
        for (Fitness cf : population) {
            sum += cf.getAptitude();
        }

        double cumsum = 0.0;
        for (Fitness cf : population) {
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

   private ArrayList<Fitness> restosSelection(ArrayList<Fitness> population) {
        ArrayList<Fitness> selected = new ArrayList<>();
        int n = population.size();
        double sum = 0.0;
        for (Fitness cf : population) {
            sum += cf.getAptitude();
        }

        ArrayList<Double> fractional = new ArrayList<>();

        for (Fitness cf : population) {
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

    private ArrayList<Fitness> rankingSelection(ArrayList<Fitness> population) {

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

        ArrayList<Fitness> selected = new ArrayList<>();

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
