package by.siluk.taxidepot;

import java.io.Serializable;
import java.util.Objects;

public abstract class Vehicle implements Cloneable, Serializable {

    private String name;

    private int fuelConsumption;

    private int cost;

    private int maxSpeed;

    protected Vehicle(String name, int fuelConsumption, int cost, int maxSpeed) {
        this.name = name;
        this.fuelConsumption = fuelConsumption;
        this.cost = cost;
        this.maxSpeed = maxSpeed;
    }

    public abstract void gas();

    public abstract void brake();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(int fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "name='" + name + '\'' +
                ", fuelConsumption=" + fuelConsumption +
                ", cost=" + cost +
                ", maxSpeed=" + maxSpeed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return fuelConsumption == vehicle.fuelConsumption && cost == vehicle.cost && maxSpeed == vehicle.maxSpeed && Objects.equals(name, vehicle.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, fuelConsumption, cost, maxSpeed);
    }

    @Override
    public Vehicle clone() {
        try {
            Vehicle clone = (Vehicle) super.clone();
            clone.name = name;
            clone.maxSpeed = maxSpeed;
            clone.fuelConsumption = fuelConsumption;
            clone.cost = cost;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
