package by.siluk.library;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractBook implements Serializable, Cloneable {

    private String author;

    private String name;

    protected AbstractBook(String author, String name) {
        this.author = author;
        this.name = name;
    }

    public abstract boolean isReadable(List<Abonement> abonementList, boolean inReadingHall);

    public void read() {
        System.out.println("Прочитана книга " + name);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public AbstractBook clone() {
        try {
            AbstractBook clone = (AbstractBook) super.clone();
            clone.name = name;
            clone.author = author;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
