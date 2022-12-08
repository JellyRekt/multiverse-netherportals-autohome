package com.jellyrekt.netherportalhomes.commands;

import com.jellyrekt.netherportalhomes.NetherPortalHomes;
import com.onarandombox.MultiverseCore.MVWorld;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.onarandombox.MultiverseNetherPortals.commands.NetherPortalCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;
import java.util.logging.Level;

public class HomesCommand extends NetherPortalCommand {
	private NetherPortalHomes netherPortalHomes;

	public HomesCommand(NetherPortalHomes plugin) {
		super(plugin.getMvNetherPortals());
		setName("Toggles NP sending players to their home.");
		setCommandUsage("/mvnp homes " + ChatColor.GOLD + "[WORLD]");
		setArgRange(0, 1);
		addKey("mvnp homes");
		addKey("mvnph");
		addCommandExample("/mvnp homes");
		addCommandExample("/mvnph world_nether");
		setPermission("multiverse.netherportals.homes",
				"This will set a world's nether portals to send players to their home. It will override the world's link.",
				PermissionDefault.OP);

		netherPortalHomes = plugin;
	}

	@Override
	public void runCommand(CommandSender sender, List<String> args) {
		// Command issued from console
		if (!(sender instanceof Player)) {
			// Require WORLD
			if (args.size() < 1) {
				sender.sendMessage("From the command line, WORLD is required.");
				sender.sendMessage("No changes were made...");
				return;
			}
			// Check for world registration with Multiverse
			toggleUseHomes(sender, args.get(0));
			return;
		}
		// Command issued in-game
		Player player = (Player) sender;
		String world = args.size() < 1 ? player.getWorld().getName() : args.get(0);
		toggleUseHomes(sender, world);
		plugin.log(Level.INFO, getPlayerLogMessage(player, world));
	}

	private void toggleUseHomes(CommandSender sender, String worldName) {
		MultiverseWorld mvWorld = plugin.getCore().getMVWorldManager().getMVWorld(worldName);
		if (mvWorld == null) {
			this.plugin.getCore().showNotMVWorldMessage(sender, worldName);
			return;
		}
		sender.sendMessage(getToggleMessage(mvWorld, netherPortalHomes.toggleUseHomesFor(worldName)));
	}

	private String getToggleMessage(MultiverseWorld mvWorld, boolean val) {
		return ChatColor.RED + "NOTE: " + ChatColor.WHITE + "Nether portals in " + mvWorld.getColoredWorldString() + " will "
				+ (val ? "now" : "no longer")
				+ " send players to their home.";
	}

	private String getPlayerLogMessage(Player player, String world) {
		return player.getName() + " "
				+ (netherPortalHomes.getUseHomesFor(world) ? "ENABLED" : "DISABLED")
				+ " auto-home for Nether Portals in " + world;
	}
}
