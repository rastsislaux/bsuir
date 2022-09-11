package by.den.motorcyclist;

import java.io.Serializable;

public class Gloves extends Ammunition implements Cloneable, Serializable {
    protected Gloves(String name, int weight) {
        super(name, weight);
    }

    @Override
    public int getCost() { return 5000; }

    @Override
    public String toString() {
        return "Gloves{" +
                "weight=" + weight +
                ", name='" + name + '\'' +
                ", cost=" + getCost() + '}';
    }

    @Override
    public Gloves clone() {
        try {
            return (Gloves) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
