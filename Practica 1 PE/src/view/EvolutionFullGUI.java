package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;


import java.awt.*;

import logic.EnumCruce;
import logic.EnumMutacion;
import logic.EnumSelection;
import logic.FitnessDron;
import mapaApp.MapaCamaras;
import controller.Controller;

public class EvolutionFullGUI extends JFrame{

  FitnessChartPanel fitnessChart;
  MejorMapa bd;
  private boolean bin;
  private int n_mapa;

  private JComboBox < String > escenarioBox =
    new JComboBox < > (new String[] {
      "Escenario 1 - Museo",
      "Escenario 2 - Pasillos",
      "Escenario 3 - Supermercado"
    });
  private JComboBox < EnumCruce > cruceBox =
    new JComboBox<>(EnumCruce.values());
     private JComboBox < EnumMutacion > mutaBox =
    new JComboBox<>(EnumMutacion.values());
  private JComboBox < EnumSelection > selectionBox =
    new JComboBox < > (EnumSelection.values());

  private JSpinner populationSpinner =
    new JSpinner(new SpinnerNumberModel(100, 1, 10000, 10));
  private JSpinner generationsSpinner =
    new JSpinner(new SpinnerNumberModel(500, 1, 10000, 50));
  private JSpinner crossoverSpinner =
    new JSpinner(new SpinnerNumberModel(0.8, 0.0, 1.0, 0.05));
  private JSpinner mutationSpinner =
    new JSpinner(new SpinnerNumberModel(0.01, 0.0, 1.0, 0.01));
  private JSpinner elitismoSpinner =
    new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1.0, 0.01));
  private JSpinner dronesSpinner =
    new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
  private JButton runButton = new JButton("Ejecutar generaciones");
  private Controller c;

  public EvolutionFullGUI(Controller c) {
    setTitle("Algoritmo Genético - Cámaras");
    setSize(1100, 600);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    setLayout(new BorderLayout(10, 10));

    add(createTopPanel(), BorderLayout.NORTH);
    add(createCenterPanel(), BorderLayout.CENTER);
    this.c = c;
  }

  private JPanel createTopPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));

    panel.add(new JLabel("Escenario:"));
    panel.add(escenarioBox);

    panel.add(new JLabel("Numero drones:"));
    panel.add(dronesSpinner);

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
    panel.add(new JLabel("Elitismo"), gbc);

    gbc.gridx = 1;
    panel.add(elitismoSpinner, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    panel.add(new JLabel("Est. Selección:"), gbc);

    gbc.gridx = 1;
    panel.add(selectionBox, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    panel.add(new JLabel("Est. Cruce:"), gbc);

    gbc.gridx = 1;
    panel.add(cruceBox, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    panel.add(new JLabel("Est. Mutacion"), gbc);

    gbc.gridx = 1;
    panel.add(mutaBox, gbc);


    gbc.gridx = 0;
    gbc.gridy++;
    gbc.gridwidth = 2;

    gbc.gridy++;
     runButton.addActionListener((e) -> runAlgorithm());
    panel.add(runButton, gbc);

    return panel;
  }

  private JPanel createMapPanel() {
    JPanel panel = new JPanel();

    panel.setLayout(new GridLayout(1, 1));
    panel.setBorder(new TitledBorder("Mapa"));
    panel.setBackground(Color.WHITE);

    bd = new MejorMapa();
    panel.add(bd);

    return panel;
  }

  private FitnessChartPanel createFitnessPanel() {

    fitnessChart = new FitnessChartPanel();

    return fitnessChart;
  }

  private void runAlgorithm() {

    int population = (int) populationSpinner.getValue();
    int generations = (int) generationsSpinner.getValue();
    double crossover = (double) crossoverSpinner.getValue();
    double mutation = (double) mutationSpinner.getValue();
    double elitismo = (double) elitismoSpinner.getValue();
    int n_drones = (int) dronesSpinner.getValue();
 
    EnumCruce enumCruce = (EnumCruce)cruceBox.getSelectedItem();
    EnumMutacion enumMut = (EnumMutacion)mutaBox.getSelectedItem();
    EnumSelection selection = (EnumSelection) selectionBox.getSelectedItem();
    int escenario = escenarioBox.getSelectedIndex();
    n_mapa = escenario + 1;


    new Thread(() -> c.execute(
      generations,
      escenario + 1,
      population,
      crossover,
      mutation,
      enumCruce,
      enumMut,
      selection,
      elitismo,
      n_drones)).start();
  }

  public void updateChart(double maxGen, double pEv, double bestFitness, double avgFitness) {
    SwingUtilities.invokeLater(() -> {
      fitnessChart.update(maxGen, pEv, bestFitness, avgFitness);
    });
    
  }

  public void clearChart() {
    fitnessChart.reset();
  }

  public void updateMap(int[][] visitado, FitnessDron fd) {
    if(bin){
      SwingUtilities.invokeLater(() -> bd.updateBinary(visitado));
    }else{
      //MapaCamaras mc = new MapaCamaras(n_mapa);
      //SwingUtilities.invokeLater(() ->bd.updateReal(mc.mapa, visitado, c.decode(), mc.getAngulo(), mc.getDist()));
    }
  }

}