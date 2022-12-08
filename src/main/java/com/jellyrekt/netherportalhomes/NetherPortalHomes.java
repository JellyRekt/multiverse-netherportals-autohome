package com.jellyrekt.netherportalhomes;

import com.earth2me.essentials.Essentials;
import com.jellyrekt.netherportalhomes.commands.HomesCommand;
import com.onarandombox.MultiverseCore.commands.HelpCommand;
import com.onarandombox.MultiverseNetherPortals.MultiverseNetherPortals;
import com.onarandombox.commandhandler.CommandHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class NetherPortalHomes extends JavaPlugin {
	private MultiverseNetherPortals mvNetherPortals;
	private Essentials essentials;
	private CommandHandler commandHandler;
	private HashMap<String, Boolean> useHomesFor = new HashMap<>();

	@Override
	public void onEnable() {
		mvNetherPortals = (MultiverseNetherPortals) getServer().getPluginManager().getPlugin("Multiverse-NetherPortals");
		if (mvNetherPortals == null)
		{
			getLogger().warning("Multiverse-NetherPortals not found.");
		}
		essentials = (Essentials) getServer().getPluginManager().getPlugin("Essentials");
		if (essentials == null)
		{
			getLogger().warning("Essentials not found.");
		}

		commandHandler = mvNetherPortals.getCore().getCommandHandler();
		commandHandler.registerCommand(new HomesCommand(this));
		for (com.onarandombox.commandhandler.Command c : commandHandler.getAllCommands()) {
			if (c instanceof HelpCommand) {
				c.addKey("mvnp");
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!this.isEnabled()) {
			sender.sendMessage("This plugin is disabled!");
			return true;
		}
		ArrayList<String> allArgs = new ArrayList<>(Arrays.asList(args));
		allArgs.add(0, command.getName());
		getLogger().info("Command issued.");
		this.commandHandler.locateAndRunCommand(sender, allArgs);
		return true;
	}

	public MultiverseNetherPortals getMvNetherPortals() {
		return mvNetherPortals;
	}

	public Essentials getEssentials() {
		return essentials;
	}
}
