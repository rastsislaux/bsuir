package by.shobik.lw4;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class Network {

    private List<Domain> domains;

    private List<Computer> computers;

    public Network() {
        this.domains = new ArrayList<>();
        this.computers = new ArrayList<>();
    }

    public Network(List<Domain> domains, List<Computer> computers) {
        this.domains = domains;
        this.computers = computers;
    }

    public List<Domain> getDomains() {
        return domains;
    }

    public void setDomains(List<Domain> domains) {
        this.domains = domains;
    }

    public List<Computer> getComputers() {
        return computers;
    }

    public void setComputers(List<Computer> computers) {
        this.computers = computers;
    }

    public Ip getIpFromDomain(String domainName) {
        for (Domain domain : domains) {
            if (domain.getName().equals(domainName))
                return domain.getCorrespondingIp();
        }
        throw new RuntimeException("Domain not found.");
    }

    public List<Computer> getComputersByIP(Ip ip) {
        return getComputersByPredicate(computer -> computer.getIp().equals(ip));
    }

    public List<Computer> getComputersByPredicate(Predicate<Computer> predicate) {
        List<Computer> result = new ArrayList<>();
        for (Computer computer : computers) {
            if (predicate.test(computer)) {
                result.add(computer);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "Network{" +
                "domains=" + domains +
                ", computers=" + computers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Network network = (Network) o;
        return Objects.equals(domains, network.domains) && Objects.equals(computers, network.computers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domains, computers);
    }
}
