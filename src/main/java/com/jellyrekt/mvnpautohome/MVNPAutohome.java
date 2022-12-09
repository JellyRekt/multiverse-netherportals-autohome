package com.jellyrekt.mvnpautohome;

import com.earth2me.essentials.Essentials;
import com.jellyrekt.mvnpautohome.commands.AutohomeCommand;
import com.jellyrekt.mvnpautohome.listeners.PlayerListener;
import com.onarandombox.MultiverseCore.commands.HelpCommand;
import com.onarandombox.MultiverseNetherPortals.MultiverseNetherPortals;
import com.onarandombox.commandhandler.CommandHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;

public class MVNPAutohome extends JavaPlugin {
	private MultiverseNetherPortals mvNetherPortals;
	private Essentials essentials;
	private CommandHandler commandHandler;

	@Override
	public void onEnable() {
		setDependencies();
		registerCommands();
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
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

	public boolean getAutohome(String world) {
		String path = "worlds." + world + ".autohome";
		return getConfig().contains(path) && getConfig().getBoolean(path);
	}

	public boolean toggleAutohome(String world) {
		boolean val = !getAutohome(world);
		getConfig().set("worlds." + world + ".autohome", val);
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
		commandHandler.registerCommand(new AutohomeCommand(this));
		for (com.onarandombox.commandhandler.Command c : commandHandler.getAllCommands()) {
			if (c instanceof HelpCommand) {
				c.addKey("mvnp");
			}
		}
	}
}
