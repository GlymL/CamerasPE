package mapaApp;

import java.util.Random;


public class GeneradorMapa {
    
    // private final Pair[] listaCamaras;
    private final int[][] mapa;
    private final boolean[][] visitado;
    private final int base_x;
    private final int base_y;
    private static final int PENALIZACION = 500;

    public GeneradorMapa(int seed, MapaRover mc){
        
         mapa = new int[mc.getCols()][mc.getFilas()];
            Random rand = new Random(seed); 
            
            // 1  muro     2  muestra   3  arena
            for (int i = 0; i < mc.getFilas(); i++) {
                for (int j = 0; j < mc.getCols(); j++) {
                    if (i == 0 || i == mc.getFilas() - 1 || j == 0 || j == mc.getCols() - 1) mapa[i][j] = 1;
                    else  if (rand.nextDouble() < 0.15 && (i != 1 || j != 1)) mapa[i][j] = 1;
                    else  if (rand.nextDouble() < 0.15 && (i != 1 || j != 1)) mapa[i][j] = 2; 
                    else  if (rand.nextDouble() < 0.08 && (i != 1 || j != 1)) mapa[i][j] = 3; 
                }
            }
            visitado = new boolean[mc.getFilas()][mc.getCols()];
            visitado[base_y][base_x] = true;
    }

    public int[][] getMapa(){
        return mapa.clone();
    }

    // public Pair[] getCameras(){
    //     return listaCamaras.clone();
    // }

    public int getBaseX() {
        return base;
    }

    public int getBaseX() {
        return base;
    }
}
