package logic;

public class Fitness implements Comparable<Fitness>{
    private final CromosomasRanger c;
    // private final TreeGenerator tg;
    private double fitness;
    private double aptitude;


    public Fitness(CromosomasRanger c){
        this.c = c;
    }

    // public Fitness(CromosomasRanger c, double fitness, double aptitude) {
    //     this.c = c;
    //     this.fitness = fitness;
    //     this.aptitude = aptitude;
    // }

    public void calculateFitness(RoverState state) {
        //Aplicar penalizacion de giro cada 4 ciclos, no a partir de los 4 ciclos.
        //Ejemplo. Penalizamos el giro 4, el 8, el 12, pero no el 5, el 6, el 11...

    }

    @Override
    public int compareTo(Fitness a2) {
        return Double.compare(a2.aptitude, aptitude);
    }

    public double getFitness() {
        return fitness;
    }

    public CromosomasRanger getCrom(){
        return c.clone();
    }

    public Fitness clone(){
        return new Fitness(c);
    }
    public void setAptitude(double apt){
        this.aptitude = apt;
    }

    public double getAptitude(){
        return aptitude;
    }
}
