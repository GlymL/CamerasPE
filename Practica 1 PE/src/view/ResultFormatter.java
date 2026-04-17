package view;

import logic.AEstrellaPrecalc;
import logic.CromosomasRanger;
import logic.EnumFlota;
import logic.Fitness;

public class ResultFormatter {

    private final Fitness fitnessDron;

    public ResultFormatter(Fitness fitnessDron) {
        this.fitnessDron = fitnessDron;
    }

    /**
     * Computes the time for a single drone route, including return to base
     */
    private double computeTimeForSingleRoute(int[] ruta, int droneIndex) {
        if (ruta.length == 0 || ruta[0] == -1) return 0;
        AEstrellaPrecalc a = fitnessDron.getPrecalc();

        double tiempo = 0;
        int j = 0;

        while (j < ruta.length && ruta[j] != -1) {
            if (j == 0) tiempo += a.getInit(ruta[j]);
            else tiempo += a.getPrecalc(ruta[j - 1], ruta[j]);
            j++;
        }

        // Add cost to return to base
        tiempo += a.getInit(ruta[j - 1]);

        // Apply drone speed
        return tiempo / EnumFlota.values()[droneIndex].getVel();
    }

    /**
     * Builds a string describing the solution, per drone
     */
    public String buildSummaryString() {
        CromosomasRanger c = fitnessDron.getCrom();
        int[][] rutas = c.rutas();
        StringBuilder sb = new StringBuilder();

        sb.append("=== Solución del mejor individuo ===\n");
        sb.append("Fitness total: ").append(fitnessDron.getFitness()).append("\n\n");

        for (int i = 0; i < rutas.length; i++) {
            int[] ruta = rutas[i];
            sb.append("Drone ").append(i).append(":\n");

            if (ruta[0] == -1) {
                sb.append("  Sin cámaras asignadas\n\n");
                continue;
            }

            sb.append("  Ruta: Base -> ");
            int count = 1;
            for (int cam : ruta) {
                if (cam == -1) break;
                sb.append("C").append(cam).append("(").append(count).append(") -> ");
                count++;
            }
            sb.append("Base\n");

            double time = computeTimeForSingleRoute(ruta, i);
            sb.append("  Tiempo total: ").append(String.format("%.2f", time)).append("\n\n");
        }

        return sb.toString();
    }
}