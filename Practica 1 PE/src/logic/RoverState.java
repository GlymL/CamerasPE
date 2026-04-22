package logic;

import mapaApp.GeneradorMapa;

public class RoverState {
    //TO-DO: CAMBIAR LAS DISRANCIAS PARA QUE CONCUERDEN CON LAS TRANSPARENCIAS (no me acuerdo si esta hecho bien y revisado)
    //TO-DO: Implementar patologias evolutivas

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
    private boolean accion_tomada;
    private Direction direction;

    //TO-DO: meter las busquedas lineales de arenas, muestras y obstaculos
    public RoverState(GeneradorMapa m) {
        mapa = m;
        energia = 100;

        visitado = new boolean[m.getMapa().length][m.getMapa()[0].length];
        visitado[m.getBaseX()][m.getBaseY()] = true;
        pos = new int[]{m.getBaseX(), m.getBaseY()};
        direction = m.getMapaRover().getDirection();
    }

    private boolean onLimits(int pos_x, int pos_y) {
        return pos_x >= 0 && pos_y >= 0 && pos_x < mapa.getFilas() && pos_y < mapa.getCols();
    }

    public int getDistMuestra() {
        int[][] direcciones = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        int res = 100;

        for (int i = 0; i < 4; i++) {
            int dx = direcciones[i][0], dy = direcciones[i][1];
            
            int pos_x = pos[1] + dx;
            int pos_y = pos[0] + dy;

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
            
            int pos_x = pos[1] + dx;
            int pos_y = pos[0] + dy;

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
            
            int pos_x = pos[1] + dx;
            int pos_y = pos[0] + dy;

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

    public void incrMuestras() {
        muestras_obtenidas++;
    }

    public int getMuestras() {
        return muestras_obtenidas;
    }

    public void incrCeldas() {
        celdas_exploradas++;
    }

    public int getCeldas() {
        return celdas_exploradas;
    }

    public void incrTicks() {
        ticks++;
    }

    public int getTicks() {
        return ticks;
    }

    public boolean isFinished() {
        return ticks >= 150;
    }

    public void incrVisualRewarding() {
        visual_rewarding++;
    }

    public int getVisualRewarding() {
        return visual_rewarding;
    }

    public void incrArenas() {
        arena_pisada++;
    }

    public int getArenas() {
        return arena_pisada;
    }

    public void incrColisiones() {
        colisiones++;
    }

    public int getColisiones() {
        return colisiones;
    }

    public boolean isAlive() {
        return energia > 0;
    }

    public void setAccionTomada(boolean val) {
        accion_tomada = val;
    }

    public boolean getAccionTomada() {
        return accion_tomada;
    }

    public MovementInfo executeAction(Action a) {
        int destiny_x = pos[1], destiny_y = pos[0];
        int energia_gastada;

        if (a == Action.AVANZAR) {
            energia_gastada = 1;
            switch (direction) {
                case EAST:
                    destiny_x += 1;
                    break;
                case WEST:
                    destiny_x -= 1;
                    break;
                case SOUTH:
                    destiny_y += 1;
                    break;
                case NORTH:
                    destiny_y -= 1;
                    break;
                default:
                    break;
            }

            if (!onLimits(destiny_x, destiny_y)) {
                energia_gastada = 100 - energia;
            }

            if (mapa.getMapa()[destiny_y][destiny_x] == 1) {
                energia_gastada = 2;
            }
            else if (mapa.getMapa()[destiny_y][destiny_x] == 2) {
                energia_gastada = 10;
                visitado[destiny_y][destiny_x] = true;
            }
            else {
                muestras_obtenidas++;
                visitado[destiny_y][destiny_x] = true;
                mapa.getMapa()[destiny_y][destiny_x] = 0;
            }
        }
        else if (a == Action.GIRAR_DER) {
            direction = direction.rotateRight();
            energia_gastada = 1;
        }
        else {
            direction = direction.rotateLeft();
            energia_gastada = 1;
        }

        return new MovementInfo(energia_gastada, true);
    }
}
