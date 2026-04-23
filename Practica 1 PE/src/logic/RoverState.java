package logic;

import mapaApp.GeneradorMapa;

public class RoverState {
    //TO-DO: CAMBIAR LAS DISRANCIAS PARA QUE CONCUERDEN CON LAS TRANSPARENCIAS (no me acuerdo si esta hecho bien y revisado)
    //distancia deben ser de rango de visión. //HECHO
    //TO-DO: Implementar patologias evolutivas (Mareo HECHO; Pereza)
    //TO-DO: Ajustar energía (puede  ser negativa) //HECHO


    private GeneradorMapa mapa;
    private int[][] celdas;

    private int energia;

    private int muestras_obtenidas;
    private int celdas_exploradas;
    private int giros_consecutivos;
    private int arena_pisada;
    private int colisiones;
    private int visual_rewarding;

    private int ticks;

    private int[] pos;
    private boolean[][] visitado;
    private Direction direction;

    private boolean accion_tomada;

    //TO-DO: meter las busquedas lineales de arenas, muestras y obstaculos (HECHO)
    public RoverState(GeneradorMapa m) {
        mapa = m;
        celdas = m.getMapa().clone();
        energia = 100;

        giros_consecutivos = 0;
        arena_pisada = 0;
        celdas_exploradas = 0;
        muestras_obtenidas = 0;
        colisiones = 0;
        visual_rewarding = 0;

        ticks = 0;

        visitado = new boolean[m.getMapa().length][m.getMapa()[0].length];
        visitado[m.getBaseX()][m.getBaseY()] = true;
        pos = new int[]{m.getBaseX(), m.getBaseY()};
        direction = m.getMapaRover().getDirection();

        accion_tomada = false;
    }

    private boolean onLimits(int pos_x, int pos_y) {
        return pos_x >= 0 && pos_y >= 0 && pos_x < mapa.getFilas() && pos_y < mapa.getCols();
    }

    public int getDistMuestra() {
        int res = 100;
        int pos_x = pos[1] + direction.x, pos_y = pos[0] + direction.y;
        int dist = 0;

        while (onLimits(pos_x, pos_y)) {
            //Muro
            if (celdas[pos_y][pos_x] == 1) {
                break;
            }
            //Muestra
            else if (celdas[pos_y][pos_x] == 2) {
                res = dist;
                break;
            }
            dist++;
            pos_x += direction.x;
            pos_y += direction.y;
        }

        return res;
    }

    public int getDistArena() {
        //int[][] direcciones = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        int res = 100;

        // for (int i = 0; i < 4; i++) {
        //     int dx = direcciones[i][0], dy = direcciones[i][1];
            
        //     int pos_x = pos[1] + dx;
        //     int pos_y = pos[0] + dy;

        //     int dist = 0;
        //     while (onLimits(pos_x, pos_y)) {
        //         dist++;

        //         //Obstaculo
        //         if (celdas[pos_x][pos_y] == 1) {
        //             break;
        //         }
        //         //Arena
        //         else if (celdas[pos_x][pos_y] == 3) {
        //             res = Math.min(res, dist);
        //             break;
        //         }

        //         pos_x += dx; pos_y += dy;
        //     }
        // }

        int pos_x = pos[1] + direction.x, pos_y = pos[0] + direction.y;
        int dist = 0;

        while (onLimits(pos_x, pos_y)) {
            //Muro
            if (celdas[pos_y][pos_x] == 1) {
                break;
            }
            //Arena
            else if (celdas[pos_y][pos_x] == 3) {
                res = dist;
                break;
            }
            dist++;
            pos_x += direction.x;
            pos_y += direction.y;
        }

        return res;
    }

    public int getDistObstaculo() {
        int res = 100;
        int pos_x = pos[1] + direction.x, pos_y = pos[0] + direction.y;
        int dist = 0;

        while (onLimits(pos_x, pos_y)) {
            //Muro
            if (celdas[pos_y][pos_x] == 1) {
                res = dist;
                break;
            }

            dist++;
            pos_x += direction.x;
            pos_y += direction.y;
        }

        return res;
    }
    
    public int getNivelEnergia() {
        return energia;
    }

    public void lowerEnergia(int amount) {
        energia -= amount;
    }

    // public void resetEnergia() {
    //     energia = 100;
    // }

    public int getMuestras() {
        return muestras_obtenidas;
    }

    public int getCeldasExploradas() {
        return celdas_exploradas;
    }

    public void incrTicks() {
        ticks++;
    }

    public int getTicks() {
        return ticks;
    }

    public boolean isFinished() {
        return energia <= 0 && ticks >= 150;
    }

    public int getVisualRewarding() {
        return visual_rewarding;
    }

    public int getArenas() {
        return arena_pisada;
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

    private void applyMareo() {
        giros_consecutivos++;

        if (giros_consecutivos >= 4) {
            energia -= 20;
            giros_consecutivos = 0;
        }
    }

    public int applyPereza() {
        if (celdas_exploradas < 4)
            return 1000;
        else
            return 0;
    }

    public MovementInfo executeAction(Action a) {
        int destiny_x = pos[1], destiny_y = pos[0];
        int energia_gastada;

        if (a == Action.AVANZAR) {
            energia_gastada = 1;
            giros_consecutivos = 0;
            // switch (direction) {
            //     case EAST:
            //         destiny_x += 1;
            //         break;
            //     case WEST:
            //         destiny_x -= 1;
            //         break;
            //     case SOUTH:
            //         destiny_y += 1;
            //         break;
            //     case NORTH:
            //         destiny_y -= 1;
            //         break;
            //     default:
            //         break;
            // }

            destiny_x += direction.x;
            destiny_y += direction.y;

            if (!onLimits(destiny_x, destiny_y)) {
                energia_gastada = 100 - energia;
            }

            if (celdas[destiny_y][destiny_x] == 1) {
                energia_gastada = 2;
                colisiones++;
            }
            else if (celdas[destiny_y][destiny_x] == 2) {
                energia_gastada = 10;
                arena_pisada++;

                if (!visitado[destiny_y][destiny_x]) {
                    celdas_exploradas++;
                    visitado[destiny_y][destiny_x] = true;
                }

                pos[1] = destiny_x;
                pos[0] = destiny_y;
            }
            else {
                muestras_obtenidas++;
                celdas[destiny_y][destiny_x] = 0;

                if (!visitado[destiny_y][destiny_x]) {
                    celdas_exploradas++;
                    visitado[destiny_y][destiny_x] = true;
                }

                pos[1] = destiny_x;
                pos[0] = destiny_y;
            }
        }
        else if (a == Action.GIRAR_DER) {
            direction = direction.rotateRight();
            energia_gastada = 1;

            applyMareo();
        }
        else {
            direction = direction.rotateLeft();
            energia_gastada = 1;

            applyMareo();
        }

        //Aplicamos el visual rewarding
        if (getDistMuestra() != 100) {
            visual_rewarding++;
        }

        incrTicks();

        return new MovementInfo(energia_gastada, true);
    }
}
