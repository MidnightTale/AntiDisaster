package xyz.hynse.antidisaster;

import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Ghast;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiDisaster extends JavaPlugin implements Listener {
    private boolean antiCreeperGriefEnabled;
    private boolean antiGhastGriefEnabled;
    private boolean antiEndermanGriefEnabled;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfig();
        getServer().getPluginManager().registerEvents(this, this);
    }

    private void loadConfig() {
        FileConfiguration config = getConfig();
        antiCreeperGriefEnabled = config.getBoolean("anti-creeper-grief-enabled", true);
        antiGhastGriefEnabled = config.getBoolean("anti-ghast-grief-enabled", true);
        antiEndermanGriefEnabled = config.getBoolean("anti-enderman-grief-enabled", true);
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (antiEndermanGriefEnabled && event.getEntityType() == EntityType.ENDERMAN) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (antiCreeperGriefEnabled && event.getEntityType() == EntityType.CREEPER) {
            for (Block block : event.blockList()) {
                block.getDrops().clear();
            }
            event.blockList().clear();
            event.setYield(0); // Prevent block damage
        }
        if (antiGhastGriefEnabled && event.getEntityType() == EntityType.FIREBALL && ((Fireball) event.getEntity()).getShooter() instanceof Ghast) {
            for (Block block : event.blockList()) {
                block.getDrops().clear();
            }
            event.blockList().clear();
            event.setYield(0); // Prevent block damage
        }
    }
}
