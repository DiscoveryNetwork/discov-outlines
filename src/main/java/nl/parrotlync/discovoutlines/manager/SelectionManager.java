package nl.parrotlync.discovoutlines.manager;

import nl.parrotlync.discovoutlines.model.ReferencePoint;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class SelectionManager {
    private final HashMap<UUID, ReferencePoint> selections = new HashMap<>();

    public boolean hasSelection(Player player) {
        return selections.containsKey(player.getUniqueId());
    }

    public void select(Player player, ReferencePoint point) {
        selections.put(player.getUniqueId(), point);
    }

    public ReferencePoint getSelection(Player player) {
        return selections.get(player.getUniqueId());
    }
}
