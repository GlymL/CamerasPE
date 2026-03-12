package logic;


import logic.AEstrella.Pair;
import mapaApp.GeneradorCamaras;
import mapaApp.MapaCamaras;

public class FitnessDron implements Comparable<FitnessDron>{
    private AEstrella a;
    private CromosomasDron c;
    private GeneradorCamaras gc;
    private double fitness;


    public FitnessDron (CromosomasDron c, GeneradorCamaras gc){

        this.c = c;
        this.gc = gc;
        this.a = new AEstrella(gc.getMapa());
    }

    private FitnessDron(CromosomasDron c, GeneradorCamaras gc, AEstrella a){

        this.c = c;
        this.gc = gc;
        this.a = a;
    }

    public void calculateFitness(){
        int[][] rutas = c.rutas();
        Pair[] camPos = gc.getCameras();
        double ret = 0;
        for(int i = 0; i < rutas.length; i++){
            double coste = 0;
            int j = 0;
            while(j < rutas[i].length && rutas[i][j] != -1){
                if(j == 0){
                    coste += a.aStarSearch(new Pair(1, 1), camPos[rutas[i][j]]);
                }
                else
                    coste += a.aStarSearch(camPos[rutas[i][j-1]], camPos[rutas[i][j]]);
                j++;
            }
            ret = Math.max(ret, coste/EnumFlota.values()[i].getVel());
        }
        System.out.println(ret);
        fitness = ret;

    }

    public static void main(String [] args){

        FitnessDron f = new FitnessDron(new CromosomasDron(40, 3), 
        new GeneradorCamaras(3000, new MapaCamaras(3)));

        f.calculateFitness();
    }

    public int compareTo(FitnessDron a2) {
        return Double.compare(a2.fitness, fitness);
    }

    public double getFitness() {
        return fitness;
    }

    public CromosomasDron getCrom(){
        return c.clone();
    }

    public FitnessDron clone(){
        return new FitnessDron(c, gc, a);

    }
}
