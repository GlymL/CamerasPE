package logic;

public class Function {
    private SpatialSensor sensor;
    private FunctionOperator operator;
    private int value;

    public Function(SpatialSensor s, FunctionOperator op, int v) {
        sensor = s;
        operator = op;
        value = v;
    }

    public SpatialSensor getSensor() {
        return sensor;
    }

    public FunctionOperator getOperator() {
        return operator;
    }

    public int getValue() {
        return value;
    }

    public boolean evaluate(RoverState state) {
        int sensorValue = sensor.selectValue(state);

        return operator.evaluate(sensorValue, value);
    }
}
