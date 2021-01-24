package top.xmdhs.ipsave.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import top.xmdhs.ipsave.Main;
import top.xmdhs.ipsave.sql.sql;

import java.net.InetSocketAddress;

public class Event implements Listener {
    private final sql s;
    private final Main main;
    private final print p;

    public Event(sql s) {
        this.s = s;
        main = Main.getInstance();
        p = (msg) -> {
            main.getLogger().warning(msg);
        };
    }

    @EventHandler
    public void onPlayerMove(PlayerJoinEvent e) {
        String[] list = getnameip(e.getPlayer());
        if (list == null) {
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Join.join(list[0], list[1], s, p);
        });
    }

    static String[] getnameip(Player p) {
        String name = p.getName();
        InetSocketAddress i = p.getAddress();
        if (i == null) {
            Main.getInstance().getLogger().warning("无法获取 " + name + " 的 ip");
            return null;
        }
        String ip = i.getAddress().getHostAddress();
        return new String[] { name, ip };
    }
}
