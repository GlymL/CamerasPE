package view;

import java.awt.Color;
import java.util.ArrayList;

// import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.math.plot.*;


public class FitnessChartPanel extends Plot2DPanel {
    ArrayList<Double> x = new ArrayList<Double>();
    ArrayList<Double> y = new ArrayList<Double>();
    ArrayList<Double> generations = new ArrayList<Double>();
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

 public void update(double maxGen, double pEv){
    x.add(maxGen);
    y.add(pEv);
    this.removeAllPlots();

  this.addLinePlot("Fitness",Color.BLACK, generations.stream().mapToDouble(Double::doubleValue).toArray(), 
                                    x.stream().mapToDouble(Double::doubleValue).toArray());
  this.addLinePlot("Fitness",Color.RED, generations.stream().mapToDouble(Double::doubleValue).toArray()
    ,y.stream().mapToDouble(Double::doubleValue).toArray());
    generations.add(generations.getLast() + 1);

}
}
