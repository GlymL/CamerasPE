package logic;

import java.util.Random;

public enum SpatialSensor {
    DIST_MUESTRA, DIST_ARENA, DIST_OBSTACULO, NIVEL_ENERGIA;

    public int selectValue(RoverState state) {
        switch (this) {
            case DIST_MUESTRA:
                return state.getDistMuestra();
            case DIST_ARENA:
                return state.getDistArena();
            case DIST_OBSTACULO:
                return state.getDistObstaculo();
            case NIVEL_ENERGIA:
                return state.getNivelEnergia();
            default:
                return -1;
        }
    }

    public static SpatialSensor randomSensor() {
        Random r = new Random();

        return switch (r.nextInt(4)) {
            case 0 -> DIST_MUESTRA;
            case 1 -> DIST_ARENA;
            case 2 -> DIST_OBSTACULO;
            case 3 -> NIVEL_ENERGIA;
            default -> null;
        };
    }
}
