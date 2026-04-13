package logic;

public class BlockNode extends ASTNode {
    private int index;
    private int size;
    
    public BlockNode(ASTNode[] nodes) {
        super(nodes);
    }

    public ASTNode[] getNodes() {
        return (ASTNode[]) content;
    }

    //TO-DO: ejecutar siguiente nodo, comprobar si el nodo esta acabado.

}
