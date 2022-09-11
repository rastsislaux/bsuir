package com.company;

import java.util.Objects;
import java.util.regex.Pattern;

public class Ip {

    private String address;

    public Ip(String address) {
        setAddress(address);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (!ipPattern.matcher(address).matches()) {
            throw new IllegalArgumentException("Not a valid IP address");
        }
        this.address = address;
    }

    private static final String ipRegex = "([0-9]{1,3}[\\.]){3}[0-9]{1,3}";

    private static final Pattern ipPattern = Pattern.compile(ipRegex);

    @Override
    public String toString() {
        return "Ip{" +
                "address='" + address + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ip ip = (Ip) o;
        return Objects.equals(address, ip.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
