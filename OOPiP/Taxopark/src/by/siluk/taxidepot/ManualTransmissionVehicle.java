package by.siluk.taxidepot;

import java.io.Serializable;
import java.util.Objects;

public class ManualTransmissionVehicle extends Vehicle implements Serializable, Cloneable {

    private int gear = 0;

    public ManualTransmissionVehicle(String name, int fuelConsumption, int cost, int maxSpeed) {
        super(name, fuelConsumption, cost, maxSpeed);
    }

    @Override
    public void gas() {
        System.out.println("Нажата педаль газа в машине с механической коробкой передач.");
    }

    @Override
    public void brake() {
        System.out.println("Нажата педаль тормоза в машине с механической коробкой передач.");
    }

    public void clutch() {
        System.out.println("Нажата педаль сцепления.");
    }

    public int getGear() {
        return gear;
    }

    public void setGear(int gear) {
        this.gear = gear;
    }

    @Override
    public String toString() {
        return "ManualTransmissionVehicle{" +
                "name='" + getName() + '\'' +
                ", fuelConsumption=" + getFuelConsumption() +
                ", cost=" + getCost() +
                ", maxSpeed=" + getMaxSpeed() +
                ", gear=" + gear +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ManualTransmissionVehicle that = (ManualTransmissionVehicle) o;
        return gear == that.gear;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gear);
    }

    @Override
    public ManualTransmissionVehicle clone() {
        ManualTransmissionVehicle clone = (ManualTransmissionVehicle) super.clone();
        clone.gear = gear;
        return clone;
    }

}
