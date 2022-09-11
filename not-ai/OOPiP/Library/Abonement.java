package by.siluk.library;

import java.io.Serializable;
import java.util.Objects;

public class Abonement implements Serializable, Cloneable {

    private AbstractBook book;

    private boolean inReadingHall;

    public Abonement(AbstractBook book, boolean inReadingHall) {
        this.book = book;
        this.inReadingHall = inReadingHall;
    }

    public AbstractBook getBook() {
        return book;
    }

    public void setBook(AbstractBook book) {
        this.book = book;
    }

    public boolean isInReadingHall() {
        return inReadingHall;
    }

    public void setInReadingHall(boolean inReadingHall) {
        this.inReadingHall = inReadingHall;
    }

    @Override
    public String toString() {
        return "Abonement{" +
                "book=" + book +
                ", inReadingHall=" + inReadingHall +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Abonement abonement = (Abonement) o;
        return inReadingHall == abonement.inReadingHall && Objects.equals(book, abonement.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(book, inReadingHall);
    }

    @Override
    public Abonement clone() {
        try {
            Abonement clone = (Abonement) super.clone();
            clone.inReadingHall = inReadingHall;
            clone.book = book;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
