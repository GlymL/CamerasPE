package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import static org.junit.jupiter.api.Assumptions.abort;

import java.awt.*;

// import logic.Fitness;
import logic.Selection;
import controller.Controller;

public class EvolutionFullGUI extends JFrame{

  FitnessChartPanel fitnessChart;

  private JComboBox < String > escenarioBox =
    new JComboBox < > (new String[] {
      "Escenario 1",
      "Escenario 2",
      "Escenario 3"
    });
  private JCheckBox importanciaBox =
    new JCheckBox("Modo Importancia");
  private JCheckBox monopointBox =
    new JCheckBox("Modo cruce monopunto");
  private JComboBox < Selection > selectionBox =
    new JComboBox < > (Selection.values());

  private JSpinner populationSpinner =
    new JSpinner(new SpinnerNumberModel(100, 1, 10000, 10));
  private JSpinner generationsSpinner =
    new JSpinner(new SpinnerNumberModel(500, 1, 10000, 50));
  private JSpinner crossoverSpinner =
    new JSpinner(new SpinnerNumberModel(0.8, 0.0, 1.0, 0.05));
  private JSpinner mutationSpinner =
    new JSpinner(new SpinnerNumberModel(0.01, 0.0, 1.0, 0.01));

  private JButton runBinaryButton = new JButton("Ejecutar Binario");
  private JButton runRealButton = new JButton("Ejecutar Real");

  public EvolutionFullGUI() {
    setTitle("Algoritmo Genético - Cámaras");
    setSize(1100, 600);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    setLayout(new BorderLayout(10, 10));

    add(createTopPanel(), BorderLayout.NORTH);
    add(createCenterPanel(), BorderLayout.CENTER);
  }

  private JPanel createTopPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));

    panel.add(new JLabel("Escenario:"));
    panel.add(escenarioBox);
    panel.add(importanciaBox);

    return panel;
  }

  private JPanel createCenterPanel() {
    JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));

    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    panel.add(createConfigPanel());
    panel.add(createMapPanel());
    panel.add(createFitnessPanel());

    return panel;
  }

  private JPanel createConfigPanel() {

    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    panel.setBorder(new TitledBorder("Configuración AG"));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(new JLabel("Población:"), gbc);

    gbc.gridx = 1;
    panel.add(populationSpinner, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    panel.add(new JLabel("Generaciones:"), gbc);

    gbc.gridx = 1;
    panel.add(generationsSpinner, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    panel.add(new JLabel("Prob. Cruce:"), gbc);

    gbc.gridx = 1;
    panel.add(crossoverSpinner, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    panel.add(new JLabel("Prob. Mutación:"), gbc);

    gbc.gridx = 1;
    panel.add(mutationSpinner, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    panel.add(new JLabel("Estr. Cruce:"), gbc);

    gbc.gridx = 1;
    panel.add(selectionBox, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    panel.add(new JLabel("Cruce monopunto"), gbc);

    gbc.gridx = 1;
    panel.add(monopointBox, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    gbc.gridwidth = 2;
    runBinaryButton.addActionListener((e) -> runAlgorithm(false));
    panel.add(runBinaryButton, gbc);

    gbc.gridy++;
     runRealButton.addActionListener((e) -> runAlgorithm(true));
    panel.add(runRealButton, gbc);

    return panel;
  }

  private JPanel createMapPanel() {

    JPanel panel = new JPanel();
    panel.setBorder(new TitledBorder("Mapa"));
    panel.setBackground(Color.WHITE);

    return panel;
  }

  private FitnessChartPanel createFitnessPanel() {

    fitnessChart = new FitnessChartPanel();

    return fitnessChart;
  }

  private void runAlgorithm(boolean isReal) {

    int population = (int) populationSpinner.getValue();
    int generations = (int) generationsSpinner.getValue();
    double crossover = (double) crossoverSpinner.getValue();
    double mutation = (double) mutationSpinner.getValue();

    boolean importancia = importanciaBox.isSelected();
    boolean monopunto = monopointBox.isSelected();
    int escenario = escenarioBox.getSelectedIndex();

    Selection selection =
      (Selection) selectionBox.getSelectedItem();

    Controller controller = new Controller(
      this,
      generations,
      escenario + 1,
      population,
      crossover,
      mutation,
      importancia,
      monopunto,
      selection);

    new Thread(() -> controller.start()).start();
  }

  public void updateChart(double maxGen, double pEv, double bestFitness, double avgFitness) {
    fitnessChart.update(maxGen, pEv, bestFitness, avgFitness);
  }

}