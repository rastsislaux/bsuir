package by.yasenko.subscription;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public abstract class Periodical implements Serializable, Cloneable {

    protected String name;

    protected int cost;

    Periodical(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

    public abstract String getName();

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Periodical{" +
                "name='" + name + '\'' +
                ", cost=" + cost +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Periodical that = (Periodical) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Periodical periodical = (Periodical) super.clone();
        periodical.cost = cost;
        periodical.name = name;
        return periodical;
    }
}
