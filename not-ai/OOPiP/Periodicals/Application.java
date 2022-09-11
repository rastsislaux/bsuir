package by.yasenko.subscription;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Application implements Serializable, Cloneable {

    ArrayList<Periodical> periodicals = new ArrayList<>();

    protected boolean isBanned;

    public int getCost() {
        return periodicals.stream().mapToInt(Periodical::getCost).sum();
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Application that = (Application) o;
        return isBanned == that.isBanned;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isBanned);
    }

    @Override
    public String toString() {
        return "Application{" +
                "isBanned=" + isBanned +
                ", cost=" + getCost() +
                ", list=" + periodicals +
                '}';
    }

    @Override
    public Object clone() {
        Application application = new Application();
        application.periodicals = periodicals;
        application.isBanned = isBanned;
        return application;
    }
}
