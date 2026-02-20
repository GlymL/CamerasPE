package logic;

import mapaApp.MapaCamaras;

public class FitnessReal extends Fitness{
    private static final int CAMARA = 7;
    private static final int CUBIERTO = 1;

    private int[][] visitado;
    private double[][] camaras;
    private boolean[] valid;
    private MapaCamaras mapa;
    private int punt;
    private boolean ponderado;
    private Cromosoma c;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";


    public FitnessReal(MapaCamaras m, Cromosoma c, boolean ponder){
        mapa = m;
        this.c = c;
        double[][] cams = c.decode();
        camaras = new double[cams.length][3];
        ponderado = ponder;
        for (int i = 0; i < cams.length; i++) {
            camaras[i][0] = cams[i][0];
            camaras[i][1] = cams[i][1];
            camaras[i][2] = cams[i][2];
        }
        visitado = new int[m.getFilas()][m.getCols()];
        valid = new boolean[cams.length];
        validate();
        putCamaras();
        punt = evaluar();
    }

    private void validate(){
        for (int i = 0; i < mapa.getNumCams(); i++){
            valid[i] = (inside((int)Math.floor(camaras[i][0]), (int)Math.floor(camaras[i][1])) && 
            !mapa.esObstaculo((int)Math.floor(camaras[i][0]), (int)Math.floor(camaras[i][1])));
        }
    }

    private void putCamaras() {
       for(int i = 0; i < mapa.getNumCams(); i++){
        if(valid[i])
            visitado[(int)Math.floor(camaras[i][0])][(int)Math.floor(camaras[i][1])] = 7;
       }
    }

    private int evaluar(){
        int fitness = 0;
        for(int i = 0; i < mapa.getNumCams(); i++){
            double camx, camy;
            camx = camaras[i][0];
            camy = camaras[i][1];
            if(!valid[i]){
                fitness -=100;
            }
            else{

                double dirRad = Math.toRadians(camaras[i][2]);
                double dirY = Math.cos(dirRad);
                double dirX = Math.sin(dirRad);
                double[][] posible = cone(camaras[i][0] - 1, camaras[i][1] - 1, dirX,
                    dirY, mapa.getAngulo());
                for(int j = 0; j < posible.length - 1; j++){
                    double posx = (posible[j][0]);
                    double posy = (posible[j][1]);
                    int posTileX = (int)Math.floor(posible[j][0]);
                    int posTileY = (int)Math.floor(posible[j][1]);
                    if(posible[j][0] == -1)
                        break;
                    if(lineaLibre(camx, camy,  posx + 0.5,  posy + 0.5) && visitado[posTileX][posTileY] == 0){
                        if (ponderado)
                            fitness += mapa.prioridad(posTileX, posTileY);
                        else
                            fitness++;
                        visitado[posTileX][posTileY] = CUBIERTO;
                    }
                }
            }
        }
        return fitness;
    }

    public int[][] print(){
        int[][] ret = new int[mapa.getFilas()][mapa.getCols()];
        for(int i = 0; i < mapa.getFilas(); i++){
            for(int j = 0; j < mapa.getCols(); j++){
                if(mapa.esObstaculo(i, j)){
                    ret[i][j] = 2;
                    visitado[i][j] = 2;
                    //System.out.print(ANSI_RED + 2 + " " + ANSI_RESET);
                }else if (visitado[i][j] == CUBIERTO){
                    ret[i][j] = CUBIERTO;
                   // System.out.print(ANSI_GREEN + visitado[i][j] + " " + ANSI_RESET);
                } else if (visitado[i][j] == CAMARA){
                    ret[i][j] = CAMARA;
                  //  System.out.print(ANSI_BLUE + visitado[i][j] + " " + ANSI_RESET);
                }else{
                    ret[i][j] = 0;
                    //System.out.print(visitado[i][j] + " ");
                }
            }
            //System.out.println("");
        }
        //System.out.println("");
        //double[][] dec = c.decode();
        //for(int i = 0; i < dec.length; i++)
            //System.out.println(dec[i][0] + " " + dec[i][1]);
        return ret;
    }

    private boolean inside(int x, int y){
        return ((x >= 0) && (x < mapa.getFilas()) && 
                y >= 0 && y < mapa.getCols()); 
    }

    public double getPunt(){
        return punt;
    }

    public boolean getPonder(){
        return ponderado;
    }

    public int[][] getMap(){
        return visitado.clone();
    }

    public Fitness clone(){
        return new FitnessReal(mapa, c, ponderado);
    }

    
    private boolean lineaLibre(double x1, double y1, double x2, double y2) {
        // 1. Calcular distancia total
        double dist = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));

        // Optimización: Si está muy cerca, asumimos que se ve
        if (dist < 0.1) return !mapa.esObstaculo((int)Math.floor(x1), (int)Math.floor(y1));
        // 2. Definir la resolución del paso (20 pasos por celda = 0.05 de avance)
        int pasos = Math.max(1, (int)(dist * 20.0));
        double dx = (x2 - x1) / pasos;
        double dy = (y2 - y1) / pasos;

        double px = x1;
        double py = y1;

        // Guardamos la celda anterior para detectar cambios de diagonal
        int prevX = (int)x1;
        int prevY = (int)y1;
        // 3. Recorrer el rayo paso a paso
        for (int i = 0; i < pasos; i++) {
            px += dx;
            py += dy;

            int cx = (int)px; // Fila actual
            int cy = (int)py; // Columna actual

            // A. Chequeo de límites del mapa
            if (cx < 0 || cx >= mapa.getFilas() || cy < 0 || cy >= mapa.getCols()) 
                return false;


            if (mapa.esObstaculo(cx, cy) || visitado[cx][cy] == CAMARA) 
                return false;

            if (cx != prevX && cy != prevY) {
                if (mapa.esObstaculo(cx, prevY) && mapa.esObstaculo(prevX, cy)) {
                    return false;
                }
            }
            prevX = cx;
            prevY = cy;
        }

        return true;
    }

    private double[][] cone(double camx, double camy,
                        double dirX, double dirY,
                        double angle) {

        int rad = mapa.getDist();
        int iter = 0;

        double[][] ret = new double[rad * rad * 4 + 1][2];

        // Precompute once
        double radSq = rad * rad;
        double limit = Math.cos(Math.toRadians(angle / 2.0));

        for (int i = 0; i < mapa.getFilas(); i++) {
            for (int j = 0; j < mapa.getCols(); j++) {

                double dx = i - camx;
                double dy = j - camy;

                double distSq = dx * dx + dy * dy;

                // 1️⃣ Check circle
                if (distSq <= radSq) {

                    // 2️⃣ Dot product (NO sqrt needed)
                    double dot = dx * dirX + dy * dirY;

                    // Compare without normalizing
                    if (dot >= limit * Math.sqrt(distSq)) {

                        ret[iter][0] = i;
                        ret[iter][1] = j;
                        iter++;
                    }
                }
            }
        }

        ret[iter][0] = -1;
        ret[iter][1] = -1;

        return ret;
    }
}
