package logic;

public class TerminalNode extends ASTNode {
    public TerminalNode(Action a) {
        super(a);
    }

    public Action getAction() {
        return (Action) content;
    }
}
