package nl.parrotlync.discovoutlines;

import nl.parrotlync.discovoutlines.command.OutlinesCommandExecutor;
import nl.parrotlync.discovoutlines.manager.ReferenceManager;
import nl.parrotlync.discovoutlines.manager.SelectionManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class DiscovOutlines extends JavaPlugin {
    private static DiscovOutlines instance;
    private final ReferenceManager referenceManager;
    private final SelectionManager selectionManager;

    public DiscovOutlines() {
        instance = this;
        referenceManager = new ReferenceManager();
        selectionManager = new SelectionManager();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getScheduler().runTask(this, referenceManager::load);
        Objects.requireNonNull(getCommand("outlines")).setExecutor(new OutlinesCommandExecutor());
        getLogger().info("DiscovOutlines is now enabled!");
    }

    @Override
    public void onDisable() {
        referenceManager.save();
        getLogger().info("DiscovOutlines is now disabled!");
    }

    public ReferenceManager getReferenceManager() {
        return referenceManager;
    }

    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    public static DiscovOutlines getInstance() {
        return instance;
    }
}
