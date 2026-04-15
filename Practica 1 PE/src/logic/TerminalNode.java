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
        // RoverState rover = (RoverState) params;
        // switch (getAction()) {
        //     case AVANZAR -> rover.avanzar();
        //     case GIRAR_IZQ -> rover.girarIzquierda();
        //     case GIRAR_DER -> rover.girarDerecha();
        // }
    }
}
