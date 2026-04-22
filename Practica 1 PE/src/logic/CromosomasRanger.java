package logic;

// import java.util.Random;

public class CromosomasRanger{
    //IMPLEMENTAR CLONE DE ASTNODE
    private final ASTNode cromosoma;

    // private static final Random r = new Random();

    public CromosomasRanger(ASTNode cromosoma){
        this.cromosoma = cromosoma;
        //cromosoma = new Integer[camaras+(drones-1)];
        // randomInitialize(generator);
    }
    
    // public CromosomasRanger(ASTNode cromosoma){
    //     // this.drones = drones;
    //     // this.camaras = camaras;
    //     //IMPLEMENTAR CLONE DE ASTNODE
    //     //this.cromosoma = cromosoma.clone();
    // }

    // public int getLength() {
    //     return cromosoma.length;
    // }

    // public void randomInitialize(TreeGenerator generator) {
    //     for(int i = 0; i < cromosoma.length; i++){
    //         cromosoma[i] = i;
    //     }
    //    ArrayList<Integer> al = new ArrayList<>(Arrays.asList(cromosoma));

    //     Collections.shuffle(al);

    //     for(int i = 0; i < cromosoma.length; i++){
    //         cromosoma[i] = al.get(i);
    //     }
    // }
    
    public void execute(RoverState state) {
        cromosoma.execute(state);
    }

    public CromosomasRanger clone() {
       return new CromosomasRanger(cromosoma);
    }

    // private boolean contains(Integer[] arr, int val) {
    //     for (Integer x : arr) {
    //         if (x != null && x == val) return true;
    //     }
    //     return false;
    // }
}
