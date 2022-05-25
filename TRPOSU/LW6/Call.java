package by.shobik.lw6;

import java.util.Objects;

public class Call {
    private String name;
    private boolean forOperator;

    public Call(String name, boolean forOperator) {
        this.name = name;
        this.forOperator = forOperator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isForOperator() {
        return forOperator;
    }

    public void setForOperator(boolean forOperator) {
        this.forOperator = forOperator;
    }

    @Override
    public String toString() {
        return "Call{" +
                "name='" + name + '\'' +
                ", forOperator=" + forOperator +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Call call = (Call) o;
        return forOperator == call.forOperator && Objects.equals(name, call.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, forOperator);
    }
}
