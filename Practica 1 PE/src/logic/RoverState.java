package logic;

import mapaApp.GeneradorMapa;

public class RoverState {
    //TO-DO: CAMBIAR LAS DISRANCIAS PARA QUE CONCUERDEN CON LAS TRANSPARENCIAS
    private GeneradorMapa mapa;

    private int energia;

    private int muestras_obtenidas;
    private int celdas_exploradas;
    private int ticks;
    private int visual_rewarding;
    private int arena_pisada;
    private int colisiones;
    private int[] pos;
    private boolean[][] visitado;

    //TO-DO: meter las busquedas lineales de arenas, muestras y obstaculos
    public RoverState(GeneradorMapa m) {
        mapa = m;
        energia = 100;

        visitado = new boolean[m.getMapa().length][m.getMapa()[0].length];
        visitado[m.getBaseX()][m.getBaseY()] = true;
        pos = new int[]{m.getBaseX(), m.getBaseY()};
    }

    private boolean onLimits(int pos_x, int pos_y) {
        return pos_x >= 0 && pos_y >= 0 && pos_x < mapa.getFilas() && pos_y < mapa.getCols();
    }

    public int getDistMuestra() {
        int[][] direcciones = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        int res = 100;

        for (int i = 0; i < 4; i++) {
            int dx = direcciones[i][0], dy = direcciones[i][1];
            
            int pos_x = pos[0] + dx;
            int pos_y = pos[1] + dy;

            int dist = 0;
            while (onLimits(pos_x, pos_y)) {
                dist++;

                //Obstaculo
                if (mapa.getMapa()[pos_x][pos_y] == 1) {
                    break;
                }
                //Muestra
                else if (mapa.getMapa()[pos_x][pos_y] == 2) {
                    res = Math.min(res, dist);
                    break;
                }

                pos_x += dx; pos_y += dy;
            }
        }
        return res;
    }

    public int getDistArena() {
        int[][] direcciones = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        int res = 100;

        for (int i = 0; i < 4; i++) {
            int dx = direcciones[i][0], dy = direcciones[i][1];
            
            int pos_x = pos[0] + dx;
            int pos_y = pos[1] + dy;

            int dist = 0;
            while (onLimits(pos_x, pos_y)) {
                dist++;

                //Obstaculo
                if (mapa.getMapa()[pos_x][pos_y] == 1) {
                    break;
                }
                //Arena
                else if (mapa.getMapa()[pos_x][pos_y] == 3) {
                    res = Math.min(res, dist);
                    break;
                }

                pos_x += dx; pos_y += dy;
            }
        }
        return res;
    }

    public int getDistObstaculo() {
        int[][] direcciones = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        int res = 100;

        for (int i = 0; i < 4; i++) {
            int dx = direcciones[i][0], dy = direcciones[i][1];
            
            int pos_x = pos[0] + dx;
            int pos_y = pos[1] + dy;

            int dist = 0;
            while (onLimits(pos_x, pos_y)) {
                dist++;

                //Obstaculo
                if (mapa.getMapa()[pos_x][pos_y] == 1) {
                    res = Math.min(res, dist);
                    break;
                }

                pos_x += dx; pos_y += dy;
            }
        }
        return res;
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
