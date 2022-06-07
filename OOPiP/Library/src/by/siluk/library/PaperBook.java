package by.siluk.library;

import java.io.Serializable;
import java.util.List;

public class PaperBook extends AbstractBook implements Serializable, Cloneable {

    protected PaperBook(String author, String name) {
        super(author, name);
    }

    @Override
    public boolean isReadable(List<Abonement> abonementList, boolean inReadingHall) {
        return abonementList.stream().anyMatch(
                abonement -> abonement.getBook().equals(this) && abonement.isInReadingHall() == inReadingHall
        );
    }

    @Override
    public PaperBook clone() {
        return (PaperBook) super.clone();
    }
}
