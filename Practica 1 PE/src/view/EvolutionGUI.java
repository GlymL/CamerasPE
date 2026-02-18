package view;

// import controller.Controller;
import logic.Selection;

import javax.swing.*;
import java.awt.*;

public class EvolutionGUI extends JFrame {

    private JTextField generationsField = new JTextField("5000");
    private JTextField mapField = new JTextField("1");
    private JTextField populationField = new JTextField("100");
    private JTextField crossoverField = new JTextField("0.8");
    private JTextField mutationField = new JTextField("0.02");
    private JComboBox<Selection> selectionBox =
        new JComboBox<>(Selection.values());

    private JCheckBox ponderBox = new JCheckBox("Ponder", true);
    private JCheckBox monopointBox = new JCheckBox("Monopoint", false);

    private JButton startButton = new JButton("Start");

    public EvolutionGUI() {

        setTitle("Genetic Algorithm Configuration");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Generations:"));
        panel.add(generationsField);

        panel.add(new JLabel("Map ID:"));
        panel.add(mapField);

        panel.add(new JLabel("Population Size:"));
        panel.add(populationField);

        panel.add(new JLabel("Crossover Ratio:"));
        panel.add(crossoverField);

        panel.add(new JLabel("Mutation Ratio:"));
        panel.add(mutationField);

        panel.add(new JLabel("Selection Type:"));
        panel.add(selectionBox);

        panel.add(ponderBox);
        panel.add(monopointBox);

        panel.add(new JLabel());
        panel.add(startButton);

        add(panel, BorderLayout.CENTER);

        startButton.addActionListener(e -> startAlgorithm());
    }

    private void startAlgorithm() {
        try {
            // int generations = Integer.parseInt(generationsField.getText());
            // int map = Integer.parseInt(mapField.getText());
            // int population = Integer.parseInt(populationField.getText());
            // double crossover = Double.parseDouble(crossoverField.getText());
            // double mutation = Double.parseDouble(mutationField.getText());

            // boolean ponder = ponderBox.isSelected();
            // boolean monopoint = monopointBox.isSelected();
            // Selection SelectionStrategy = (Selection) selectionBox.getSelectedItem();

            // Controller controller = new Controller(
            //     (EvolutionFullGUI) this,
            //     generations,
            //     map,
            //     population,
            //     crossover,
            //     mutation,
            //     ponder,
            //     monopoint,
            //     SelectionStrategy
            // );

            // controller.start();

            JOptionPane.showMessageDialog(this, "Algorithm started!");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid input values!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
