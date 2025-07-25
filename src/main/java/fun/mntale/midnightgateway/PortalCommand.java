package fun.mntale.midnightgateway;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PortalCommand implements BasicCommand {

    @Override
    public void execute(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        CommandSender sender = commandSourceStack.getSender();

        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /mgw <create|delete|list|reload>", NamedTextColor.RED));
            return;
        }

        switch (args[0].toLowerCase()) {
            case "create" -> handleCreate(sender, args);
            case "delete" -> handleDelete(sender, args);
            case "list" -> handleList(sender);
            case "reload" -> handleReload(sender);
            default -> sender.sendMessage(Component.text("Usage: /mgw <create|delete|list|reload>", NamedTextColor.RED));
        }
    }

    private void handleCreate(CommandSender sender, String[] args) {
        if (args.length != 10) {
            sender.sendMessage(Component.text("Usage: /mgw create <name> <world> <x1> <y1> <z1> <x2> <y2> <z2> <destination>", NamedTextColor.RED));
            return;
        }

        String name = args[1];
        String world = args[2];
        int x1, y1, z1, x2, y2, z2;
        try {
            x1 = Integer.parseInt(args[3]);
            y1 = Integer.parseInt(args[4]);
            z1 = Integer.parseInt(args[5]);
            x2 = Integer.parseInt(args[6]);
            y2 = Integer.parseInt(args[7]);
            z2 = Integer.parseInt(args[8]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("Coordinates must be integers.", NamedTextColor.RED));
            return;
        }
        String destination = args[9];

        MidnightGateway.getInstance().getConfigManager().createPortal(name, world, x1, y1, z1, x2, y2, z2, destination);
        sender.sendMessage(Component.text("Portal '" + name + "' created.", NamedTextColor.GREEN));
    }

    private void handleDelete(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(Component.text("Usage: /mgw delete <name>", NamedTextColor.RED));
            return;
        }

        String name = args[1];
        MidnightGateway.getInstance().getConfigManager().deletePortal(name);
        sender.sendMessage(Component.text("Portal '" + name + "' deleted.", NamedTextColor.GREEN));
    }

    private void handleList(CommandSender sender) {
        sender.sendMessage(Component.text("Portals:", NamedTextColor.GOLD));
        for (String portalName : MidnightGateway.getInstance().getConfigManager().getPortals().keySet()) {
            sender.sendMessage(Component.text("- " + portalName, NamedTextColor.GRAY));
        }
    }

    private void handleReload(CommandSender sender) {
        MidnightGateway.getInstance().getConfigManager().load();
        sender.sendMessage(Component.text("MidnightGateway configuration reloaded.", NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(@NotNull CommandSourceStack commandSourceStack, @NotNull String[] args) {
        if (commandSourceStack.getSender() instanceof Player player) {
            if (args.length == 4 || args.length == 5 || args.length == 6) {
                Location targetBlock = player.getTargetBlock(null, 5).getLocation();
                if (args.length == 4) return List.of(String.valueOf(targetBlock.getBlockX()));
                if (args.length == 5) return List.of(String.valueOf(targetBlock.getBlockY()));
                if (args.length == 6) return List.of(String.valueOf(targetBlock.getBlockZ()));
            }
            if (args.length == 7 || args.length == 8 || args.length == 9) {
                Location targetBlock = player.getTargetBlock(null, 5).getLocation();
                if (args.length == 7) return List.of(String.valueOf(targetBlock.getBlockX()));
                if (args.length == 8) return List.of(String.valueOf(targetBlock.getBlockY()));
                if (args.length == 9) return List.of(String.valueOf(targetBlock.getBlockZ()));
            }
        }

        if (args.length == 1) {
            return List.of("create", "delete", "list", "reload");
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
            return new ArrayList<>(MidnightGateway.getInstance().getConfigManager().getPortals().keySet());
        }
        return List.of();
    }

    @Override
    public String permission() {
        return "midnightgateway.admin";
    }

    @Override
    public boolean canUse(@NotNull CommandSender sender) {
        return sender.hasPermission(permission());
    }
}