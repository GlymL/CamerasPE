package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;


import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logic.AEstrella;
import logic.AEstrellaPrecalc;
import logic.EnumCruce;
import logic.EnumMutacion;
import logic.EnumSelection;
import logic.FitnessDron;
import mapaApp.MapaCamaras;
import controller.Controller;
import mapaApp.GeneradorCamaras;

public class EvolutionFullGUI extends JFrame{

  private class OnlyIntVerifier extends InputVerifier {
    @Override
    public boolean verify(JComponent input) {
      JTextField tf = (JTextField) input;
      Pattern pattern = Pattern.compile("^[0-9]+$");
      Matcher matcher = pattern.matcher(tf.getText());
      boolean onlyints = matcher.find();

      if (!onlyints) {
        tf.setText("3000");
      }

      return onlyints;
    }
    
  }

  FitnessChartPanel fitnessChart;
  MejorMapa bd;
  private int n_mapa;

  private final JComboBox < String > escenarioBox =
    new JComboBox < > (new String[] {
      "Escenario 1 - Museo",
      "Escenario 2 - Pasillos",
      "Escenario 3 - Supermercado"
    });
  private final JComboBox < EnumCruce > cruceBox =
    new JComboBox<>(EnumCruce.values());
     private final JComboBox < EnumMutacion > mutaBox =
    new JComboBox<>(EnumMutacion.values());
  private final JComboBox < EnumSelection > selectionBox =
    new JComboBox < > (EnumSelection.values());

  private final JSpinner populationSpinner =
    new JSpinner(new SpinnerNumberModel(100, 1, 10000, 10));
  private final JSpinner generationsSpinner =
    new JSpinner(new SpinnerNumberModel(500, 1, 10000, 50));
  private final JSpinner crossoverSpinner =
    new JSpinner(new SpinnerNumberModel(0.8, 0.0, 1.0, 0.05));
  private final JSpinner mutationSpinner =
    new JSpinner(new SpinnerNumberModel(0.01, 0.0, 1.0, 0.01));
  private final JSpinner elitismoSpinner =
    new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1.0, 0.01));
  private final JSpinner dronesSpinner =
    new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
  private final JButton runButton = new JButton("Ejecutar generaciones");
  private final JTextField seedfield = new JTextField("3000", 5);
  private final JCheckBox opt_CheckBox = new JCheckBox();
  private ResultPanel resultPanel;

  private final Controller c;
  private GeneradorCamaras gc;
  private int seed;

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

    panel.add(new JLabel("Semilla:"));
    seedfield.setInputVerifier(new OnlyIntVerifier());
    panel.add(seedfield);

    return panel;
  }

private JPanel createCenterPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weighty = 0.2;

    JPanel leftContainer = new JPanel(new GridBagLayout());
    GridBagConstraints leftGbc = new GridBagConstraints();
    leftGbc.gridx = 0;
    leftGbc.weightx = 0.3;
    leftGbc.fill = GridBagConstraints.BOTH;

    leftGbc.gridy = 0;
    leftGbc.weighty = 0.7;
    leftContainer.add(createConfigPanel(), leftGbc);

    leftGbc.gridy = 1;
    leftGbc.weighty = 0.3;
    resultPanel = new ResultPanel();
    leftContainer.add(resultPanel, leftGbc);

    panel.add(leftContainer, gbc);

    gbc.gridx = 1;
    gbc.weightx = 0.5;
    panel.add(createMapPanel(), gbc);

    gbc.gridx = 2;
    gbc.weightx = 0.3;
    panel.add(createFitnessPanel(), gbc);

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
    panel.add(new JLabel("Optimización 2-Opt:"), gbc);

    gbc.gridx = 1;
    panel.add(opt_CheckBox, gbc);

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
    seed = Integer.parseInt(seedfield.getText());
    boolean opt = opt_CheckBox.isSelected();
 
    EnumCruce enumCruce = (EnumCruce)cruceBox.getSelectedItem();
    EnumMutacion enumMut = (EnumMutacion)mutaBox.getSelectedItem();
    EnumSelection selection = (EnumSelection) selectionBox.getSelectedItem();
    int escenario = escenarioBox.getSelectedIndex();

    n_mapa = escenario + 1;

    gc = new GeneradorCamaras(seed, new MapaCamaras(n_mapa));

    AEstrellaPrecalc precalc = new AEstrellaPrecalc(gc, new AEstrella(gc.getMapa()));

    bd.updateMap(gc, precalc);
    new Thread(() -> c.execute(
      generations,
      gc,
      population,
      crossover,
      mutation,
      enumCruce,
      enumMut,
      selection,
      elitismo,
      n_drones, precalc, opt)).start();
  }

  public void updateChart(double maxGen, double pEv, double bestFitness, double avgFitness) {
    SwingUtilities.invokeLater(() -> {
      fitnessChart.update(maxGen, pEv, bestFitness, avgFitness);
    });
  }

  public void clearChart() {
    fitnessChart.reset();
  }

  public void updateMap(int[][] bestCrom) {
        bd.setRoutes(bestCrom);
  }

  public void result(FitnessDron best){

    resultPanel.displayFitnessDron(best, seed);
  }

}