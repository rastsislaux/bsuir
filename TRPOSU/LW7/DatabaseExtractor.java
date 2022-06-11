package by.shobik.lw4;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DatabaseExtractor {

    private Connection con;

    private PreparedStatement getServersStmt;
    private PreparedStatement getDomainsStmt;
    private PreparedStatement getPcsStmt;

    public DatabaseExtractor(Connection con) throws SQLException {
        this.con = con;
        getServersStmt = con.prepareStatement("SELECT * FROM `servers`");
        getDomainsStmt = con.prepareStatement("SELECT * FROM `domains`");
        getPcsStmt = con.prepareStatement("SELECT * FROM `computers`");
    }

    public List<Domain> getDomains() throws SQLException {
        List<Domain> domainList = new ArrayList<>();
        ResultSet rs = getDomainsStmt.executeQuery();
        while (rs.next()) {
            domainList.add(
                    new Domain(rs.getString(2), rs.getString(1))
            );
        }
        rs.close();
        return domainList;
    }

    public List<Computer> getComputers() throws SQLException {
        List<Computer> computerList = new ArrayList<>();
        ResultSet rs = getServersStmt.executeQuery();
        while (rs.next()) {
            computerList.add(
                    new Server(
                            rs.getString(1),
                            rs.getString(2),
                            Arrays.stream(rs.getString(3).split(",")).toList()
                    )
            );
        }
        rs = getPcsStmt.executeQuery();
        while (rs.next()) {
            computerList.add(
                    new Computer(
                            rs.getString(1),
                            rs.getString(2)
                    )
            );
        }
        rs.close();
        return computerList;
    }

}
