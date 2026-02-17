package logic;

import mapaApp.MapaCamaras;

public class Fitness {
    private static final int CAMARA = 7;
    private static final int CUBIERTO = 1;

    private int[][] visitado;
    private int[][] camaras;
    private boolean[] valid;
    private MapaCamaras mapa;
    private int punt;
    private boolean ponderado;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";


    public Fitness(MapaCamaras m, int[][] cams, boolean ponder){
        mapa = m;
        camaras = new int[cams.length][2];
        ponderado = ponder;
        for (int i = 0; i < cams.length; i++) {
            camaras[i][0] = cams[i][0];
            camaras[i][1] = cams[i][1];
        }
        visitado = new int[m.getFilas()][m.getCols()];
        valid = new boolean[cams.length];
        validate();
        putCamaras();
        punt = evaluar();
    }

    private void validate(){
        for (int i = 0; i < mapa.getNumCams(); i++){
            valid[i] = (inside(camaras[i][0] - 1, camaras[i][1] - 1) && !mapa.esObstaculo(camaras[i][0] - 1, camaras[i][1] - 1));
        }
    }

    private void putCamaras() {
       for(int i = 0; i < mapa.getNumCams(); i++){
        if(valid[i])
            visitado[camaras[i][0] - 1][camaras[i][1] - 1] = 7;
       }
    }

    private int evaluar(){
        int fitness = 0;
        for(int i = 0; i < mapa.getNumCams(); i++){
            Integer camx, camy;
            camx = camaras[i][0] - 1;
            camy =  camaras[i][1] - 1;
            if(!valid[i]){
                fitness -=100;
            }
            else{
                //abajo
                fitness += scanDirection(camx, camy,  0,  1); 
                //arriba
                fitness += scanDirection(camx, camy,  0, -1); 
                //derecha
                fitness += scanDirection(camx, camy,  1,  0); 
                //izquierda
                fitness += scanDirection(camx, camy, -1,  0); 
                if(ponderado)
                    fitness +=  mapa.prioridad(camx, camy);
                else    
                    fitness++;
            }
        }
        return fitness;
    }
    public void print(){
        for(int i = 0; i < mapa.getFilas(); i++){
            for(int j = 0; j < mapa.getCols(); j++){
                if(mapa.esObstaculo(i, j)){
                    System.out.print(ANSI_RED + 2 + " " + ANSI_RESET);
                }else if (visitado[i][j] == CUBIERTO){
                    System.out.print(ANSI_GREEN + visitado[i][j] + " " + ANSI_RESET);
                } else if (visitado[i][j] == CAMARA){
                    System.out.print(ANSI_BLUE + visitado[i][j] + " " + ANSI_RESET);
                }else{
                    System.out.print(visitado[i][j] + " ");
                }
            }
            System.out.println("");
        }
    }

    private int scanDirection(int x, int y, int dx, int dy) {
        int gained = 0;
        for (int d = 1; d <= mapa.getDist(); d++) {
            int nx = x + dx * d;
            int ny = y + dy * d;

            if (!inside(nx, ny)) 
                break;

            if (mapa.esObstaculo(nx, ny))
                break;
            if (visitado[nx][ny] == CAMARA) 
                break;
            if (visitado[nx][ny] == 0) {
                visitado[nx][ny] = CUBIERTO;
                if(!ponderado)
                    gained++;
                else
                    gained += mapa.prioridad(nx, ny);
            }
        }
        return gained;
    }
    private boolean inside(int x, int y){
        return ((x >= 0) && (x < mapa.getFilas()) && 
                y >= 0 && y < mapa.getCols()); 
    }

    public int getPunt(){
        return punt;
    }

    public boolean getPonder(){
        return ponderado;
    }

    public Fitness clone(){
        return new Fitness(mapa, camaras, ponderado);
    }
}
