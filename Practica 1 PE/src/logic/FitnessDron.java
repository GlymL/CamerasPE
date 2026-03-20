package logic;

public class FitnessDron implements Comparable<FitnessDron>{
    private final CromosomasDron c;
    private final AEstrellaPrecalc a;
    private double fitness;
    private double aptitude;


    public FitnessDron(CromosomasDron c, AEstrellaPrecalc a){
        this.c = c;
        this.a = a;
    }

    public FitnessDron(CromosomasDron c, AEstrellaPrecalc a, double fitness, double aptitude) {
        this.c = c;
        this.a = a;
        this.fitness = fitness;
        this.aptitude = aptitude;
    }

    public void calculateFitness() {
        int[][] rutas = c.rutas();
        double makespan = 0;
        double minTime = Double.MAX_VALUE;

        for (int i = 0; i < rutas.length; i++) {
            int[] ruta = rutas[i];

            if (ruta[0] == -1) continue;

            double tiempo = 0;
            int j = 0;

            while (j < ruta.length && ruta[j] != -1) {
                if (j == 0) {
                    tiempo += a.getInit(ruta[j]);
                } else {
                    tiempo += a.getPrecalc(ruta[j - 1], ruta[j]);
                }
                j++;
            }

            tiempo += a.getInit(ruta[j - 1]);  
            double timeWithSpeed = tiempo / EnumFlota.values()[i].getVel();

            makespan = Math.max(makespan, timeWithSpeed);
            minTime = Math.min(minTime, timeWithSpeed);
        }

        fitness = makespan;

        //fitness += 0.5 * (makespan - minTime);
    }

    @Override
    public int compareTo(FitnessDron a2) {
        return Double.compare(a2.aptitude, aptitude);
    }

    public double getFitness() {
        return fitness;
    }

    public CromosomasDron getCrom(){
        return c.clone();
    }

    public FitnessDron clone(){
        return new FitnessDron(c, a, fitness, aptitude);
    }
    public void setAptitude(double apt){
        this.aptitude = apt;
    }

    public double getAptitude(){
        return aptitude;
    }

    public AEstrellaPrecalc getPrecalc() {
        return a;
    }
}
