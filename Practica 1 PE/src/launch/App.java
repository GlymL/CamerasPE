package launch;


import view.EvolutionFullGUI;
import view.FitnessChartPanel;

public class App {
    public static void main(String[] args) throws Exception {
        FitnessChartPanel f = new FitnessChartPanel();
        //new EvolutionFullGUI().setVisible(true);
        f.update(8, 10);
        f.update(20, 20);
        f.update(100, 40);
        

    }
}
