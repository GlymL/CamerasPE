package logic;

public abstract class ASTNode {
    Object content;

    public ASTNode(Object c) {
        content = c;
    }

    public abstract void execute(Object params);

    public void setContent(Object c) {
        content = c;
    }

    public abstract boolean isFinished();
    public abstract int getNumberOfNodes();
    public abstract int getHeight();
    public abstract ASTNode selectRandomNode();
    public abstract boolean changeNode(ASTNode node, ASTNode change);

    public abstract void reset();

    @Override
    public abstract ASTNode clone();

    @Override
    public abstract String toString();
}
