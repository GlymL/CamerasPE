package logic;

public abstract class Cromosoma {
    

    public abstract void randomInitialize();

    public abstract Cromosoma[] cruceMonop(Cromosoma other, double crossRatio);

    public abstract void mutarBitgen(Double mutationRate);

    public abstract void mutarGauss(Double mutationRate);

    public abstract Cromosoma[] cruceUnif(Cromosoma other);

    public abstract Cromosoma[] cruceArit(Cromosoma other, double crossRatio);

    public abstract Cromosoma[] cruceBLX(Cromosoma other, double crossRatio);

    public abstract Cromosoma clone();

    public abstract double[][]decode();
}
