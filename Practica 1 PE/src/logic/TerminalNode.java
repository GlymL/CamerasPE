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

    @Override
    public ASTNode clone() {
        return new TerminalNode(getAction());
    }

    @Override
    public ASTNode selectRandomNode() {
        return this;
    }

    @Override
    public boolean changeNode(ASTNode node, ASTNode change) {
        //Como no tiene hijos, no puede cambiar nada.
        return false;
    }

    @Override
    public int getHeight() {
        return 1;
    }
}
