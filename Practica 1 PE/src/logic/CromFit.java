package logic;

public class CromFit implements Comparable<CromFit>{
    private Cromosoma crom;
    private Fitness fit;
    private double apt;


    private CromFit(Cromosoma c, Fitness f, Double a){
        crom = c;
        fit = f;
        apt = a;
    }
    public CromFit(Cromosoma c, Fitness f){
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

    public Cromosoma getCrom() { return crom; }
    public Fitness getFit() { return fit; }
    public double getApt() { return apt; }
    public void setFit(Fitness fit) { this.fit = fit; }
    
}
