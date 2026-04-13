package logic;

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
}
