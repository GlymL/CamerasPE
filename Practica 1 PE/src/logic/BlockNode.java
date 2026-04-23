package logic;

import java.util.Random;

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

        if (i < size) {
            nodes[i].execute(params);
            if (nodes[i].isFinished())
                i++;
        }
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

    @Override
    public ASTNode clone() {
        ASTNode[] childs_copy = new ASTNode[size];
        ASTNode[] nodes = (ASTNode[]) this.content;

        for(int ix = 0; ix < size; ix++) {
            childs_copy[ix] = nodes[ix].clone();
        }

        return new BlockNode(childs_copy);
    }

    @Override
    public ASTNode selectRandomNode() {
        Random r = new Random();
        int opt = r.nextInt(size + 1);
        ASTNode[] nodes = (ASTNode[]) this.content;

        if (opt == size)
            return this;

        return nodes[opt];
    }

    @Override
    public boolean changeNode(ASTNode node, ASTNode change) {
        ASTNode[] nodes = (ASTNode[]) this.content;
        boolean changed = false;

        for (int ix = 0; ix < size; ix++) {
            if (nodes[ix] == node) {
                nodes[ix] = change;

                changed = true;
                break;
            }
            else if (nodes[ix].changeNode(node, change)) {
                changed = true;
                break;
            }
        }

        return changed;
    }

    @Override
    public int getHeight() {
        int max_height = 0;

        for (ASTNode child : (ASTNode[]) this.content) {
            max_height = Math.max(max_height, child.getHeight());
        }

        return max_height + 1;
    }

    @Override
    public void reset() {
        i = 0;
        for (ASTNode child : (ASTNode[]) this.content) {
            child.reset();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        ASTNode[] nodes = (ASTNode[]) this.content;
        for (int j = 0; j < nodes.length; j++) {
            sb.append(nodes[j].toString());
            if (j < nodes.length - 1) sb.append("; \n");
        }
        sb.append(" }");
        return sb.toString();
    }
}


