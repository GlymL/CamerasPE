package logic;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Random;

public class CromosomasRanger{
    //IMPLEMENTAR CLONE DE ASTNODE (HECHO)
    private final ASTNode cromosoma;

    private static final Random r = new Random();

    public CromosomasRanger(ASTNode cromosoma){
        this.cromosoma = cromosoma;
    }

    public int getNumberOfNodes() {
        return cromosoma.getNumberOfNodes();
    }

    public void execute(RoverState state) {
        cromosoma.execute(state);
    }

    @Override
    public CromosomasRanger clone() {
       return new CromosomasRanger(cromosoma.clone());
    }

    CromosomasRanger[] cruce(CromosomasRanger otro, double cross_ratio) {
        if(r.nextDouble() > cross_ratio){
            return new CromosomasRanger[]{ this.clone(), otro.clone() };
        }

        ASTNode hijo1 = this.cromosoma.clone();
        ASTNode hijo2 = otro.cromosoma.clone();

        ASTNode cruce1 = hijo1.selectRandomNode();
        ASTNode cruce2 = hijo2.selectRandomNode();

        ASTNode temporal = cruce1.clone();

        if (cruce2 == hijo2 && cruce1 == hijo1) {
            hijo1 = hijo2;
            hijo2 = temporal;
        }
        else if (cruce1 == hijo1) {
            hijo1 = cruce2;
            hijo2.changeNode(cruce2, temporal);
        }
        else if (cruce2 == hijo2) {
            temporal = cruce2.clone();

            hijo2 = cruce1;
            hijo1.changeNode(cruce1, temporal);
        }
        else {
            hijo1.changeNode(cruce1, cruce2);
            hijo2.changeNode(cruce2, temporal);
        }

        CromosomasRanger[] ret = new CromosomasRanger[2];
        ret[0] = new CromosomasRanger(hijo1);
        ret[1] = new CromosomasRanger(hijo2);

        return ret;
    }

}
