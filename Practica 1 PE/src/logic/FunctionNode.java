package logic;

public class FunctionNode extends ASTNode {
    ASTNode if_;
    ASTNode else_;
    
    public FunctionNode(Function f, ASTNode ifChild, ASTNode elseChild) {
        super(f);
        if_ = ifChild;
        else_ = elseChild;
    }

    public SpatialSensor getSensor() {
        return ((Function) content).getSensor();
    }

    public ASTNode getIf() {
        return if_;
    }

    public ASTNode getElse() {
        return else_;
    }

    public ASTNode getCorrectChild(RoverState state) {
        Function f = (Function) content;
        if (f.evaluate(state)) {
            return if_;
        } else {
            return else_;
        }
    }
}
