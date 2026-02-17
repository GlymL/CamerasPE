package logic;

public class CromFit implements Comparable<CromFit>{
    private Cromosomas crom;
    private Fitness fit;
    private double apt;


    private CromFit(Cromosomas c, Fitness f, Double a){
        crom = c;
        fit = f;
        apt = a;
    }
    public CromFit(Cromosomas c, Fitness f){
        crom = c;
        fit = f;
    }

    public void calculateAptitude(Double totalF){
        apt = fit.getPunt() / totalF;
    }


   @Override
    public int compareTo(CromFit o) {
        return Double.compare(this.apt, o.apt);
    }

    public CromFit clone(){
        return new CromFit(crom.clone(), fit.clone(), apt);

    }

    public Cromosomas getCrom() { return crom; }
    public Fitness getFit() { return fit; }
    public double getApt() { return apt; }
    public void setFit(Fitness fit) { this.fit = fit; }
    
}
