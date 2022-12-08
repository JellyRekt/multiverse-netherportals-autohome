package com.jellyrekt.mvnpautohome.commands;

import com.jellyrekt.mvnpautohome.MVNPAutohome;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.onarandombox.MultiverseNetherPortals.commands.NetherPortalCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;
import java.util.logging.Level;

public class AutohomeCommand extends NetherPortalCommand {
	private MVNPAutohome mvnpAutohome;

	public AutohomeCommand(MVNPAutohome plugin) {
		super(plugin.getMvNetherPortals());
		setName("Toggles NP sending players to their home.");
		setCommandUsage("/mvnp autohome " + ChatColor.GOLD + "[WORLD]");
		setArgRange(0, 1);
		addKey("mvnp autohome");
		addKey("mvnph");
		addCommandExample("/mvnp autohome");
		addCommandExample("/mvnph world_nether");
		setPermission("multiverse.netherportals.autohome",
				"This will set a world's nether portals to send players to their home. It will override the world's link.",
				PermissionDefault.OP);

		mvnpAutohome = plugin;
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
	}

	private void toggleUseHomes(CommandSender sender, String worldName) {
		MultiverseWorld mvWorld = plugin.getCore().getMVWorldManager().getMVWorld(worldName);
		if (mvWorld == null) {
			this.plugin.getCore().showNotMVWorldMessage(sender, worldName);
			return;
		}
		sender.sendMessage(getToggleMessage(mvWorld, mvnpAutohome.toggleAutohome(worldName)));
		plugin.log(Level.INFO, getLogMessage(sender, worldName));
	}

	private String getToggleMessage(MultiverseWorld mvWorld, boolean val) {
		return ChatColor.RED + "NOTE: " + ChatColor.WHITE + "Nether portals in " + mvWorld.getColoredWorldString() + " will "
				+ (val ? "now" : "no longer")
				+ " send players to their home.";
	}

	private String getLogMessage(CommandSender sender, String world) {
		return sender.getName() + " "
				+ (mvnpAutohome.getAutohome(world) ? "enabled" : "disabled")
				+ " auto-home for nether portals in " + world;
	}
}
