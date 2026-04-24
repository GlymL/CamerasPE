package logic;

public enum FunctionOperator {
    LESS, GREATER, EQUAL;

    public boolean evaluate(int a, int b) {
        switch (this) {
            case LESS:
                return a < b;
            case GREATER:
                return a > b;
            case EQUAL:
                return a == b;
        }
        return false;
    }
}
