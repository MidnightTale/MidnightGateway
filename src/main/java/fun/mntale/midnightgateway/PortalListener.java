package fun.mntale.midnightgateway;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PortalListener implements Listener {

    private final Map<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();

        if (to == null) {
            return;
        }

        if (cooldowns.containsKey(player.getUniqueId()) && System.currentTimeMillis() - cooldowns.get(player.getUniqueId()) < 3000) {
            return;
        }

        for (Portal portal : MidnightGateway.getInstance().getConfigManager().getPortals().values()) {
            if (isInPortal(to, portal)) {
                cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
                sendPlayerToServer(player, portal.destination());
                return;
            }
        }
    }

    private boolean isInPortal(Location location, Portal portal) {
        if (!location.getWorld().getName().equals(portal.world())) {
            return false;
        }

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        return x >= Math.min(portal.x1(), portal.x2()) && x <= Math.max(portal.x1(), portal.x2()) &&
               y >= Math.min(portal.y1(), portal.y2()) && y <= Math.max(portal.y1(), portal.y2()) &&
               z >= Math.min(portal.z1(), portal.z2()) && z <= Math.max(portal.z1(), portal.z2());
    }

    private void sendPlayerToServer(Player player, String serverName) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("Connect");
            out.writeUTF(serverName);
            player.sendPluginMessage(MidnightGateway.getInstance(), "BungeeCord", b.toByteArray());
        } catch (IOException e) {
            player.sendMessage("Error while connecting to the server.");
            Vector knockback = player.getLocation().getDirection().multiply(-1).setY(0.5);
            player.setVelocity(knockback);
            e.printStackTrace();
        }
    }
}