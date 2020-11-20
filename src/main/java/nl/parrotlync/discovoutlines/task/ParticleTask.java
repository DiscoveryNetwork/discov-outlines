package nl.parrotlync.discovoutlines.task;

import nl.parrotlync.discovoutlines.DiscovOutlines;
import nl.parrotlync.discovoutlines.model.ReferencePoint;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class ParticleTask implements Runnable {
    private final Player player;
    private final ReferencePoint referencePoint;

    public ParticleTask(Player player, ReferencePoint referencePoint) {
        this.player = player;
        this.referencePoint = referencePoint;
    }

    @Override
    public void run() {
        for (double i = 0; i < 3 + 1; i = i + 0.5) {
            player.spawnParticle(Particle.DRIP_LAVA, referencePoint.getLocation().add(0, i, 0), 1, 0, 0, 0);
        }
    }
}
