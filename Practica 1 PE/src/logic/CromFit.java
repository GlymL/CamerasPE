package logic;

public class CromFit implements Comparable<CromFit>{
    private Cromosoma crom;
    private FitnessBinario fit;
    private double apt;


    private CromFit(Cromosoma c, FitnessBinario f, Double a){
        crom = c;
        fit = f;
        apt = a;
    }
    public CromFit(Cromosoma c, FitnessBinario f){
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
    public FitnessBinario getFit() { return fit; }
    public double getApt() { return apt; }
    public void setFit(FitnessBinario fit) { this.fit = fit; }
    
}
