package by.siluk.library;

import java.io.Serializable;
import java.util.List;

public class ElectronicBook extends AbstractBook implements Serializable, Cloneable {

    protected ElectronicBook(String author, String name) {
        super(author, name);
    }

    @Override
    public boolean isReadable(List<Abonement> abonementList, boolean inReadingHall) {
        return !inReadingHall;
    }

    @Override
    public ElectronicBook clone() {
        return (ElectronicBook) super.clone();
    }
}
