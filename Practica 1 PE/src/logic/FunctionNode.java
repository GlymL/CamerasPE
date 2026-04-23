package logic;

public class FunctionNode extends ASTNode {
    boolean evaluated;
    boolean option;
    ASTNode if_;
    ASTNode else_;
    
    public FunctionNode(Function f, ASTNode ifChild, ASTNode elseChild) {
        super(f);
        if_ = ifChild;
        else_ = elseChild;

        evaluated = false;
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

    @Override
    public void execute(Object params) {
        RoverState state = (RoverState) params;
        Function f = (Function) content;

        if (!evaluated) {
            option = f.evaluate(state);
            evaluated = true;
        }

        if (option && !if_.isFinished()) {
            if_.execute(params);
            }
        else if (!else_.isFinished()) {
            else_.execute(params);
        }
    }

    @Override
    public boolean isFinished() {
        boolean child_finished = (option && if_.isFinished()) || (!option && else_.isFinished());

        return child_finished && evaluated;
    }

    @Override
    public int getNumberOfNodes() {
        return 1 + if_.getNumberOfNodes() + else_.getNumberOfNodes();
    }
}
