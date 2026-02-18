package logic;

public abstract class Cromosoma {
    

    public abstract void randomInitialize();

    public abstract Cromosoma[] cruceMonop(Cromosoma other, double crossRatio);

    public abstract void mutarCromosoma(Double mutationRate);

    public abstract Cromosoma[] cruceUnif(Cromosoma other);

    public abstract Cromosoma clone();

    public abstract double[][]decode();
}
