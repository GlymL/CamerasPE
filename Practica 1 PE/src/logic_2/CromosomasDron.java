package logic_2;

import java.util.Arrays;
import java.util.Collections;

public class CromosomasDron{

    private int[] cromosoma;
    private int drones;
    private int camaras;

    public CromosomasDron(int camaras, int drones){
        this.drones = drones;
        this.camaras = camaras;
        cromosoma = new int[camaras+(drones-1)];
    }

    private CromosomasDron(int camaras, int drones, int[] cromosoma){
        this.drones = drones;
        this.camaras = camaras;
        cromosoma = cromosoma.clone();
    }

    public void randomInitialize() {
        for(int i = 0; i < cromosoma.length; i++){
            cromosoma[i] = i;
        }
       Collections.shuffle(Arrays.asList(cromosoma));
    }


    
    
    public CromosomasDron clone() {
       return new CromosomasDron(camaras, drones, cromosoma);
    }


}
