package logic;

import java.util.ArrayList;

public class BlockNode extends ASTNode {
    
    public BlockNode(ASTNode[] nodes) {
        super(nodes);
    }

    public ASTNode[] getNodes() {
        return (ASTNode[]) content;
    }

    @Override
    public void execute(Object params) {
        for (ASTNode node : getNodes()) {
            node.execute(params);
        }
    }    
}
