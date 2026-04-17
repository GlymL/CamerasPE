package logic;

public class Fitness implements Comparable<Fitness>{
    private final CromosomasRanger c;
    // private final TreeGenerator tg;
    private double fitness;
    private double aptitude;


    public Fitness(CromosomasRanger c/*, TreeGenerator tg*/){
        this.c = c;
        // this.tg = tg;
    }

    public Fitness(CromosomasRanger c, double fitness, double aptitude) {
        this.c = c;
        this.fitness = fitness;
        this.aptitude = aptitude;
    }

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

    // // 2-Opt

    // public Fitness untangle(){
    //     int[][] rutas = c.rutas();
    //     int[][] rutasRet = new int[rutas.length][rutas[0].length];
    //     Integer[] ret = new Integer[c.getLength()];
    //     int cams = c.getCamaras();
    //     int drones = c.getDrones();
    //     for(int i = 0; i < rutas.length; i++){
    //         rutasRet[i] = twoOpt(rutas[i]);
    //     }
    //     int iter = 0;
    //     for(int i = 0; i < rutasRet.length; i++){
    //         int interiter = 0;
    //         Integer next = rutasRet[i][interiter++];
    //         while(next != -1){
    //             ret[iter++] = next;
    //             next = rutasRet[i][interiter++];
    //         }
    //         if(i + 1 != rutasRet.length)
    //             ret[iter++] = ret.length - i - 1;
    //     }

    //     return new Fitness(new CromosomasRanger(cams, drones, ret), a);
    // }

    // public int[] twoOpt(int[] route) {
    //     int n = route.length;
    //     boolean improved = true;

    //     while (improved) {
    //         improved = false;

    //         for (int i = 1; i < n - 2; i++) {
    //             for (int k = i + 1; k < n - 1; k++) {
    //                 if(route[i] == -1 || route[k] == -1 || route[k + 1] == -1) continue;
    //                 double before =
    //                         a.getPrecalc(route[i - 1], route[i]) +
    //                         a.getPrecalc(route[k], route[k + 1]);

    //                 double after =
    //                         a.getPrecalc(route[i - 1], route[k]) +
    //                         a.getPrecalc(route[i], route[k + 1]);

    //                 if (after < before) {
    //                     route = twoOptSwap(route, i, k);
    //                     improved = true;
    //                 }
    //             }
    //         }
    //     }

    //     return route;
    // }

    // private static int[] twoOptSwap(int[] route, int i, int k) {
    //     int[] newRoute = new int[route.length];

    //     System.arraycopy(route, 0, newRoute, 0, i);

    //     for (int c = i; c <= k; c++) {
    //         newRoute[c] = route[k - (c - i)];
    //     }

    //     System.arraycopy(route, k + 1, newRoute, k + 1, route.length - k - 1);

    //     return newRoute;
    // }

}
