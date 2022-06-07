package by.den.motorcyclist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Motorcyclist implements Cloneable, Serializable {
    private List<Ammunition> ammunitionList;

    Motorcyclist() {
        ammunitionList = new ArrayList<>();
    }

    Motorcyclist(List<Ammunition> ammunitionList) {
        this.ammunitionList = ammunitionList;
    }

    public List<Ammunition> getAmmunitionList() {
        return ammunitionList;
    }

    public int getAmmunitionAmount() {
        return ammunitionList.size();
    }

    public void addAmmunition(Ammunition ammunition) {
        ammunitionList.add(ammunition);
    }

    public void removeAmmunition(Ammunition ammunition) {
        ammunitionList.remove(ammunition);
    }

    public void removeAmmunition(int index) {
        ammunitionList.remove(index);
    }

    public Ammunition getAmmunition(int index) {
        return ammunitionList.get(index);
    }

    public int getAllCost() {
        return ammunitionList.stream().mapToInt(Ammunition::getCost).sum();
    }

    public void sortByWeight() {
        ammunitionList.sort((a, b) -> a.getWeight() - b.getWeight());
    }

    public List<Ammunition> filterByCost(int min, int max) {
        return ammunitionList.stream().filter(a -> a.getCost() > min && a.getCost() < max).toList();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Motorcyclist motorcyclist = (Motorcyclist) super.clone();
        motorcyclist.ammunitionList = ammunitionList;
        return motorcyclist;
    }

    @Override
    public String toString() {
        return "Motorcyclist{" +
                "ammunitionList=" + ammunitionList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Motorcyclist that = (Motorcyclist) o;
        return Objects.equals(ammunitionList, that.ammunitionList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ammunitionList);
    }
}