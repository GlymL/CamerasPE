package view;

import javax.swing.*;
import java.awt.*;

public class MejorMapa extends JPanel {

    public MejorMapa() {
                this.setBackground(Color.WHITE);
    }

    public void initialize(int [][] visitado) {
        this.removeAll();
        this.repaint();
        setLayout(new GridLayout(visitado.length, visitado[0].length));

        for (int row = 0; row < visitado.length; row++) {
            for (int col = 0; col < visitado[0].length; col++) {

                JPanel tile = new JPanel();
                tile.setBackground(getColor(visitado[row][col]));

                tile.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                this.add(tile);
            }
        }
    }

    private Color getColor(int value) {
    switch (value) {
        case 0: return Color.WHITE;
        case 1: return Color.GREEN;
        case 2: return Color.RED;
        case 7: return Color.BLUE;
        default: return Color.BLACK;
    }
}

}