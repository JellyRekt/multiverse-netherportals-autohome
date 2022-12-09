package com.jellyrekt.mvnpautohome.listeners;

import com.earth2me.essentials.User;
import com.jellyrekt.mvnpautohome.MVNPAutohome;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.onarandombox.MultiverseCore.utils.WorldManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class PlayerListener implements Listener {
	private final MVNPAutohome plugin;
	private final MVWorldManager worldManager;

	public PlayerListener(MVNPAutohome plugin) {
		this.plugin = plugin;
		worldManager = plugin.getMvNetherPortals().getCore().getMVWorldManager();
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerPortal(PlayerPortalEvent event) {
		plugin.getLogger().info("PortalEvent fired for " + event.getPlayer().getName() + " in " + event.getFrom().getWorld().getName());
		World fromWorld = event.getFrom().getWorld();
		// Check that the world is registered with Multiverse and has auto-home set to true
		if (!worldManager.isMVWorld(fromWorld) || !plugin.getAutohome(fromWorld.getName())) {
			plugin.getLogger().info("Autohome is disabled for " + fromWorld.getName() + "; exiting");
			return;
		}
		// TODO Allow using Player's personal respawn point?
		// TODO Allow overriding the portal's destination world?
		// TODO Allow sending the Player back to the portal through which they came?
		// Get the player's home
		Player player = event.getPlayer();
		Location dest = new User(player, plugin.getEssentials()).getHome("home");
		// If no home is set, use the world's respawn point
		if (dest == null) {
			dest = worldManager
					.getMVWorld(event.getFrom().getWorld())
					.getRespawnToWorld()
					.getSpawnLocation();
		}
		// Setting the event destination creates a new portal, so instead we teleport the player manually and cancel the event.
		player.teleport(dest);
		event.setCancelled(true);
	}
}
