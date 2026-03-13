package view;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class MejorMapa extends JPanel {

    private int[][] map; // 0 = free, 1 = obstacle
    private List<List<Point>> paths = new ArrayList<>(); // multiple A* paths

    public MejorMapa() {
        setBackground(Color.WHITE);
    }

    // Update the grid map
    public void updateMap(int[][] map) {
        this.map = map;
        repaint();
    }

    // Add a new A* path
    public void addPath(List<Point> path) {
        if (path != null && !path.isEmpty()) {
            paths.add(path);
            repaint();
        }
    }

    // Clear all paths
    public void clearPaths() {
        paths.clear();
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
                g2.fillRect(x, y, tileSize, tileSize);
                g2.setColor(Color.GRAY);
                g2.drawRect(x, y, tileSize, tileSize);
            }
        }

        // Draw all paths
        g2.setColor(Color.BLUE);
        for (List<Point> path : paths) {
            for (int i = 0; i < path.size() - 1; i++) {
                Point a = path.get(i);
                Point b = path.get(i + 1);
                g2.drawLine(
                        a.y * tileSize + tileSize / 2, a.x * tileSize + tileSize / 2,
                        b.y * tileSize + tileSize / 2, b.x * tileSize + tileSize / 2
                );
            }
            // Optionally mark start/end
            if (!path.isEmpty()) {
                Point start = path.get(0);
                Point end = path.get(path.size() - 1);
                g2.setColor(Color.GREEN);
                g2.fillRect(start.y * tileSize, start.x * tileSize, tileSize, tileSize);
                g2.setColor(Color.RED);
                g2.fillRect(end.y * tileSize, end.x * tileSize, tileSize, tileSize);
                g2.setColor(Color.BLUE); // reset for next path
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