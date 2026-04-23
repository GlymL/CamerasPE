package logic;

public class TerminalNode extends ASTNode {
    boolean executed;
    public TerminalNode(Action a) {
        super(a);
        executed = false;
    }

    public Action getAction() {
        return (Action) content;
    }

    @Override
    public void execute(Object params) {    
        RoverState rover = (RoverState) params;

        if (!rover.getAccionTomada()) {
            MovementInfo minfo = rover.executeAction(getAction());
            rover.lowerEnergia(minfo.energia());
            executed = true;

            rover.setAccionTomada(true);
        }
        
    }

    @Override
    public boolean isFinished() {
        return executed;
    }

    @Override
    public int getNumberOfNodes() {
        return 1;
    }
}
