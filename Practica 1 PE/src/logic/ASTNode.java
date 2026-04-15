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
}
