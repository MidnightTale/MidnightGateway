package fun.mntale.midnightgateway;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationManager {

    private final Map<String, Portal> portals = new HashMap<>();

    public void load() {
        MidnightGateway.getInstance().saveDefaultConfig();
        MidnightGateway.getInstance().reloadConfig();
        FileConfiguration config = MidnightGateway.getInstance().getConfig();
        portals.clear();

        ConfigurationSection portalsSection = config.getConfigurationSection("portals");
        if (portalsSection != null) {
            for (String portalName : portalsSection.getKeys(false)) {
                ConfigurationSection portalSection = portalsSection.getConfigurationSection(portalName);
                if (portalSection != null) {
                    String world = portalSection.getString("world");
                    ConfigurationSection pos1Section = portalSection.getConfigurationSection("pos1");
                    ConfigurationSection pos2Section = portalSection.getConfigurationSection("pos2");
                    String destination = portalSection.getString("destination");

                    if (world != null && pos1Section != null && pos2Section != null && destination != null) {
                        portals.put(portalName, new Portal(
                                world,
                                pos1Section.getInt("x"),
                                pos1Section.getInt("y"),
                                pos1Section.getInt("z"),
                                pos2Section.getInt("x"),
                                pos2Section.getInt("y"),
                                pos2Section.getInt("z"),
                                destination
                        ));
                    }
                }
            }
        }
    }

    public Map<String, Portal> getPortals() {
        return portals;
    }

    public void createPortal(String name, String world, int x1, int y1, int z1, int x2, int y2, int z2, String destination) {
        FileConfiguration config = MidnightGateway.getInstance().getConfig();
        String path = "portals." + name;
        config.set(path + ".world", world);
        config.set(path + ".pos1.x", x1);
        config.set(path + ".pos1.y", y1);
        config.set(path + ".pos1.z", z1);
        config.set(path + ".pos2.x", x2);
        config.set(path + ".pos2.y", y2);
        config.set(path + ".pos2.z", z2);
        config.set(path + ".destination", destination);
        MidnightGateway.getInstance().saveConfig();
        load();
    }

    public void deletePortal(String name) {
        FileConfiguration config = MidnightGateway.getInstance().getConfig();
        config.set("portals." + name, null);
        MidnightGateway.getInstance().saveConfig();
        load();
    }
}