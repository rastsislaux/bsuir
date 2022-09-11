package by.siluk.taxidepot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TaxiDepot extends ArrayList<Vehicle> implements Serializable {

    public TaxiDepot() { }

    public TaxiDepot(List<Vehicle> vehicles) {
        this.addAll(vehicles);
    }

    public void sortByFuelConsumption() {
        this.sort(Comparator.comparingInt(Vehicle::getFuelConsumption));
    }

    public int getCost() {
        return this.stream().mapToInt(Vehicle::getCost).sum();
    }

    public TaxiDepot getBySpeedRange(int min, int max) {
        return new TaxiDepot(this.stream()
                               .filter(vehicle -> (vehicle.getMaxSpeed() >= min && vehicle.getMaxSpeed() <= max))
                               .toList());
    }

}
