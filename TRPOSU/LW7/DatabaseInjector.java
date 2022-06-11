package by.shobik.lw4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseInjector {

    private Connection con;

    private PreparedStatement injectServerStmt;
    private PreparedStatement injectPcStmt;
    private PreparedStatement injectDomainStmt;

    DatabaseInjector(Connection con) throws SQLException {
        this.con = con;
        injectServerStmt = con.prepareStatement(
                "INSERT INTO `servers` (`name`, `ip`, `pages`) VALUES (?, ?, ?);"
        );
        injectPcStmt = con.prepareStatement(
                "INSERT INTO `computers` (`name`, `ip`, `username`) VALUES (?, ?, ?)"
        );
        injectDomainStmt = con.prepareStatement(
                "INSERT INTO `domains` (`IP`, `Domain`) VALUES (?, ?)"
        );
    }

    public void injectDomains(List<Domain> toInject) throws SQLException {
        for (Domain domain : toInject) {
            injectDomainStmt.setString(2, domain.getName());
            injectDomainStmt.setString(1, domain.getCorrespondingIp().getAddress());
            injectDomainStmt.execute();
        }
    }

    public void injectServers(List<Server> toInject) throws SQLException {
        for (Server server : toInject) {
            injectServerStmt.setString(1, server.getName());
            injectServerStmt.setString(2, server.getIp().getAddress());
            injectServerStmt.setString(3, server.getContent().stream().collect(Collectors.joining(",")));
            injectServerStmt.execute();
        }
    }

    public void injectPcs(List<PersonalComputer> pcs) throws SQLException {
        for (PersonalComputer pc : pcs) {
            injectPcStmt.setString(1, pc.getName());
            injectPcStmt.setString(2, pc.getIp().getAddress());
            injectPcStmt.setString(3, pc.getUserName());
            injectPcStmt.execute();
        }
    }

    public void injectAll(List<Domain> domains,
                          List<Server> servers,
                          List<PersonalComputer> pcs) throws SQLException {
        injectDomains(domains);
        injectPcs(pcs);
        injectServers(servers);
    }

}
