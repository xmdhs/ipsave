package top.xmdhs.ipsave;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.xmdhs.ipsave.sql.GetNames;
import top.xmdhs.ipsave.sql.sql;

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
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(),
                new GetNames(name, commandSender::sendMessage, sql));
        return true;
    }
}
