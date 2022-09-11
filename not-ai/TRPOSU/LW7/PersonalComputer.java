package by.shobik.lw4;

import java.util.Objects;

public class PersonalComputer extends Computer {

    private String userName;

    PersonalComputer(String name, String ip, String userName) {
        super(name, ip);
        setUserName(userName);
    }

    PersonalComputer(String name, Ip ip, String userName) {
        super(name, ip);
        setUserName(userName);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "PersonalComputer{" +
                "name='" + getName() + "'," +
                "ip='" + getIp() + "'," +
                "userName='" + userName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PersonalComputer that = (PersonalComputer) o;
        return Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userName);
    }
}
