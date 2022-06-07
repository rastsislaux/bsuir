package by.yasenko.subscription;

import java.io.Serializable;

public class Newspaper extends Periodical implements Serializable, Cloneable {
    Newspaper(String name, int cost) {
        super(name, cost);
    }

    @Override
    public String getName() {
        return "Newspaper \"" + name + "\"";
    }

    @Override
    public String toString() {
        return "Newspaper{" +
                "name='" + name + '\'' +
                ", cost=" + cost +
                '}';
    }

    @Override
    public Newspaper clone() {
        try {
            return (Newspaper) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
