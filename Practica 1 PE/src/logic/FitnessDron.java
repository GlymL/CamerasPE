package logic;

import java.util.Random;

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

    public void calculateFitness(){
        int[][] rutas = c.rutas();
        double ret = 0;
        double minSpd = Double.MAX_VALUE;
        for(int i = 0; i < rutas.length; i++){
            double coste = 0;
            int j = 0;
            while(j < rutas[i].length && rutas[i][j] != -1){
                if(j == 0){
                    coste += a.getInit(rutas[i][j]);
                }
                else
                    coste += a.getPrecalc(rutas[i][j-1], rutas[i][j]);
                j++;
            }
            if(rutas[i][0] != -1)
                coste += a.getInit(rutas[i][Math.max(0,  j - 1)]);
            ret = Math.max(ret, coste/EnumFlota.values()[i].getVel());
            minSpd =  Math.min(minSpd, coste/EnumFlota.values()[i].getVel());

        }
        System.out.println(0.5*(ret-minSpd));
        ret += 0.5*(ret-minSpd); 
        fitness = ret;
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
}
