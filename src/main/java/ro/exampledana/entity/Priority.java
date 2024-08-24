package ro.exampledana.entity;

public enum Priority {

    HIGH(1),
    MEDIUM(2),
    LOW(3);

    private int value;

    Priority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
