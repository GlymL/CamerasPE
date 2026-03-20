package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import logic.*;

public class ResultPanel extends JPanel {

    private final JTextPane textPane = new JTextPane();

    // Your fixed color array for drones:
    private static final Color[] color = {Color.MAGENTA, Color.BLUE, Color.RED, Color.CYAN, new Color(255, 140, 0)};

    public ResultPanel() {
        setLayout(new BorderLayout());
        textPane.setEditable(false);
        textPane.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(textPane);
        add(scroll, BorderLayout.CENTER);
        setBorder(BorderFactory.createTitledBorder("Resultado mejor individuo"));
    }

    public void displayFitnessDron(FitnessDron best, int seed) {
        StyledDocument doc = textPane.getStyledDocument();
        try {
            AEstrellaPrecalc a = best.getPrecalc();
            doc.remove(0, doc.getLength()); // Clear previous content

            // Style setups
            SimpleAttributeSet redBold = new SimpleAttributeSet();
            StyleConstants.setForeground(redBold, Color.RED);
            StyleConstants.setBold(redBold, true);

            SimpleAttributeSet blackBold = new SimpleAttributeSet();
            StyleConstants.setForeground(blackBold, Color.BLACK);
            StyleConstants.setBold(blackBold, true);

            SimpleAttributeSet[] droneColors = new SimpleAttributeSet[color.length];
            for (int i = 0; i < color.length; i++) {
                droneColors[i] = new SimpleAttributeSet();
                StyleConstants.setForeground(droneColors[i], color[i]);
            }

            // Header
            doc.insertString(doc.getLength(), "RUTA COMPLETADA\n\n", redBold);

            // Makespan (fitness)
            doc.insertString(doc.getLength(), "MAKESPAN: ", blackBold);
            doc.insertString(doc.getLength(), String.format("%.2f s\n", best.getFitness()), null);

            // Seed
            doc.insertString(doc.getLength(), "SEMILLA: ", blackBold);
            doc.insertString(doc.getLength(), seed + "\n\n", null);

            // TIEMPOS POR DRON line
            doc.insertString(doc.getLength(), "TIEMPOS POR DRON: ", blackBold);

            int[][] rutas = best.getCrom().rutas();
            int dronesCount = rutas.length;

            for (int i = 0; i < dronesCount; i++) {
                double speed = EnumFlota.values()[i].getVel();
                double time = computeDroneTime(rutas[i], a, speed);

                String droneLabel = "D" + (i + 1) + " (x" + String.format("%.1f", speed) + "): ";

                doc.insertString(doc.getLength(), droneLabel, droneColors[i]);

                String timeStr = String.format("%.1f s", time);
                doc.insertString(doc.getLength(), timeStr, droneColors[i]);

                if (i < dronesCount - 1) doc.insertString(doc.getLength(), " ", null);
            }
            doc.insertString(doc.getLength(), "\n", null);

            // CROMOSOMA line
            doc.insertString(doc.getLength(), "CROMOSOMA: [ ", blackBold);

            for (int i = 0; i < dronesCount; i++) {
                SimpleAttributeSet routeColor = droneColors[i];
                int[] ruta = rutas[i];

                boolean hasCams = false;
                for (int cam : ruta) {
                    if (cam != -1) {
                        hasCams = true;
                        break;
                    }
                }

                if (hasCams) {
                    for (int cam : ruta) {
                        if (cam == -1) break;
                        doc.insertString(doc.getLength(), cam + " ", routeColor);
                    }
                }
                // If no cameras, leave empty section

                if (i < dronesCount - 1) {
                    doc.insertString(doc.getLength(), "|| ", blackBold);
                }
            }
            doc.insertString(doc.getLength(), "]\n", blackBold);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private double computeDroneTime(int[] ruta, AEstrellaPrecalc a, double speed) {
        if (ruta == null || ruta.length == 0 || ruta[0] == -1) return 0.0;

        double tiempo = 0;
        int j = 0;
        while (j < ruta.length && ruta[j] != -1) {
            if (j == 0) tiempo += a.getInit(ruta[j]);
            else tiempo += a.getPrecalc(ruta[j - 1], ruta[j]);
            j++;
        }
        tiempo += a.getInit(ruta[j - 1]);
        return tiempo / speed;
    }
}