package logic;

public class TerminalNode extends ASTNode {
    public TerminalNode(Action a) {
        super(a);
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
            rover.setAccionTomada(minfo.accion_tomada());
        }
        
    }
}
