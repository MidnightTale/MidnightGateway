package fun.mntale.midnightgateway;

import io.papermc.paper.command.brigadier.BasicCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class MidnightGateway extends JavaPlugin {

    private static MidnightGateway instance;
    private ConfigurationManager configManager;

    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigurationManager();
        configManager.load();

        getServer().getPluginManager().registerEvents(new PortalListener(), this);
        BasicCommand PortalCommand = new PortalCommand();
        registerCommand("mgw", PortalCommand);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MidnightGateway getInstance() {
        return instance;
    }

    public ConfigurationManager getConfigManager() {
        return configManager;
    }
}
