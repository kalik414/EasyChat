package dev.kali;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class easychat extends JavaPlugin implements Listener {

    private static final int LOCAL_CHAT_RADIUS = 50; // !!! Radius for local chat !!!

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("ChatPlugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ChatPlugin disabled!");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player sender = event.getPlayer();
        String message = event.getMessage();
        Location senderLocation = sender.getLocation();

        String dimensionColor = getDimensionColor(sender);
        String formattedMessage;

        if (message.startsWith("!")) {
            formattedMessage = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "G" + ChatColor.DARK_GRAY + "] "
                    + dimensionColor + sender.getName()
                    + ChatColor.DARK_GRAY + " » " + ChatColor.WHITE + message.substring(1).trim();

            event.setCancelled(true);
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(formattedMessage);
            }
        } else {
            formattedMessage = ChatColor.DARK_GRAY + "[" + ChatColor.RED + "L" + ChatColor.DARK_GRAY + "] "
                    + dimensionColor + sender.getName()
                    + ChatColor.DARK_GRAY + " » " + ChatColor.WHITE + message.trim();

            event.setCancelled(true);
            boolean heard = false;
            for (Player player : sender.getWorld().getPlayers()) {
                if (player.getLocation().distance(senderLocation) <= LOCAL_CHAT_RADIUS) {
                    player.sendMessage(formattedMessage);
                    heard = true;
                }
            }

            if (!heard) {
                sender.sendMessage(ChatColor.GRAY + "Nobody heard you.");
            }
        }
    }

    private String getDimensionColor(Player player) {
        return switch (player.getWorld().getEnvironment()) {
            case NORMAL -> ChatColor.GREEN.toString();
            case NETHER -> ChatColor.RED.toString();
            case THE_END -> ChatColor.LIGHT_PURPLE.toString();
            default -> ChatColor.GRAY.toString();
        };
    }
}
