package by.siluk.taxidepot;

import java.io.Serializable;

public class AutomaticTransmissionVehicle extends Vehicle implements Serializable, Cloneable {

    public enum Mode {
        PARKING,
        DRIVE,
        NEUTRAL,
        REVERSE
    }

    private Mode mode;

    public AutomaticTransmissionVehicle(String name, int fuelConsumption, int cost, int maxSpeed) {
        super(name, fuelConsumption, cost, maxSpeed);
        mode = Mode.NEUTRAL;
    }

    @Override
    public void gas() {
        System.out.println("Нажата педать газа в машине с автоматической коробкой передач.");
    }

    @Override
    public void brake() {
        System.out.println("Нажата педать тормоза в машине с автоматической коробкой передач.");
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "AutomaticTransmissionVehicle{" +
                "name='" + getName() + '\'' +
                ", fuelConsumption=" + getFuelConsumption() +
                ", cost=" + getCost() +
                ", maxSpeed=" + getMaxSpeed() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public AutomaticTransmissionVehicle clone() {
        return (AutomaticTransmissionVehicle) super.clone();
    }
}
