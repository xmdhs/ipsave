package top.xmdhs.ipsave.sql;

import java.sql.SQLException;
import java.util.Set;

public interface sql {
    void add(String uuid, String ip) throws SQLException;

    Set<String> getByUUID(String UUID) throws SQLException;

    Set<String> getByIp(String ip) throws SQLException;
}
