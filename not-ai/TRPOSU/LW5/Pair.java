package by.shobik;

public class Pair<K, V> {
    K first;
    V second;

    Pair() {}

    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
