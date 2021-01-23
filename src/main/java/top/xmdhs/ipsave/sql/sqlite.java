package top.xmdhs.ipsave.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class sqlite implements sql {
    private final DataSource ds;
    private final String path;

    public sqlite(String path) throws SQLException {
        HikariConfig config = new HikariConfig();
        this.path = path;
        config.setJdbcUrl("jdbc:sqlite:" + path + File.separator + "sql.db");
        config.addDataSourceProperty("connectionTimeout", "1000");
        config.addDataSourceProperty("idleTimeout", "60000");
        config.addDataSourceProperty("maximumPoolSize", "10");
        ds = new HikariDataSource(config);
        try (Connection c = ds.getConnection()) {
            try (Statement stmt = c.createStatement()) {
                c.setAutoCommit(false);
                stmt.execute("CREATE TABLE IF NOT EXISTS IP(ID TEXT PRIMARY KEY NOT NULL,JSON TEXT NOT NULL);");
                stmt.execute("CREATE TABLE IF NOT EXISTS NAME(ID TEXT PRIMARY KEY NOT NULL,JSON TEXT NOT NULL);");
                c.commit();
            }
        }
    }


    @Override
    public void add(String uuid, String ip) throws SQLException {
        sqlset("NAME", uuid, setAddAnd2json("NAME", uuid, ip));
        sqlset("IP", ip, setAddAnd2json("IP", ip, uuid));
    }

    private String setAddAnd2json(String table, String id, String v) throws SQLException {
        String j = sqlget(table, id);
        set s = json.toSet(j);
        Set<String> Set = null;
        if (s == null) {
            Set = new HashSet<>();
        } else {
            Set = s.set;
        }
        Set.add(v);
        set ss = new set(Set);
        return json.toJson(ss);
    }

    private String sqlget(String table, String id) throws SQLException {
        try (Connection c = ds.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement("SELECT ID,JSON FROM " + table + " WHERE ID = ?")) {
                ps.setObject(1, id);
                try (ResultSet r = ps.executeQuery();) {
                    if (r.next()) {
                        return r.getString("JSON");
                    } else {
                        return "";
                    }
                }
            }
        }
    }

    @Override
    public Set<String> getByUUID(String UUID) throws SQLException {
        String j = sqlget("NAME", UUID);
        set s = json.toSet(j);
        if (s == null) {
            return null;
        }
        return s.set;
    }

    @Override
    public Set<String> getByIp(String ip) throws SQLException {
        String j = sqlget("IP", ip);
        set s = json.toSet(j);
        if (s == null) {
            return null;
        }
        return s.set;
    }

    private void sqlset(String table, String v1, String v2) throws SQLException {
        try (Connection c = ds.getConnection()) {
            try {
                try (PreparedStatement ps = c.prepareStatement("INSERT INTO " + table + " VALUES(?,?);")) {
                    c.setAutoCommit(false);
                    ps.setObject(1, v1);
                    ps.setObject(2, v2);
                    ps.executeUpdate();
                    c.commit();
                }
            } catch (SQLException e) {
                try (PreparedStatement ps = c.prepareStatement("UPDATE " + table + " SET JSON = ? WHERE ID = ?;")) {
                    c.setAutoCommit(false);
                    ps.setObject(1, v2);
                    ps.setObject(2, v1);
                    ps.executeUpdate();
                    c.commit();
                }
            }
        }
    }
}
