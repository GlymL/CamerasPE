package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FitnessChartPanel extends JPanel {

    private List<Double> absoluteBestFitness;
    private List<Double> bestOfGeneration;
    private List<Double> avgFitness;

    private static final int PADDING_LEFT = 50;
    private static final int PADDING_RIGHT = 20;
    private static final int PADDING_TOP = 30;
    private static final int PADDING_BOTTOM = 50;

    private double maxFitness = 40.0;

    public FitnessChartPanel() {
        setBackground(Color.WHITE);
    }

    public void setData(List<Double> absoluteBest, List<Double> bestGen, List<Double> avg) {
        this.absoluteBestFitness = absoluteBest;
        this.bestOfGeneration = bestGen;
        this.avgFitness = avg;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (absoluteBestFitness == null || bestOfGeneration == null || avgFitness == null) return;
        if (absoluteBestFitness.isEmpty() || bestOfGeneration.isEmpty() || avgFitness.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Draw background grid
        drawGrid(g2, w, h);

        // Draw axes
        drawAxes(g2, w, h);

        // Draw lines
        drawLine(g2, absoluteBestFitness, Color.CYAN, w, h);
        drawLine(g2, bestOfGeneration, Color.RED, w, h);
        drawLine(g2, avgFitness, new Color(0, 128, 0), w, h); // dark green

        // Draw legend and labels
        drawLegend(g2, w, h);
        drawLabels(g2, w, h);
    }

    private void drawGrid(Graphics2D g2, int w, int h) {
        g2.setColor(new Color(200, 200, 200));
        int stepsY = 8;
        int stepsX = 10;

        // Horizontal grid lines
        for (int i = 0; i <= stepsY; i++) {
            int y = PADDING_TOP + i * (h - PADDING_TOP - PADDING_BOTTOM) / stepsY;
            g2.drawLine(PADDING_LEFT, y, w - PADDING_RIGHT, y);
        }

        // Vertical grid lines
        for (int i = 0; i <= stepsX; i++) {
            int x = PADDING_LEFT + i * (w - PADDING_LEFT - PADDING_RIGHT) / stepsX;
            g2.drawLine(x, PADDING_TOP, x, h - PADDING_BOTTOM);
        }
    }

    private void drawAxes(Graphics2D g2, int w, int h) {
        g2.setColor(Color.BLACK);

        // Y axis
        g2.drawLine(PADDING_LEFT, PADDING_TOP, PADDING_LEFT, h - PADDING_BOTTOM);

        // X axis
        g2.drawLine(PADDING_LEFT, h - PADDING_BOTTOM, w - PADDING_RIGHT, h - PADDING_BOTTOM);
    }

    private void drawLine(Graphics2D g2, List<Double> data, Color color, int w, int h) {
        g2.setColor(color);
        int n = data.size();

        for (int i = 1; i < n; i++) {
            int x1 = mapX(i - 1, n, w);
            int y1 = mapY(data.get(i - 1), h);
            int x2 = mapX(i, n, w);
            int y2 = mapY(data.get(i), h);
            g2.drawLine(x1, y1, x2, y2);
        }
    }

    // Map generation index to X coordinate
    private int mapX(int index, int totalPoints, int width) {
        int usableWidth = width - PADDING_LEFT - PADDING_RIGHT;
        return PADDING_LEFT + (int) ((index / (double)(totalPoints - 1)) * usableWidth);
    }

    // Map fitness value to Y coordinate (inverted because y=0 is top)
    private int mapY(double fitness, int height) {
        int usableHeight = height - PADDING_TOP - PADDING_BOTTOM;
        double ratio = fitness / maxFitness;
        ratio = Math.min(1.0, Math.max(0.0, ratio)); // Clamp 0..1
        return height - PADDING_BOTTOM - (int)(ratio * usableHeight);
    }

    private void drawLegend(Graphics2D g2, int w, int h) {
        int x = PADDING_LEFT + 10;
        int y = PADDING_TOP - 10;

        int boxSize = 12;
        int spacing = 100;

        // Mejor absoluto - Cyan
        g2.setColor(Color.CYAN);
        g2.fillRect(x, y, boxSize, boxSize);
        g2.setColor(Color.BLACK);
        g2.drawString("Mejor absoluto", x + boxSize + 5, y + boxSize - 2);

        // Mejor de la generación - Red
        x += spacing;
        g2.setColor(Color.RED);
        g2.fillRect(x, y, boxSize, boxSize);
        g2.setColor(Color.BLACK);
        g2.drawString("Mejor de la generación", x + boxSize + 5, y + boxSize - 2);

        // Media de la generación - Green
        x += spacing;
        g2.setColor(new Color(0, 128, 0));
        g2.fillRect(x, y, boxSize, boxSize);
        g2.setColor(Color.BLACK);
        g2.drawString("Media de la generación", x + boxSize + 5, y + boxSize - 2);
    }

    private void drawLabels(Graphics2D g2, int w, int h) {
        g2.setColor(Color.BLACK);
        // Y axis labels (from 0 to maxFitness)
        int steps = 8;
        for (int i = 0; i <= steps; i++) {
            int y = PADDING_TOP + i * (h - PADDING_TOP - PADDING_BOTTOM) / steps;
            double val = maxFitness * (1 - (i / (double)steps));
            String label = String.format("%.1f", val);
            g2.drawString(label, 5, y + 5);
        }

        // X axis labels (generation numbers)
        int genSteps = 10;
        int n = absoluteBestFitness.size();
        for (int i = 0; i <= genSteps; i++) {
            int x = PADDING_LEFT + i * (w - PADDING_LEFT - PADDING_RIGHT) / genSteps;
            int genNum = (int)((i / (double)genSteps) * (n - 1));
            g2.drawString(String.valueOf(genNum), x - 10, h - PADDING_BOTTOM + 20);
        }

        // Axis titles
        g2.drawString("Evaluación", 10, 15);               // Y axis title (top-left)
        g2.drawString("Generación", w / 2, h - 10);       // X axis title (bottom center)
    }
}
