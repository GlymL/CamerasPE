package logic;

public class CromosomasBinario extends Cromosoma{
    private boolean[] cromosoma;
    private int bitsFila = 0;
    private int bitsCol = 0;
    private int fila = 0;
    private int col = 0;
    private final int n_cam;

    public CromosomasBinario(int x, int y, int nCameras) {
        fila = x;
        col = y;
        bitsFila = 32 - Integer.numberOfLeadingZeros(fila);
        bitsCol = 32 - Integer.numberOfLeadingZeros(col);
        cromosoma = new boolean[nCameras * (bitsFila + bitsCol)];
        n_cam = nCameras;
    }

    public CromosomasBinario(int x, int y, int nCameras, boolean[] cromos) {
        fila = x;
        col = y;
        bitsFila = 32 - Integer.numberOfLeadingZeros(fila);
        bitsCol = 32 - Integer.numberOfLeadingZeros(col);
        n_cam = nCameras;
        cromosoma = cromos.clone();
    }

    @Override
    public void randomInitialize() {
        int index = 0;

        for (int i = 0; i < n_cam; i++) {
            int filaVal = 1 + (int) (Math.random() * fila);
            int colVal = 1 + (int) (Math.random() * col);

            index = intToBits(filaVal, bitsFila, index);
            index = intToBits(colVal, bitsCol, index);
        }
    }

    public double[][] decode() {
        double[][] cams = new double[n_cam][2];
        int index = 0;

        for (int i = 0; i < n_cam; i++) {
            cams[i][0] = bitsToInt(bitsFila, index);
            index += bitsFila;
            cams[i][1] = bitsToInt(bitsCol, index);
            index += bitsCol;
        }
        return cams;
    }

    @Override
    public void mutarBitgen(Double mutationRate) {

        for (int i = 0; i < cromosoma.length; i++) {
            if (Math.random() <= mutationRate) {
                cromosoma[i] = !cromosoma[i];
            }
        }
    }

    @Override
    public Cromosoma[] cruceUnif(Cromosoma other) {

        int cut = 1 + (int) (Math.random() * (cromosoma.length - 1));

        CromosomasBinario otro = (CromosomasBinario) other;

        boolean[] c1 = new boolean[cromosoma.length];
        boolean[] c2 = new boolean[cromosoma.length];

        for (int i = 0; i < cromosoma.length; i++) {
            if (i < cut) {
                c1[i] = this.cromosoma[i];
                c2[i] = otro.cromosoma[i];
            } else {
                c1[i] = otro.cromosoma[i];
                c2[i] = this.cromosoma[i];
            }
        }

        return new CromosomasBinario[] {
                new CromosomasBinario(fila, col, n_cam, c1),
                new CromosomasBinario(fila, col, n_cam, c2)
        };
    }

    @Override
    public Cromosoma[] cruceMonop(Cromosoma other, double crossRatio) {

        boolean[] c1 = new boolean[cromosoma.length];
        boolean[] c2 = new boolean[cromosoma.length];

         CromosomasBinario otro = (CromosomasBinario) other;

        for (int i = 0; i < cromosoma.length; i++) {
            if (Math.random() < crossRatio) {
                c1[i] = this.cromosoma[i];
                c2[i] = otro.cromosoma[i];
            } else {
                c1[i] = otro.cromosoma[i];
                c2[i] = this.cromosoma[i];
            }
        }

        return new CromosomasBinario[] {
                new CromosomasBinario(fila, col, n_cam, c1),
                new CromosomasBinario(fila, col, n_cam, c2)
        };
    }

    private int intToBits(int value, int bits, int index) {
        for (int i = bits - 1; i >= 0; i--) {
            cromosoma[index++] = ((value >> i) & 1) == 1;
        }
        return index;
    }

    private int bitsToInt(int bits, int index) {
        int value = 0;
        for (int i = 0; i < bits; i++) {
            value = (value << 1) | (cromosoma[index + i] ? 1 : 0);
        }
        return value;
    }


    public boolean[] getGenes() {
        return cromosoma.clone();
    }


    public CromosomasBinario clone(){
        return new CromosomasBinario(fila, col, n_cam, cromosoma);

    }

    @Override
    public Cromosoma[] cruceArit(Cromosoma other, double crossRatio) {
        throw new UnsupportedOperationException("Cruce Aritmetico no se puede hacer en binario.");
    }

    @Override
    public Cromosoma[] cruceBLX(Cromosoma other, double crossRatio) {
        throw new UnsupportedOperationException("Cruce BLX-Alfa no se puede hacer en binario.");
    }

    @Override
    public void mutarGauss(Double mutationRate) {
        throw new UnsupportedOperationException("Mutacion gaussiana no se puede hacer en binario.");
    }
}