package view;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import logic.AEstrellaPrecalc;
import logic.AEstrella.Pair;
import mapaApp.GeneradorCamaras;

public class MejorMapa extends JPanel {

    private int[][] map; // 0 = free, 1 = obstacle
    private Pair init;
    private Pair[] listaCamaras;
    private AEstrellaPrecalc ae;

    private List<List<Point>> dronePaths = new ArrayList<>(); // each drone's full path
    private List<Color> pathColors = new ArrayList<>();       // color per drone

    public MejorMapa() {
        setBackground(Color.WHITE);
    }

    // Update the map
    public void updateMap(GeneradorCamaras gc, AEstrellaPrecalc ae) {
        this.map = gc.getMapa();
        this.init = gc.getBase();
        this.listaCamaras = gc.getCameras();
        this.ae = ae;
        repaint();
    }

    /**
     * Set routes using int[][] rutas (camera indices per drone)
     * This will draw the full precalculated A* paths for each drone
     */
    public void setRoutes(int[][] rutas) {
        dronePaths.clear();
        pathColors.clear();

        for (int d = 0; d < rutas.length; d++) {
            List<Point> fullDronePath = new ArrayList<>();
            Pair last = init; // start from base

            for (int i = 0; i < rutas[d].length; i++) {
                int camIndex = rutas[d][i];
                if (camIndex < 0 || camIndex >= listaCamaras.length) continue;

                Pair target = listaCamaras[camIndex];
                Pair[] pathPairs;

                // Get path from AEstrellaPrecalc
                pathPairs = ae.getPathBetween(last, target); // camera -> camera

                // Add all points to full path
                for (Pair p : pathPairs) {
                    fullDronePath.add(new Point(p.getFirst(), p.getSecond()));
                }
                last = target;
            }

            if (!fullDronePath.isEmpty()) {
                dronePaths.add(fullDronePath);
                pathColors.add(Color.getHSBColor((float) Math.random(), 0.8f, 0.9f));
            }
        }
        repaint();
    }

    // Clear all paths
    public void clearPaths() {
        dronePaths.clear();
        pathColors.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        
        if (map == null) return;

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int rows = map.length;
        int cols = map[0].length;

        int tileWidth = Math.max(1, panelWidth / cols);
        int tileHeight = Math.max(1, panelHeight / rows);
        int tileSize = Math.min(tileWidth, tileHeight);

        Graphics2D g2 = (Graphics2D) g;



        // Draw grid
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = col * tileSize;
                int y = row * tileSize;

                g2.setColor(map[row][col] == 0 ? Color.BLACK : Color.WHITE);
                if(map[row][col] > 500)
                    g2.setColor(Color.GREEN);
                if(row == init.getSecond() && col == init.getFirst())
                    g2.setColor(Color.MAGENTA);
                g2.fillRect(x, y, tileSize, tileSize);

                g2.setColor(Color.GRAY);
                g2.drawRect(x, y, tileSize, tileSize);
            }
        }

        // Draw each drone's full path
        for (int d = 0; d < dronePaths.size(); d++) {
            List<Point> path = dronePaths.get(d);
            Color c = pathColors.get(d);
            g2.setColor(c);

            if (path.isEmpty()) continue;

            // Start from the base to the first path point
            Point prev = new Point(init.getSecond(), init.getFirst());

            for (Point curr : path) {
                g2.drawLine(
                        prev.y * tileSize + tileSize / 2, prev.x * tileSize + tileSize / 2,
                        curr.y * tileSize + tileSize / 2, curr.x * tileSize + tileSize / 2
                );
                prev = curr;
            }
            
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (map == null) return new Dimension(400, 400);

        int rows = map.length;
        int cols = map[0].length;
        int panelWidth = 800;
        int panelHeight = 600;
        int tileWidth = Math.max(1, panelWidth / cols);
        int tileHeight = Math.max(1, panelHeight / rows);
        int tileSize = Math.min(tileWidth, tileHeight);

        return new Dimension(cols * tileSize, rows * tileSize);
    }
}