package com.jellyrekt.netherportalhomes.commands;

import com.jellyrekt.netherportalhomes.NetherPortalHomes;
import com.onarandombox.MultiverseNetherPortals.MultiverseNetherPortals;
import com.onarandombox.MultiverseNetherPortals.commands.NetherPortalCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

public class HomesCommand extends NetherPortalCommand {

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
			return;
		}
		// Command issued in-game
		sender.sendMessage(ChatColor.AQUA + "You are a player.");
		sender.sendMessage(ChatColor.AQUA + "Provided " + args.size() + " arg(s).");
	}
}
