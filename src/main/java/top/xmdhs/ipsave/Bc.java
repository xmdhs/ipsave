package top.xmdhs.ipsave;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import top.xmdhs.ipsave.event.Join;
import top.xmdhs.ipsave.sql.GetNames;
import top.xmdhs.ipsave.sql.sql;
import top.xmdhs.ipsave.sql.sqlite;

import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bc extends Plugin implements Listener {
    private sql s;
    private static final ExecutorService exec = Executors.newCachedThreadPool();

    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        try {
            Class.forName("org.sqlite.JDBC");
            s = new sqlite(getDataFolder().getPath());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
            return;
        }
        getLogger().info("Start");
        getProxy().getPluginManager().registerListener(this, this);
        getProxy().getPluginManager().registerCommand(this, new commd(exec, s));
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        PendingConnection c = event.getConnection();
        String name = c.getName();
        exec.execute(() -> {
            InetSocketAddress i = (InetSocketAddress) c.getSocketAddress();
            Join.join(name, i.getAddress().getHostAddress(), s, (msg) -> {
                getLogger().warning(msg);
            });
        });
    }
}

class commd extends Command {
    public commd(ExecutorService exec, sql s) {
        super("ipsave", "ipsave.true");
        this.s = s;
        this.exec = exec;
    }

    private final ExecutorService exec;
    private final sql s;

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if ((commandSender instanceof ProxiedPlayer)) {
            ProxiedPlayer p = (ProxiedPlayer) commandSender;
            if (!p.hasPermission("ipsave.true")) {
                commandSender.sendMessage(new TextComponent("你没有权限"));
                return;
            }
        }
        if (strings.length != 1) {
            commandSender.sendMessage(new TextComponent("/ipsave <name>"));
            return;
        }
        String name = strings[0];
        exec.execute(new GetNames(name, (msg) -> {
            commandSender.sendMessage(new TextComponent(msg));
        }, s));
    }
}