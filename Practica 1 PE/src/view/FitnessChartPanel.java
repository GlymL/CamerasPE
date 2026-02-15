package view;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.*;
import org.math.plot.*;


public class FitnessChartPanel extends JPanel {
    ArrayList<Double> x = new ArrayList<Double>();
    ArrayList<Double> y = new ArrayList<Double>();
    ArrayList<Double> generations = new ArrayList<Double>();
    JFrame frame = new JFrame("a plot panel");
    Plot2DPanel plot = new Plot2DPanel();
    
 public FitnessChartPanel(){
    try{
    frame.setSize(600, 600);
    generations.add(1.0);
    plot.addLegend("SOUTH");
    frame.setContentPane(plot);
    frame.setVisible(true);

  }catch(Exception e) {
    e.printStackTrace();
  }
 }

 public void update(double maxGen, double pEv){
    x.add(maxGen);
    y.add(pEv);
    plot.removeAllPlots();

  plot.addLinePlot("Fitness",Color.BLACK, generations.stream().mapToDouble(Double::doubleValue).toArray(), 
                                    x.stream().mapToDouble(Double::doubleValue).toArray());
  plot.addLinePlot("Fitness",Color.RED, generations.stream().mapToDouble(Double::doubleValue).toArray()
    ,y.stream().mapToDouble(Double::doubleValue).toArray());
    generations.add(generations.getLast() + 1);

}
}
