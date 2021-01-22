package nl.parrotlync.discovoutlines.manager;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import nl.parrotlync.discovoutlines.DiscovOutlines;
import nl.parrotlync.discovoutlines.model.ReferencePoint;
import nl.parrotlync.discovoutlines.util.DataUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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
        return referencePoints.get(name);
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
        DataUtil.saveObjectToPath(referencePoints, "plugins/DiscovOutlines/points.data");
        DiscovOutlines.getInstance().getLogger().info("Saved " + referencePoints.size() + " to file.");
    }

    private void createHologram(ReferencePoint point) {
        Hologram hologram = HologramsAPI.createHologram(DiscovOutlines.getInstance(), point.getLocation().add(0, 2, 0));
        hologram.appendTextLine("§c§lReference Point");
        hologram.appendTextLine("§7" + point.getName());
        hologram.getVisibilityManager().setVisibleByDefault(false);
        holograms.put(point.getName().toLowerCase(), hologram);
    }

    private void removeHologram(ReferencePoint point) {
        holograms.remove(point.getName().toLowerCase());
    }

    public void showHolograms(Player player) {
        for (Hologram hologram : holograms.values()) {
            hologram.getVisibilityManager().showTo(player);
        }
    }

    public void hideHolograms(Player player) {
        for (Hologram hologram : holograms.values()) {
            hologram.getVisibilityManager().hideTo(player);
        }
    }
}
