package logic;

import java.util.Random;

public class CromosomasReal extends Cromosoma{

    private double[] cromosoma;
    private int fila = 0;
    private int col = 0;
    private final int n_cam;

    public CromosomasReal(int filas, int cols, int numCams) {
        this.fila = filas;
        this.col = cols;
        this.n_cam = numCams;
        cromosoma = new double[n_cam*3];
        randomInitialize();
    }

    private CromosomasReal(int filas, int cols, int numCams, double[] crom) {
        this.fila = filas;
        this.col = cols;
        this.n_cam = numCams;
        this.cromosoma = crom.clone();
    }

    @Override
    public void randomInitialize() {
        Random r = new Random();
        int iter = 0;
        for(int i = 0; i < n_cam; i++){
            cromosoma[iter++] = r.nextDouble(fila);
            cromosoma[iter++] = r.nextDouble(col);
            cromosoma[iter++] = r.nextDouble(360.0);
        }
    }

    @Override
    public Cromosoma[] cruceMonop(Cromosoma other, double crossRatio) {
        double[] c1 = new double[cromosoma.length];
        double[] c2 = new double[cromosoma.length];

         CromosomasReal otro = (CromosomasReal) other;

        for (int i = 0; i < cromosoma.length; i++) {
            if (Math.random() < crossRatio) {
                c1[i] = this.cromosoma[i];
                c2[i] = otro.cromosoma[i];
            } else {
                c1[i] = otro.cromosoma[i];
                c2[i] = this.cromosoma[i];
            }
        }

        return new CromosomasReal[] {
                new CromosomasReal(fila, col, n_cam, c1),
                new CromosomasReal(fila, col, n_cam, c2)
        };
    }

    public Cromosoma[] cruceBLX(Cromosoma other, double crossRatio) {
        double[] c1 = new double[cromosoma.length];
        double[] c2 = new double[cromosoma.length];

         CromosomasReal otro = (CromosomasReal) other;

        for (int i = 0; i < cromosoma.length; i++) {
            if (Math.random() < crossRatio) {
                c1[i] = this.cromosoma[i];
                c2[i] = otro.cromosoma[i];
            } else {
                c1[i] = otro.cromosoma[i];
                c2[i] = this.cromosoma[i];
            }
        }

        return new CromosomasReal[] {
                new CromosomasReal(fila, col, n_cam, c1),
                new CromosomasReal(fila, col, n_cam, c2)
        };
    }

    public Cromosoma[] cruceArit(Cromosoma other, double crossRatio) {
        if (Math.random() < crossRatio) {
            double[] c1 = new double[cromosoma.length];
            double[] c2 = new double[cromosoma.length];

            CromosomasReal otro = (CromosomasReal) other;

            for (int i = 0; i < cromosoma.length; i++) {
                c1[i] = (cromosoma[i]*0.6 + otro.cromosoma[i]*0.4);
                c2[i] = (cromosoma[i]*0.4 + otro.cromosoma[i]*0.6);
            } 
            return new CromosomasReal[] {
                new CromosomasReal(fila, col, n_cam, c1),
                new CromosomasReal(fila, col, n_cam, c2)
            };
        }

        return new CromosomasReal[]{
            (CromosomasReal)this.clone(), 
            (CromosomasReal)other.clone()
        };
    }

    @Override
    public void mutarCromosoma(Double mutationRate) {
        Random r = new Random();
        for(int i = 0; i < cromosoma.length; i++){
            if(r.nextDouble() < mutationRate){
                if(i%3 == 0){
                    cromosoma[i] = r.nextDouble(fila);
                } else if (i%3 == 1){
                    cromosoma[i] = r.nextDouble(col);
                } else{
                    cromosoma[i] = r.nextDouble(360.0);
                }
            }
        }
    }

    public void mutarGauss(Double mutationRate){
        Random r = new Random();
        for(int i = 0; i < cromosoma.length; i++){
            if(r.nextDouble() < mutationRate){
                cromosoma[i] += r.nextGaussian();
            }
        }
    }

    @Override
    public Cromosoma[] cruceUnif(Cromosoma other) {
        int cut = 1 + (int) (Math.random() * (cromosoma.length - 1));

        CromosomasReal otro = (CromosomasReal) other;

        double[] c1 = new double[cromosoma.length];
        double[] c2 = new double[cromosoma.length];

        for (int i = 0; i < cromosoma.length; i++) {
            if (i < cut) {
                c1[i] = this.cromosoma[i];
                c2[i] = otro.cromosoma[i];
            } else {
                c1[i] = otro.cromosoma[i];
                c2[i] = this.cromosoma[i];
            }
        }

        return new CromosomasReal[] {
                new CromosomasReal(fila, col, n_cam, c1),
                new CromosomasReal(fila, col, n_cam, c2)
        };
    }

    @Override
    public Cromosoma clone() {
        return new CromosomasReal(fila, col, n_cam, cromosoma);
    }

    @Override
    public double[][] decode() {
        int iter = 0;
        double[][]ret = new double[cromosoma.length/3][3];
        for(int i = 0; i < cromosoma.length; i++){
            ret[iter][i%3] = cromosoma[i];
            if(i%3 == 2)
                iter++;
        }
        return ret;
    }

    public static void main(String[] args){
        CromosomasReal c =  new CromosomasReal(10, 10, 4);
        CromosomasReal c2 =  new CromosomasReal(10, 10, 4);
        c.mutarCromosoma(.8);
        c2.clone();
        double[][] d = c.decode();
    }
}
