package mapaApp;

import java.util.Random;
import logic.AEstrella.Pair;


public class GeneradorMapa {
    
    private final Pair[] listaCamaras;
    private final int[][] mapa;
    private final Pair base;
    private static final int PENALIZACION = 500;

    public GeneradorMapa(int seed, MapaRover mc){
        // Inicializar el generador de números aleatorios CON LA SEMILLA
        // Esto es la clave: al usar la misma semilla, la secuencia de números
        // aleatorios será idéntica en el ordenador de cualquier alumno.
        mapa = mc.getMapa().clone();
        Random rand = new Random(seed);
        int numCamaras = -1;
        listaCamaras = new Pair[numCamaras];
        base = mc.getBase();
        //La semilla se selecciona de la interfaz
        
        // Lista para guardar las posiciones (asumiendo una clase simple Punto(x,y))
        int iter = 0;
        while (iter < numCamaras) {
            int y = rand.nextInt(mc.getCols());
            int x = rand.nextInt(mc.getFilas());
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

    public Pair getBase() {
        return base;
    }

}
