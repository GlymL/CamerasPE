    package view;

    import java.awt.*;
    import java.util.ArrayList;
    import java.util.List;
    import javax.swing.*;
    import logic.AEstrella.Pair;
    import logic.AEstrellaPrecalc;
    import mapaApp.GeneradorCamaras;

    public class MejorMapa extends JPanel {

        private int[][] map; // 0 = free, 1 = obstacle
        private Pair init;
        private Pair[] listaCamaras;
        private AEstrellaPrecalc ae;

        private List<List<Point>> dronePaths = new ArrayList<>(); // each drone's full path
        private static final Color[] color = {Color.MAGENTA, Color.BLUE, Color.RED, Color.CYAN, Color.YELLOW};

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

            for (int[] ruta : rutas) {
                List<Point> fullDronePath = new ArrayList<>();
                
                // Start at base
                fullDronePath.add(new Point(init.getFirst(), init.getSecond()));
                Pair last = init;

                for (int i = 0; i < ruta.length; i++) {
                    int camIndex = ruta[i];
                    if (camIndex == -1) break;

                    Pair target = listaCamaras[camIndex];
                    Pair[] pathPairs = ae.getPathBetween(last, target);
                    if (pathPairs == null) continue;

                    // Skip first point if it's the same as last
                    int start = (last.equals(pathPairs[0])) ? 1 : 0;
                    for (int k = start; k < pathPairs.length; k++) {
                        Pair p = pathPairs[k];
                        fullDronePath.add(new Point(p.getSecond(), p.getFirst()));
                    }

                    last = target;
                }

                // Return to base
                Pair[] returnPath = ae.getPathBetween(last, init);
                if (returnPath != null && returnPath.length > 1) {
                    for (int k = 1; k < returnPath.length; k++) { // skip first to avoid duplicate
                        Pair p = returnPath[k];
                        fullDronePath.add(new Point(p.getSecond(), p.getFirst()));
                    }
                }

                if (!fullDronePath.isEmpty()) {
                    dronePaths.add(fullDronePath);
                }
            }

            repaint();
        }

        // Clear all paths
        public void clearPaths() {
            dronePaths.clear();
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

                    // Choose tile color
                    Color colorTile;

                    if (row == init.getSecond() && col == init.getFirst()) {
                        colorTile = Color.MAGENTA; // base
                    }
                    else if (map[row][col] > 500) {
                        colorTile = Color.GREEN; // camera
                    }
                    else if (map[row][col] == 0) {
                        colorTile = Color.BLACK; // free
                    }
                    else {
                        colorTile = new Color(255, 0, 0, map[row][col]*10); // obstacle
                    }

                    g2.setColor(colorTile);
                    g2.fillRect(x, y, tileSize, tileSize);

                    // Grid lines
                    g2.setColor(Color.GRAY);
                    g2.drawRect(x, y, tileSize, tileSize);
                }
            }

            // -------- DRAW PATHS --------
            g2.setStroke(new BasicStroke(3));
            
            int size = dronePaths.size();
            for (int d = 0; d < size; d++) {
                
                List<Point> path = dronePaths.get(d);
                if (path == null || path.size() < 2) {
                    System.out.println("Drone " + d + " has empty path, skipping");
                    continue;
                }

                g2.setColor(color[d % color.length]);

                // small offset for parallel lines
                double offset = ((double)d / size - 0.5) * tileSize * 0.5;

                // start from the first point
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