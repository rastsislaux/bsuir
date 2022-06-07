package by.den.motorcyclist;

import java.io.Serializable;

public class Helmet extends Ammunition implements Cloneable, Serializable {
    public Helmet(String name, int weight) {
        super(name, weight);
    }

    @Override
    public int getCost() { return 10000; }

    @Override
    public String toString() {
        return "Helmet{" +
                "weight=" + weight +
                ", name='" + name + '\'' +
                ", cost=" + getCost() + '}';
    }

    @Override
    public Helmet clone() {
        try {
            return (Helmet) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
