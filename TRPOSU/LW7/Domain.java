package by.shobik.lw4;

import java.util.Objects;

public class Domain {

    private String name;

    private Ip correspondingIp;

    public Domain(String name, String ip) {
        setName(name);
        setCorrespondingIp(ip);
    }

    public Domain(String name, Ip ip) {
        setName(name);
        setCorrespondingIp(ip);
    }

    public Ip getCorrespondingIp() {
        return correspondingIp;
    }

    public void setCorrespondingIp(String correspondingIp) {
        this.correspondingIp = new Ip(correspondingIp);
    }

    public void setCorrespondingIp(Ip correspondingIp) {
        this.correspondingIp = correspondingIp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Domain{" +
                "name='" + name + '\'' +
                ", correspondingIp=" + correspondingIp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Domain domain = (Domain) o;
        return Objects.equals(name, domain.name) && Objects.equals(correspondingIp, domain.correspondingIp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, correspondingIp);
    }
}
