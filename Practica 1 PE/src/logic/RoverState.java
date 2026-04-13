package logic;

import mapaApp.GeneradorMapa;

public class RoverState {
    private GeneradorMapa mapa;

    int energia;

    //TO-DO: meter las busquedas lineales de arenas, muestras y obstaculos
    public RoverState(GeneradorMapa m) {
        mapa = m;
        energia = 100;
    }

    public int getDistMuestra() {
        return 0;
    }

    public int getDistArena() {
        return 0;
    }

    public int getDistObstaculo() {
        return 0;
    }
    
    public int getNivelEnergia() {
        return energia;
    }

    public boolean lowerEnergia(int amount) {
        if (energia >= amount) {
            energia -= amount;
            return true;
        } else {
            return false;
        }
    }

    public void resetEnergia() {
        energia = 100;
    }
}
