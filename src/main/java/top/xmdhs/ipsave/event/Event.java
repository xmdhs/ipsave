package top.xmdhs.ipsave.event;

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

    public Event(sql s) {
        this.s = s;
        main = Main.getInstance();
    }

    @EventHandler
    public void onPlayerMove(PlayerJoinEvent e) {
        join(e.getPlayer(), s, main);
    }

    static void join(Player player, sql s, Main main) {
        InetSocketAddress i = player.getAddress();
        if (i != null) {
            try {
                s.add(player.getName(), i.getAddress().getHostAddress());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else {
            main.getLogger().warning("无法获取 " + player.getName() + " 的 ip");
        }
    }
}
