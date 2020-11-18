package nl.parrotlync.discovoutlines.manager;

import nl.parrotlync.discovoutlines.DiscovOutlines;
import nl.parrotlync.discovoutlines.model.ReferencePoint;
import nl.parrotlync.discovoutlines.util.DataUtil;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReferenceManager {
    private HashMap<String, ReferencePoint> referencePoints;

    public void addPoint(String name, Location location) {
        referencePoints.put(name, new ReferencePoint(name, location));
    }

    public boolean removePoint(String name) {
        if (referencePoints.containsKey(name)) {
            referencePoints.remove(name);
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
        referencePoints = DataUtil.loadObjectFromPath("plugins/DiscovOutlines/points.data");
        if (referencePoints == null) {
            referencePoints = new HashMap<>();
        }
        DiscovOutlines.getInstance().getLogger().info("DiscovOutlines loaded with " + referencePoints.size() + " points.");
    }

    public void save() {
        DataUtil.saveObjectToPath(referencePoints, "plugins/DiscovOutlines/points.data");
        DiscovOutlines.getInstance().getLogger().info("Saved " + referencePoints.size() + " to file.");
    }
}
