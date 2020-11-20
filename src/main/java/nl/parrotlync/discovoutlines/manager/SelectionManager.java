package nl.parrotlync.discovoutlines.manager;

import nl.parrotlync.discovoutlines.DiscovOutlines;
import nl.parrotlync.discovoutlines.model.ReferencePoint;
import nl.parrotlync.discovoutlines.task.ParticleTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class SelectionManager {
    private final HashMap<UUID, ReferencePoint> selections = new HashMap<>();
    private final HashMap<UUID, Integer> tasks = new HashMap<>();

    public boolean hasSelection(Player player) {
        return selections.containsKey(player.getUniqueId());
    }

    public void select(Player player, ReferencePoint point) {
        if (tasks.get(player.getUniqueId()) != null) {
            Bukkit.getScheduler().cancelTask(tasks.get(player.getUniqueId()));
        }
        selections.put(player.getUniqueId(), point);
        int taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(DiscovOutlines.getInstance(), new ParticleTask(player, point), 0, 40).getTaskId();
        tasks.put(player.getUniqueId(), taskId);
    }

    public ReferencePoint getSelection(Player player) {
        return selections.get(player.getUniqueId());
    }
}
