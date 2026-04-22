package mapaApp;

import java.util.Random;


public class GeneradorMapa {
    
    // private final Pair[] listaCamaras;
    private final int[][] mapa;
    private final MapaRover mc;
    private final int base_x;
    private final int base_y;
    private final int filas;
    private final int cols;
    private final int seed;
    // private static final int PENALIZACION = 500;

    public GeneradorMapa(int seed, MapaRover mc){
        this.mc = mc;
        this.seed = seed;
        base_x = base_y = 1;
        
         mapa = new int[mc.getCols()][mc.getFilas()];
            Random rand = new Random(seed); 
            filas = mc.getFilas(); cols = mc.getCols();
            
            // 1  muro     2  muestra   3  arena
            for (int i = 0; i < mc.getFilas(); i++) {
                for (int j = 0; j < mc.getCols(); j++) {
                    if (i == 0 || i == mc.getFilas() - 1 || j == 0 || j == mc.getCols() - 1) mapa[i][j] = 1;
                    else  if (rand.nextDouble() < 0.15 && (i != 1 || j != 1)) mapa[i][j] = 1;
                    else  if (rand.nextDouble() < 0.15 && (i != 1 || j != 1)) mapa[i][j] = 2; 
                    else  if (rand.nextDouble() < 0.08 && (i != 1 || j != 1)) mapa[i][j] = 3; 
                }
            }
            // visitado = new boolean[mc.getFilas()][mc.getCols()];
            // visitado[base_y][base_x] = true;
    }

    public int[][] getMapa(){
        return mapa.clone();
    }

    // public Pair[] getCameras(){
    //     return listaCamaras.clone();
    // }

    public int getBaseX() {
        return base_x;
    }

    public int getBaseY() {
        return base_y;
    }

    public int getFilas() {
        return filas;
    }

    public int getCols() {
        return cols;
    }

    public int getSeed() {
        return seed;
    }

    public MapaRover getMapaRover() {
        return mc;
    }
}
