package agh.ics.oop.proj1;

public enum SimulationType {
    ORDINARY,
    MAGICAL;

    public String toString() {
        return switch (this){
            case ORDINARY -> "Zwykla";
            case MAGICAL -> "Magiczna";
        };
    }
}
