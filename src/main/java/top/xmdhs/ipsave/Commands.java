package top.xmdhs.ipsave;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.xmdhs.ipsave.sql.sql;

import java.sql.SQLException;
import java.util.Set;

public class Commands implements CommandExecutor {
    private final sql sql;

    public Commands() {
        sql = Main.getInstance().getSql();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if (!p.hasPermission("ipsave.true")) {
                p.sendMessage("你没有权限");
                return false;
            }
        }
        if (strings.length != 1) {
            commandSender.sendMessage("/ipsave <name>");
            return false;
        }
        String name = strings[0];
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Set<String> ips = null;
            try {
                ips = sql.getByUUID(name);
            } catch (SQLException throwables) {
                commandSender.sendMessage("数据库读取出错");
                throwables.printStackTrace();
                return;
            }
            if (ips == null){
                commandSender.sendMessage("找不到这个玩家的任何记录");
                return;
            }
            try {
                for (String v : ips) {
                    Set<String> names = sql.getByIp(v);
                    commandSender.sendMessage(v + " 有以下玩家");
                    for (String n : names) {
                        commandSender.sendMessage(n);
                    }
                }
                commandSender.sendMessage("查询完毕");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        return true;
    }
}
