package by.den.motorcyclist;

import java.io.Serializable;
import java.util.Objects;

public abstract class Ammunition implements Serializable, Cloneable {
    int weight;
    String name;

    protected Ammunition(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public abstract int getCost();

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Ammunition{" +
                "weight=" + weight +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ammunition that = (Ammunition) o;
        return weight == that.weight && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight, name);
    }

    @Override
    protected Ammunition clone() throws CloneNotSupportedException {
        Ammunition result = (Ammunition) super.clone();
        result.name = name;
        result.weight = weight;
        return result;
    }
}
