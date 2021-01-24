package top.xmdhs.ipsave.sql;

import top.xmdhs.ipsave.event.print;

import java.sql.SQLException;
import java.util.Set;

public class GetNames implements Runnable {
    private final String name;
    private final print p;
    private final sql s;

    public GetNames(String name, print p, sql s) {
        this.name = name;
        this.p = p;
        this.s = s;
    }

    @Override
    public void run() {
        Set<String> ips;
        try {
            ips = s.getByUUID(name);
        } catch (SQLException throwables) {
            p.toprint("数据库读取出错");
            throwables.printStackTrace();
            return;
        }
        if (ips == null) {
            p.toprint("找不到这个玩家的任何记录");
            return;
        }
        try {
            for (String v : ips) {
                Set<String> names = s.getByIp(v);
                p.toprint(v + " 有以下玩家");
                for (String n : names) {
                    p.toprint(n);
                }
            }
            p.toprint("查询完毕");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
