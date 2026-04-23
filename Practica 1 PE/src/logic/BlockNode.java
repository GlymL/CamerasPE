package logic;

public class BlockNode extends ASTNode {
    int size;
    int i;
    
    public BlockNode(ASTNode[] nodes) {
        super(nodes);
        i = 0;
        size = nodes.length;
    }

    public ASTNode[] getNodes() {
        return (ASTNode[]) content;
    }

    @Override
    public void execute(Object params) {
        ASTNode[] nodes = (ASTNode[]) this.content;

        if (i < size)
            nodes[i].execute(params);

        if (nodes[i].isFinished())
            i++;
    }

    @Override
    public boolean isFinished() {
        return i >= size;
    }

    @Override
    public int getNumberOfNodes() {
        int res = 1;
        
        for(ASTNode child : (ASTNode[]) this.content) {
            res += child.getNumberOfNodes();
        }
        return res;
    }    
}
