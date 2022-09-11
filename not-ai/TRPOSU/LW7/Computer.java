package by.shobik.lw4;

import java.util.Objects;
import java.util.regex.Pattern;

public class Computer {

    private String name;

    private Ip ip;

    Computer(String name, String ip) {
        setName(name);
        setIp(ip);
    }

    Computer(String name, Ip ip) {
        setName(name);
        setIp(ip);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Ip getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = new Ip(ip);
    }

    public void setIp(Ip ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "Computer{" +
                "name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Computer computer = (Computer) o;
        return Objects.equals(name, computer.name) && Objects.equals(ip, computer.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ip);
    }
}
