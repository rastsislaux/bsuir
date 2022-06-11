package by.yasenko.subscription;

import java.io.Serializable;

public class Magazine extends Periodical implements Serializable, Cloneable {
    Magazine(String name, int cost) {
        super(name, cost);
    }

    @Override
    public String getName() {
        return "Magazine \"" + name + "\"";
    }

    @Override
    public String toString() {
        return "Magazine{" +
                "name='" + name + '\'' +
                ", cost=" + cost +
                '}';
    }

    @Override
    public Magazine clone() {
        try {
            return (Magazine) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
