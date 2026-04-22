package logic;

public enum Direction {
    NORTH, SOUTH, EAST, WEST;

    public Direction rotateLeft() {
        return switch (this) {
            case EAST -> NORTH;
            case NORTH -> WEST;
            case WEST -> SOUTH;
            case SOUTH -> EAST;
        };
    }

    public Direction rotateRight() {
        return switch (this) {
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
            case NORTH -> EAST;
        };
    }
}
