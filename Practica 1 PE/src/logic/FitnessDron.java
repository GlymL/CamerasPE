package logic;

public class FitnessDron implements Comparable<FitnessDron>{
    private final CromosomasDron c;
    private final AEstrellaPrecalc a;
    private double fitness;


    public FitnessDron(CromosomasDron c, AEstrellaPrecalc a){
        this.c = c;
        this.a = a;
    }

    public void calculateFitness(){
        int[][] rutas = c.rutas();
        double ret = 0;
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
        }
        int min = Integer.MAX_VALUE, max = 0;
        for(int[] r : rutas){
            int iter = 0;
            int s = 0;
            while(iter < r.length && r[iter] != -1){
                s++;
                iter++;
            }
            if (s > max) {
                max = s;
            }
            if (s < min) {
                min = s;
            }
            
        }
        ret += 0.5*(max-min); 
        fitness = ret;
    }

    @Override
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
        return new FitnessDron(c, a);
    }
}
