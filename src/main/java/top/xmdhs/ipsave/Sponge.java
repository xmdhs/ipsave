package top.xmdhs.ipsave;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.network.PlayerConnection;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import top.xmdhs.ipsave.event.Join;
import top.xmdhs.ipsave.sql.GetNames;
import top.xmdhs.ipsave.sql.sql;
import top.xmdhs.ipsave.sql.sqlite;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

@Plugin(id = "ipsave", name = "save ip", version = "0.0.1", description = "save player ip")
public class Sponge implements CommandExecutor {
    @Inject
    private Logger logger;

    private final Task.Builder taskBuilder = Task.builder();

    private Path configDir;

    @Inject
    private void setConfigDir(@ConfigDir(sharedRoot = false) Path configDir) {
        this.configDir = configDir;
    }

    private sql s;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        try {
            logger.info(configDir.toString());
            Files.createDirectories(configDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            s = new sqlite(configDir.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return;
        }
        CommandSpec myCommandSpec = CommandSpec.builder()
                .description(Text.of("查看玩家可能的小号"))
                .permission("ipsave.true")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("player")))
                )
                .executor((CommandSource src, CommandContext args) ->{
                    String name = args.<String>getOne("player").get();
                    taskBuilder.execute(new GetNames(name,(msg)->{
                        src.sendMessage(Text.of(msg));
                    },s)).async().submit(this);
                    return CommandResult.success();
                })
                .build();
        org.spongepowered.api.Sponge.getCommandManager().register(this, myCommandSpec, "ipsave");
        logger.info("start");
    }

    @Listener
    public void onJoin(ClientConnectionEvent.Join e) {
        PlayerConnection c = e.getTargetEntity().getConnection();
        String ip = c.getAddress().getAddress().getHostAddress();
        String name = c.getPlayer().getName();
        taskBuilder.execute(() -> {
            Join.join(name,ip,s,(msg)->{
                logger.warn(msg);
            });
        }).async().submit(this);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        return null;
    }
}
