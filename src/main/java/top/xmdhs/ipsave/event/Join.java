package top.xmdhs.ipsave.event;

import top.xmdhs.ipsave.sql.sql;

public class Join {
    public static void join(String name, String ip, sql s, print p) {
        try {
            s.add(name, ip);
        } catch (Exception exception) {
            p.toprint("数据库无法写入");
            exception.printStackTrace();
        }
    }
}
