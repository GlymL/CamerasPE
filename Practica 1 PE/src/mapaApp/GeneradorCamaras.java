package mapaApp;

import java.util.Random;

import logic.AEstrella.Pair;


public class GeneradorCamaras {
    
    private Pair[] listaCamaras;
    private int[][] mapa;
    private static final int PENALIZACION = 500;

    public GeneradorCamaras(int seed, MapaCamaras mc){
        // Inicializar el generador de números aleatorios CON LA SEMILLA
        // Esto es la clave: al usar la misma semilla, la secuencia de números
        // aleatorios será idéntica en el ordenador de cualquier alumno.
        mapa = mc.getMapa().clone();
        Random rand = new Random(seed);
        int numCamaras = mc.getNumCams();
        listaCamaras = new Pair[numCamaras];
        //La semilla se selecciona de la interfaz
        
        // Lista para guardar las posiciones (asumiendo una clase simple Punto(x,y))
        int iter = 0;
        while (iter < numCamaras) {
            int x = rand.nextInt(mc.getFilas());
            int y = rand.nextInt(mc.getCols());
            if(mapa[x][y] != 0 && mapa[x][y] < PENALIZACION){
                mapa[x][y] += PENALIZACION;
                listaCamaras[iter] = new Pair(x, y);
                iter++;
            }

        }
    }

    public int[][] getMapa(){
        return mapa.clone();
    }

    public Pair[] getCameras(){
        return listaCamaras.clone();
    }

}
