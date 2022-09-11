package by.shobik.lw4;

import java.util.List;
import java.util.Objects;

public class Server extends Computer {

    private List<String> content;

    Server(String name, String ip, List<String> content) {
        super(name, ip);
        this.content = content;
    }

    Server(String name, Ip ip, List<String> content) {
        super(name, ip);
        this.content = content;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Server{" +
                "name=`" + getName() + "`," +
                "ip=" + getIp() + ',' +
                "content=" + content +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Server server = (Server) o;
        return Objects.equals(content, server.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), content);
    }
}
