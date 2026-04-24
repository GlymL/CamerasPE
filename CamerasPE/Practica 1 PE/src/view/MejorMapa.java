package view;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import mapaApp.GeneradorMapa;

public class MejorMapa extends JPanel {

    private int[][] map;
    private int init_x;
    private int init_y;
    // private Pair[] listaCamaras;
    // private AEstrellaPrecalc ae;

    private int roverX = -1;
    private int roverY = -1;

    private final List<List<Point>> dronePaths = new ArrayList<>();
    private final List<List<Integer>> droneVisitNumbers = new ArrayList<>();
    private static final Color[] color = {Color.MAGENTA, Color.BLUE, Color.RED, Color.CYAN, new Color(255, 140, 0)};

    public MejorMapa() {
        setBackground(Color.WHITE);
    }

    public void updateMap(GeneradorMapa gc) {
        this.map = gc.getMapa();
        this.init_x = gc.getBaseX();
        this.init_y = gc.getBaseY();
        // this.listaCamaras = gc.getCameras();
        // this.ae = ae;
        repaint();
    }

    public void setRoverPosition(int x, int y) {
        this.roverX = x;
        this.roverY = y;
        repaint();
    }

    //     public void setRoutes(int[][] rutas) {
    //     dronePaths.clear();
    //     droneVisitNumbers.clear();

    //     for (int[] ruta : rutas) {
    //         List<Point> fullDronePath = new ArrayList<>();
    //         List<Integer> visitNumbers = new ArrayList<>();

    //         fullDronePath.add(new Point(init_x, init_y));
    //         int last_x = init_x, last_y = init_y;

    //         for (int i = 0; i < ruta.length; i++) {
    //             // int camIndex = ruta[i];
    //             // if (camIndex == -1) break;
    //             // if (camIndex < 0 || camIndex >= 15) continue;

    //             // Pair target = listaCamaras[camIndex];
    //             // Pair[] pathPairs = ae.getPathBetween(last, target);
    //             // if (pathPairs == null || pathPairs.length == 0) continue;

    //             // int start = last.equals(pathPairs[0]) ? 1 : 0;
    //             // for (int k = start; k < pathPairs.length; k++) {
    //             //     Pair p = pathPairs[k];
    //             //     fullDronePath.add(new Point(p.getSecond(), p.getFirst()));
    //             // }

    //             // last = target;
    //             // visitNumbers.add(camIndex);
    //         }

    //         // Pair[] returnPath = ae.getPathBetween(last, init);
    //         // if (returnPath != null && returnPath.length > 1) {
    //         //     for (int k = 1; k < returnPath.length; k++) {
    //         //         Pair p = returnPath[k];
    //         //         fullDronePath.add(new Point(p.getSecond(), p.getFirst()));
    //         //     }
    //         // }

    //         dronePaths.add(fullDronePath);
    //         droneVisitNumbers.add(visitNumbers);
    //     }

    //     repaint();
    // }

    public void clearPaths() {
        dronePaths.clear();
        droneVisitNumbers.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (map == null) return;

        Graphics2D g2 = (Graphics2D) g.create();

        int rows = map.length;
        int cols = map[0].length;

        int tileWidth = getWidth() / cols;
        int tileHeight = getHeight() / rows;
        int tileSize = Math.max(1, Math.min(tileWidth, tileHeight));

        // -------- DRAW MAP --------
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                int x = col * tileSize;
                int y = row * tileSize;

                Color colorTile;

                if (row == init_y && col == init_x) {
                    colorTile = Color.BLACK; // base floor underneath
                }
                else if (map[row][col] == 1) {
                    colorTile = new Color(120, 0, 0); // muro / obstáculo
                }
                else if (map[row][col] == 2) {
                    colorTile = Color.BLACK; // base floor under sample
                }
                else if (map[row][col] == 3) {
                    colorTile = new Color(210, 180, 130); // arena más clara
                }
                else {
                    colorTile = Color.BLACK; // libre
                }

                g2.setColor(colorTile);
                g2.fillRect(x, y, tileSize, tileSize);

                if (map[row][col] == 1) {
                    g2.setColor(new Color(180, 0, 0));
                    int edge = Math.max(2, tileSize / 5);
                    Stroke oldStroke = g2.getStroke();
                    g2.setStroke(new BasicStroke(Math.max(1, tileSize / 10)));
                    g2.drawLine(x + edge, y + edge, x + tileSize - edge, y + tileSize - edge);
                    g2.drawLine(x + edge, y + tileSize - edge, x + tileSize - edge, y + edge);
                    g2.setStroke(oldStroke);
                }

                if (map[row][col] == 2) {
                    g2.setColor(Color.YELLOW);
                    int dotSize = Math.max(4, tileSize / 4);
                    int dotX = x + (tileSize - dotSize) / 2;
                    int dotY = y + (tileSize - dotSize) / 2;
                    g2.fillOval(dotX, dotY, dotSize, dotSize);
                }

                if (row == init_y && col == init_x) {
                    int cx = x + tileSize / 2;
                    int cy = y + tileSize / 2;
                    int baseSize = Math.max(8, tileSize / 3);
                    Polygon baseMarker = new Polygon(
                        new int[]{cx - baseSize / 2, cx - baseSize / 2, cx + baseSize / 2},
                        new int[]{cy - baseSize / 2, cy + baseSize / 2, cy},
                        3
                    );
                    g2.setColor(Color.CYAN);
                    g2.fillPolygon(baseMarker);
                    g2.setColor(Color.WHITE);
                    Stroke originalStroke = g2.getStroke();
                    g2.setStroke(new BasicStroke(Math.max(1, tileSize / 15)));
                    g2.drawPolygon(baseMarker);
                    g2.setStroke(originalStroke);
                }

                g2.setColor(Color.GRAY);
                g2.drawRect(x, y, tileSize, tileSize);
            }
        }

        // -------- DRAW ROVER --------
        if (roverX >= 0 && roverY >= 0) {
            int rx = roverX * tileSize + tileSize / 2;
            int ry = roverY * tileSize + tileSize / 2;
            int roverSize = Math.max(8, tileSize / 3);

            Polygon roverMarker = new Polygon(
                new int[]{rx - roverSize / 2, rx - roverSize / 2, rx + roverSize / 2},
                new int[]{ry - roverSize / 2, ry + roverSize / 2, ry},
                3
            );

            g2.setColor(Color.RED);
            g2.fillPolygon(roverMarker);
            g2.setColor(Color.WHITE);
            Stroke roverStroke = g2.getStroke();
            g2.setStroke(new BasicStroke(Math.max(1, tileSize / 15)));
            g2.drawPolygon(roverMarker);
            g2.setStroke(roverStroke);
        }

        // -------- DRAW PATHS --------
        g2.setStroke(new BasicStroke(3));

        for (int d = 0; d < dronePaths.size(); d++) {
            List<Point> path = dronePaths.get(d);
            if (path == null || path.size() < 2) continue;

            g2.setColor(color[d % color.length]);
            double offset = (dronePaths.size() > 1) ? ((double)d / dronePaths.size() - 0.5) * tileSize * 0.5 : 0;

            Point prev = path.get(0);
            for (int i = 1; i < path.size(); i++) {
                Point curr = path.get(i);
                int x1 = (int)(prev.x * tileSize + tileSize / 2 + offset);
                int y1 = (int)(prev.y * tileSize + tileSize / 2 + offset);
                int x2 = (int)(curr.x * tileSize + tileSize / 2 + offset);
                int y2 = (int)(curr.y * tileSize + tileSize / 2 + offset);
                g2.drawLine(x1, y1, x2, y2);
                prev = curr;
            }

            if (droneVisitNumbers != null && d < droneVisitNumbers.size()) {
                List<Integer> visited = droneVisitNumbers.get(d);
                if (visited != null) {
                    int number = 1;
                    for (int camIndex : visited) {
                        if (camIndex < 0 || camIndex >= 15) continue;
                        int x = 1 * tileSize + tileSize / 2;
                        int y = 1 * tileSize + tileSize / 2 - 2;
                        g2.setColor(color[d % color.length]);
                        g2.setFont(g2.getFont().deriveFont(Font.BOLD, (float) tileSize * 0.6f));
                        g2.drawString(String.valueOf(number++), x, y);
                    }
                }
            }
        }

        g2.dispose();
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