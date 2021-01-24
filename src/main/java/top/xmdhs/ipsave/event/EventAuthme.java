package top.xmdhs.ipsave.event;

import fr.xephi.authme.events.LoginEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import top.xmdhs.ipsave.Main;
import top.xmdhs.ipsave.sql.sql;

public class EventAuthme implements Listener {
    private final sql s;
    private final Main main;
    private final print p;

    public EventAuthme(sql s) {
        this.s = s;
        main = Main.getInstance();
        p = (msg) -> {
            main.getLogger().warning(msg);
        };
    }

    @EventHandler
    public void onPlayerMove(LoginEvent e) {
        String[] list = Event.getnameip(e.getPlayer());
        if (list == null) {
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Join.join(list[0], list[1], s, p);
        });
    }
}
