package de.trc.trcf3gamemodeswitcher;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TRCF3GamemodeSwitcher extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        for (Player player : getServer().getOnlinePlayers()) {
            applyOPLevel(player);
        }

        Constants.LOGGER.info("TRC F3GamemodeSwitcher enabled.");
    }

    @Override
    public void onDisable() {
        Constants.LOGGER.info("TRC F3GamemodeSwitcher disabled.");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        applyOPLevel(event.getPlayer());
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        applyOPLevel(event.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        applyOPLevel(event.getPlayer());
    }

    private void updateClientOpLevel(Player player) {
        if (!player.hasPermission("trc_f3_gamemode_switcher.switch") || player.isOp()) {
            return;
        }

        try {
            player.sendOpLevel((byte) 2);
            player.updateCommands();

        } catch (NoSuchMethodError e) {
            Constants.LOGGER.error("This Plugins requires PaperMC or a Fork of PaperMC");

        } catch (Exception e) {
            Constants.LOGGER.error("Error while updating Client: {}", e.getMessage());
        }
    }

    private void applyOPLevel(Player player) {
        getServer().getScheduler().runTaskLater(this, () -> {
            if (player.isOnline()) {
                updateClientOpLevel(player);
            }
        }, 5L);
    }
}
