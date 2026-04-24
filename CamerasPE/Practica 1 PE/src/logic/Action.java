package logic;

import java.util.Random;

public enum Action {
    AVANZAR, GIRAR_IZQ, GIRAR_DER;

    public static Action randomAction() {
        Random r = new Random();

        return switch (r.nextInt(3)) {
            case 0 -> AVANZAR;
            case 1 -> GIRAR_IZQ;
            case 2 -> GIRAR_DER;
            default -> null;
        };
    }
}
