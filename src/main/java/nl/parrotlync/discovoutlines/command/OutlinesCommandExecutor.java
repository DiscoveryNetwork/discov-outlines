package nl.parrotlync.discovoutlines.command;

import nl.parrotlync.discovoutlines.DiscovOutlines;
import nl.parrotlync.discovoutlines.model.ReferencePoint;
import nl.parrotlync.discovoutlines.util.ChatUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class OutlinesCommandExecutor implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                String version = DiscovOutlines.getInstance().getDescription().getVersion();
                ChatUtil.sendMessage(sender, "§6DiscovOutlines-1.12.2-v" + version + " §7- Use /outlines help", false);
                return true;
            }

            if (args[0].equalsIgnoreCase("ref") || args[0].equalsIgnoreCase("reference") && args.length > 1) {
                if (args[1].equalsIgnoreCase("add") && args.length == 3) {
                    DiscovOutlines.getInstance().getReferenceManager().addPoint(args[2], player.getLocation());
                    ChatUtil.sendMessage(player, "§7Added the point §a" + args[2] + " §7at your current location.", true);
                    return true;
                }

                if (args[1].equalsIgnoreCase("remove") && args.length == 3) {
                    if (DiscovOutlines.getInstance().getReferenceManager().removePoint(args[2])) {
                        ChatUtil.sendMessage(player, "§7Removed the point §c" + args[2], true);
                    } else {
                        ChatUtil.sendMessage(player, "§cThat reference point doesn't exist!", true);
                    }
                    return true;
                }

                if (args[1].equalsIgnoreCase("list")) {
                    List<ReferencePoint> points = DiscovOutlines.getInstance().getReferenceManager().getPoints();
                    int pages = (int) Math.ceil((points.size() / 5) + 1);
                    int page = 1;

                    if (args.length == 3) {
                        try {
                            page = Integer.parseInt(args[2]);
                            if (page > pages) { page = 1; }
                        } catch (NumberFormatException e) {
                            ChatUtil.sendMessage(player, "§cThat isn't a valid page number!", true);
                        }
                    }

                    ChatUtil.sendMessage(player, "§f+---+ §aReference Points List §7(page " + page + ") §f+---+", false);
                    for (int i = (page - 1) * 5; i < page * 5 && i < points.size(); i++) {
                        Location loc = points.get(i).getLocation();
                        ChatUtil.sendMessage(player, "§6" + points.get(i).getName() + " §7(§3" + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "§7)", false);
                    }
                    if (page < pages) {
                        ChatUtil.sendMessage(sender, "&3Type '/outlines reference list " + (page + 1) + "' to see more.", false);
                    }
                    return true;
                }

                if (args[1].equalsIgnoreCase("select") && args.length == 3) {
                    ReferencePoint point = DiscovOutlines.getInstance().getReferenceManager().getPoint(args[2]);
                    if (point != null) {
                        DiscovOutlines.getInstance().getSelectionManager().select(player, point);
                        ChatUtil.sendMessage(player, "§7Selected the point §6" + point.getName(), true);
                    } else {
                        ChatUtil.sendMessage(player, "§cThat reference point doesn't exist!", true);
                    }
                    return true;
                }
            }

            if (args[0].equalsIgnoreCase("place") && args.length == 3) {
                if (DiscovOutlines.getInstance().getSelectionManager().hasSelection(player)) {
                    Location pointLocation = DiscovOutlines.getInstance().getSelectionManager().getSelection(player).getLocation();

                    double length = Double.parseDouble(args[1]);
                    double heading = Double.parseDouble(args[2]);

                    double radHeading = heading / 180.0D * 3.141592653589793D;
                    double preRoundX = length * Math.sin(radHeading);
                    double preRoundZ = length * Math.cos(radHeading);
                    double postRoundX = (double) Math.round(preRoundX);
                    double postRoundZ = (double) Math.round(preRoundZ);
                    int changeX = (int) postRoundX;
                    int changeZ = (int) postRoundZ;

                    Location block = new Location(pointLocation.getWorld(), (pointLocation.getX() + changeX), player.getLocation().getY(), (pointLocation.getZ() - changeZ));
                    block.getBlock().setType(Material.GOLD_BLOCK);
                    ChatUtil.sendMessage(player, "§7Placed block at: §3" + block.getBlockX() + " " + player.getLocation().getBlockY() + " " + block.getBlockZ(), false);
                } else {
                    ChatUtil.sendMessage(player, "§cYou don't have a reference point selected!", true);
                }
                return true;
            }

            return help(sender);
        } else {
            ChatUtil.sendMessage(sender, "§cYou need to be a player to use this command.", true);
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            suggestions.add("reference");
            suggestions.add("place");
            StringUtil.copyPartialMatches(args[0], suggestions, new ArrayList<>());
        }

        if (args[0].equalsIgnoreCase("ref") || args[0].equalsIgnoreCase("reference")) {
            if (args.length == 2) {
                suggestions.add("add");
                suggestions.add("remove");
                suggestions.add("select");
                suggestions.add("list");
                StringUtil.copyPartialMatches(args[1], suggestions, new ArrayList<>());
            }

            if (args.length == 3 && (args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("select"))) {
                for (ReferencePoint point : DiscovOutlines.getInstance().getReferenceManager().getPoints()) {
                    suggestions.add(point.getName());
                }
                StringUtil.copyPartialMatches(args[2], suggestions, new ArrayList<>());
            }
        }

        return suggestions;
    }

    public boolean help(CommandSender sender) {
        if (sender.hasPermission("discovoutlines.use")) {
            ChatUtil.sendMessage(sender, "§f+---+ §9DiscovOutlines §f+---+", false);
            ChatUtil.sendMessage(sender, "§3/outlines reference add <name> §7Create a reference point at your location", false);
            ChatUtil.sendMessage(sender, "§3/outlines reference remove <name> §7Remove a reference point", false);
            ChatUtil.sendMessage(sender, "§3/outlines reference select <name> §7Select a reference point", false);
            ChatUtil.sendMessage(sender, "§3/outlines reference list §7List all reference points", false);
            ChatUtil.sendMessage(sender, "§3/outlines place <length> <heading> §7Place a block at ", false);
        } else {
            ChatUtil.sendMessage(sender, "§cYou do not have permission to do that!", true);
        }
        return true;
    }
}
