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

public class NetherPortalHomes extends JavaPlugin {
	private MultiverseNetherPortals mvNetherPortals;
	private Essentials essentials;
	private CommandHandler commandHandler;

	@Override
	public void onEnable() {
		setDependencies();
		registerCommands();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!this.isEnabled()) {
			sender.sendMessage("This plugin is disabled!");
			return true;
		}
		ArrayList<String> allArgs = new ArrayList<>(Arrays.asList(args));
		allArgs.add(0, command.getName());
		return this.commandHandler.locateAndRunCommand(sender, allArgs);
	}

	public MultiverseNetherPortals getMvNetherPortals() {
		return mvNetherPortals;
	}

	public Essentials getEssentials() {
		return essentials;
	}

	public boolean getUseHomesFor(String world) {
		String path = "worlds." + world + ".homes";
		return getConfig().contains(path) && getConfig().getBoolean(path);
	}

	public boolean toggleUseHomesFor(String world) {
		boolean val = !getUseHomesFor(world);
		getConfig().set("worlds." + world + ".homes", val);
		saveConfig();
		return val;
	}

	private void setDependencies() {
		mvNetherPortals = (MultiverseNetherPortals) getServer().getPluginManager().getPlugin("Multiverse-NetherPortals");
		if (mvNetherPortals == null) {
			getLogger().warning("Multiverse-NetherPortals not found.");
		}
		essentials = (Essentials) getServer().getPluginManager().getPlugin("Essentials");
		if (essentials == null) {
			getLogger().warning("Essentials not found.");
		}
	}

	private void registerCommands() {
		commandHandler = mvNetherPortals.getCore().getCommandHandler();
		commandHandler.registerCommand(new HomesCommand(this));
		for (com.onarandombox.commandhandler.Command c : commandHandler.getAllCommands()) {
			if (c instanceof HelpCommand) {
				c.addKey("mvnp");
			}
		}
	}
}
