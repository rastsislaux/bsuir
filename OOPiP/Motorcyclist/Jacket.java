package by.den.motorcyclist;

import java.io.Serializable;

public class Jacket extends Ammunition implements Cloneable, Serializable {
    public Jacket(String name, int weight) {
        super(name, weight);
    }

    @Override
    public int getCost() { return 15000; }

    @Override
    public String toString() {
        return "Jacket{" +
                "weight=" + weight +
                ", name='" + name + '\'' +
                ", cost=" + getCost() + '}';
    }

    @Override
    public Jacket clone() {
        try {
            return (Jacket) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
