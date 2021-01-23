package top.xmdhs.ipsave;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import top.xmdhs.ipsave.event.Event;
import top.xmdhs.ipsave.event.EventAuthme;
import top.xmdhs.ipsave.sql.sql;
import top.xmdhs.ipsave.sql.sqlite;

import java.sql.SQLException;

public class Main extends JavaPlugin {
    private static Main that;
    private sql s;

    public sql getSql() {
        return s;
    }

    public static Main getInstance() {
        return that;
    }

    @Override
    public void onEnable() {
        that = this;
        getLogger().info("start");
        saveDefaultConfig();

        try {
            s = new sqlite(Main.getInstance().getDataFolder().getPath());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return;
        }

        Plugin p = Bukkit.getPluginManager().getPlugin("AuthMe");
        if (p != null) {
            Bukkit.getPluginManager().registerEvents(new EventAuthme(s), this);
        } else {
            Bukkit.getPluginManager().registerEvents(new Event(s), this);
        }
        PluginCommand comm = Bukkit.getPluginCommand("ipsave");
        assert comm != null;
        comm.setExecutor(new Commands());
    }
}
