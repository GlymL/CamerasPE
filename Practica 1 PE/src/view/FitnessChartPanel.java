package view;

import java.awt.Color;
import java.util.ArrayList;

// import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.math.plot.*;


public class FitnessChartPanel extends Plot2DPanel {
    ArrayList<Double> maxGeneration = new ArrayList<Double>();
    ArrayList<Double> EvolutivePressure = new ArrayList<Double>();
    ArrayList<Double> generations = new ArrayList<Double>();
    ArrayList<Double> bestFitness = new ArrayList<Double>();
    ArrayList<Double> avgFitness = new ArrayList<Double>();
    //JFrame frame = new JFrame("a plot panel");
    // Plot2DPanel plot = new Plot2DPanel();
    
 public FitnessChartPanel(){
    this.setBorder(new TitledBorder("Evolución del Fitness"));
    this.setBackground(Color.WHITE);
    //this.setVisible(true);
    //this.plotToolBar.setVisible(false);
    try{
    //frame.setSize(600, 600);
    generations.add(1.0);
    this.addLegend("SOUTH");
    //frame.setContentPane(plot);
    //frame.setVisible(true);

  }catch(Exception e) {
    e.printStackTrace();
  }
 }

 public void update(double maxGen, double pEv, double bestFit, double avgFit){
    maxGeneration.add(maxGen);
    EvolutivePressure.add(pEv);
    bestFitness.add(bestFit);
    avgFitness.add(avgFit);

    this.removeAllPlots();

  this.addLinePlot("Best Value",Color.BLUE, generations.stream().mapToDouble(Double::doubleValue).toArray(), 
                                    maxGeneration.stream().mapToDouble(Double::doubleValue).toArray());
  // this.addLinePlot("Evolutive Pressure",Color.PINK, generations.stream().mapToDouble(Double::doubleValue).toArray()
  //   ,EvolutivePressure.stream().mapToDouble(Double::doubleValue).toArray());
  // this.addLinePlot("Best Fitness",Color.RED, generations.stream().mapToDouble(Double::doubleValue).toArray(), 
  //                                   bestFitness.stream().mapToDouble(Double::doubleValue).toArray());
  // this.addLinePlot("Average Fitness",Color.GREEN, generations.stream().mapToDouble(Double::doubleValue).toArray()
  //   ,avgFitness.stream().mapToDouble(Double::doubleValue).toArray());

    generations.add(generations.getLast() + 1);

}
public void reset() {
  maxGeneration.clear();
  EvolutivePressure.clear();
  bestFitness.clear();
  avgFitness.clear();
  
  generations.clear();
  generations.add(1.0);
}
}
