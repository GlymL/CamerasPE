package view;

import javax.swing.*;
import java.awt.*;

public class MejorMapa extends JPanel {

    private enum Mode { BINARY, REAL }

    private Mode mode = Mode.BINARY;

    private int[][] binaryMap;
    private int[][] realMap;

    private double[][] cams;
    private double coneAngle;
    private double radius;

    public MejorMapa() {
        setBackground(Color.WHITE);
    }



    public void updateBinary(int[][] visitado) {
        this.binaryMap = visitado;
        this.mode = Mode.BINARY;
        repaint();
    }

    public void updateReal(int[][] mapa,
                           int[][] visible,
                           double[][] cameras,
                           double coneAngle,
                           double radius) {

        this.binaryMap = mapa;
        this.realMap = visible;

        cams = new double[cameras.length][3];
        for(int i = 0; i < cameras.length; i++){
            cams[i][0] = cameras[i][0];
            cams[i][1] = cameras[i][1];
            cams[i][2] = cameras[i][2];
        }
        this.coneAngle = coneAngle;
        this.radius = radius;

        this.mode = Mode.REAL;
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (binaryMap == null) return;

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int cols = binaryMap[0].length;
        int rows = binaryMap.length;

        // Compute dynamic tile size to fit map in panel
        int tileWidth = Math.max(1, panelWidth / cols);
        int tileHeight = Math.max(1, panelHeight / rows);
        int tileSize = Math.min(tileWidth, tileHeight);

        if (mode == Mode.BINARY) {
            paintBinary(g, tileSize);
        } else {
            paintReal((Graphics2D) g, tileSize);
        }
    }


    private void paintBinary(Graphics g, int tileSize) {
        for (int row = 0; row < binaryMap.length; row++) {
            for (int col = 0; col < binaryMap[0].length; col++) {

                int x = col * tileSize;
                int y = row * tileSize;
                int value = binaryMap[row][col];

                switch (value) {
                    case 2: g.setColor(Color.BLACK); break;   // obstacle
                    case 1: g.setColor(Color.YELLOW); break;  // covered
                    case 7: g.setColor(Color.RED); break;     // camera
                    default: g.setColor(Color.WHITE);         // empty
                }

                g.fillRect(x, y, tileSize, tileSize);
                g.setColor(Color.GRAY);
                g.drawRect(x, y, tileSize, tileSize);
            }
        }
    }


     private void paintReal(Graphics2D g2, int tileSize) {

        for (int row = 0; row < binaryMap.length; row++) {
            for (int col = 0; col < binaryMap[0].length; col++) {

                int x = col * tileSize;
                int y = row * tileSize;

                // Base map
                if (binaryMap[row][col] == 1)
                    g2.setColor(Color.BLACK);
                else
                    g2.setColor(Color.WHITE);

                g2.fillRect(x, y, tileSize, tileSize);

                // Visibility overlay
                if (realMap != null && realMap[row][col] == 1
                     && binaryMap[row][col] != 1
                ) {
                    float alpha = (float) realMap[row][col];
                    alpha = Math.max(0f, Math.min(1f, alpha));

                    g2.setColor(new Color(1f, 1f, 0f, alpha));
                    g2.fillRect(x, y, tileSize, tileSize);
                }

                g2.setColor(Color.GRAY);
                g2.drawRect(x, y, tileSize, tileSize);
            }
        }

        drawCameras(g2, tileSize);
        drawCone(g2, tileSize);
    }

   private void drawCameras(Graphics2D g2, int tileSize) {
    if (cams == null) return;

    for (double[] cam : cams) {
        double camX = cam[0];
        double camY = cam[1];

        // Draw camera dot
        g2.setColor(Color.RED);
        int camPixelX = (int) (camY * tileSize);
        int camPixelY = (int) (camX * tileSize);
        g2.fillOval(camPixelX - 5, camPixelY - 5, 10, 10);
    }
}

private void drawCone(Graphics2D g2, int tileSize) {
    if (cams == null) return;

    for (double[] cam : cams) {
        double camX = cam[0];
        double camY = cam[1];
        double dirAngle = cam[2];

        double leftAngle = dirAngle - coneAngle / 2.0;
        double rightAngle = dirAngle + coneAngle / 2.0;

        int camPixelX = (int) (camY * tileSize);
        int camPixelY = (int) (camX * tileSize);

        // Convert angle + radius to pixel coordinates, flipping y
        Point left = angleToPixel(camX, camY, leftAngle, radius, tileSize);
        Point right = angleToPixel(camX, camY, rightAngle, radius, tileSize);

        g2.setColor(Color.RED);
        g2.drawLine(camPixelX, camPixelY, left.x, left.y);
        g2.drawLine(camPixelX, camPixelY, right.x, right.y);
    }
}

// Helper: convert angle & distance to pixel coordinates
    private Point angleToPixel(double camX, double camY, double angleDeg, double r, int tileSize) {
        int px = (int) ((camY + r * Math.cos(Math.toRadians(angleDeg))) * tileSize);
        int py = (int) ((camX + r * Math.sin(Math.toRadians(angleDeg))) * tileSize); // flip y
        return new Point(px, py);
    }



    @Override
    public Dimension getPreferredSize() {
        if (binaryMap == null) return new Dimension(400, 400);
        int panelWidth = 800;
        int panelHeight = 600;
        int cols = binaryMap[0].length;
        int rows = binaryMap.length;

        int tileWidth = Math.max(1, panelWidth / cols);
        int tileHeight = Math.max(1, panelHeight / rows);
        int tileSize = Math.min(tileWidth, tileHeight);

        return new Dimension(cols * tileSize, rows * tileSize);
    }
}