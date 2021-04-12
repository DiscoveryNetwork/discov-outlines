package nl.parrotlync.discovoutlines.manager;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import nl.parrotlync.discovoutlines.DiscovOutlines;
import nl.parrotlync.discovoutlines.model.ReferencePoint;
import nl.parrotlync.discovoutlines.util.DataUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReferenceManager {
    private final HashMap<String, ReferencePoint> referencePoints = new HashMap<>();
    private final HashMap<String, Hologram> holograms = new HashMap<>();

    public void addPoint(String name, Location location) {
        ReferencePoint point = new ReferencePoint(name, location);
        createHologram(point);
        referencePoints.put(name.toLowerCase(), point);
    }

    public boolean removePoint(String name) {
        if (referencePoints.containsKey(name.toLowerCase())) {
            removeHologram(referencePoints.get(name.toLowerCase()));
            referencePoints.remove(name.toLowerCase());
            return true;
        }
        return false;
    }

    public ReferencePoint getPoint(String name) {
        return referencePoints.get(name.toLowerCase());
    }

    public List<ReferencePoint> getPoints() {
        return new ArrayList<>(referencePoints.values());
    }

    public void load() {
        HashMap<String, ReferencePoint> storedPoints = DataUtil.loadObjectFromPath("plugins/DiscovOutlines/points.data");
        if (storedPoints != null) {
            for (ReferencePoint point : storedPoints.values()) {
                referencePoints.put(point.getName().toLowerCase(), point);
                createHologram(point);
            }
        }
        DiscovOutlines.getInstance().getLogger().info("DiscovOutlines loaded with " + referencePoints.size() + " points.");
    }

    public void save() {
        for (ReferencePoint point : referencePoints.values()) {
            removeHologram(point);
        }
        DataUtil.saveObjectToPath(referencePoints, "plugins/DiscovOutlines/points.data");
        DiscovOutlines.getInstance().getLogger().info("Saved " + referencePoints.size() + " to file.");
    }

    private void createHologram(ReferencePoint point) {
        final Hologram hologram = HologramsAPI.createHologram(DiscovOutlines.getInstance(), point.getLocation().add(0, 2, 0));
        hologram.appendTextLine("§c§lReference Point").setTouchHandler(getTouchHandler(hologram));
        hologram.appendTextLine("§7" + point.getName()).setTouchHandler(getTouchHandler(hologram));
        hologram.getVisibilityManager().setVisibleByDefault(false);
        hologram.getVisibilityManager().resetVisibilityAll();
        holograms.put(point.getName().toLowerCase(), hologram);
    }

    private void removeHologram(ReferencePoint point) {
        holograms.get(point.getName().toLowerCase()).delete();
        holograms.remove(point.getName().toLowerCase());
    }

    private TouchHandler getTouchHandler(Hologram hologram) {
        return player -> {
            String name = ChatColor.stripColor(((TextLine) hologram.getLine(1)).getText());
            ReferencePoint point = getPoint(name);
            DiscovOutlines.getInstance().getSelectionManager().select(player, point);
            player.sendMessage("§7Selected the point §6" + point.getName());
        };
    }
}
