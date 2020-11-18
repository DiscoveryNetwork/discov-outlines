package nl.parrotlync.discovoutlines.util;

import nl.parrotlync.discovoutlines.DiscovOutlines;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ChatUtil {

    public static void sendMessage(CommandSender sender, String msg, boolean withPrefix) {
        if (withPrefix) {
            String prefix = DiscovOutlines.getInstance().getConfig().getString("prefix");
            msg = ChatColor.translateAlternateColorCodes('&', prefix) + msg;
        }
        sender.sendMessage(msg);
    }
}
