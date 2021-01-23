package top.xmdhs.ipsave.event;

import fr.xephi.authme.events.LoginEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import top.xmdhs.ipsave.Main;
import top.xmdhs.ipsave.sql.sql;

public class EventAuthme implements Listener {
    private final sql s;
    private final Main main;

    public EventAuthme(sql s) {
        this.s = s;
        main = Main.getInstance();
    }

    @EventHandler
    public void onPlayerMove(LoginEvent e) {
        Event.join(e.getPlayer(), s, main);
    }
}
