package com.github.leialoha.tiltify;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.leialoha.tiltify.checker.DonationHandler;
import com.github.leialoha.tiltify.checker.PlayerHandler;
import com.github.leialoha.tiltify.checker.TiltifyActions;
import com.github.leialoha.tiltify.events.PlayerEvents;
import com.github.leialoha.tiltify.requests.TiltifyRequest;
import com.github.leialoha.tiltify.requests.exceptions.InvalidTokenException;

public class Tiltify extends JavaPlugin {
	public static final Logger LOGGER = Logger.getLogger("Tiltify");
	private static FileConfiguration CONFIG;
	private static TiltifyRequest REQUESTS = null;
	public static boolean isRunning = true;

	public void onEnable() {
		LOGGER.info("Enabling Titlify...");
		loadConfigs();

		LOGGER.info("Loading Donations...");
		PlayerHandler.getInstance().load();
		DonationHandler.getInstance().load();

		loadTiltifyRequests();
		Bukkit.getPluginManager().registerEvents(new PlayerEvents(), this);
	}

	private void loadConfigs() {
		LOGGER.info("Loading configs...");
		File pluginFolder = this.getDataFolder();
		if (!pluginFolder.exists()) {
			LOGGER.warning("Could not find any config files... Creating some for you!");
			if (!pluginFolder.mkdirs()) {
				LOGGER.severe("Could create any config files!");
				this.setEnabled(false);
			} else {
				this.saveDefaultConfig();
				CONFIG = this.getConfig();
			}
		} else {
			this.saveDefaultConfig();
			CONFIG = this.getConfig();
		}
	}

	private void loadTiltifyRequests() {
		try {
			REQUESTS = new TiltifyRequest(CONFIG.getString("tiltify.token", "NoToken"));
		} catch (InvalidTokenException | IOException e) {
			LOGGER.warning("Could not validate token...");
			e.printStackTrace();
		}

		if (CONFIG.get("tiltify.campaigns").getClass() == Integer.class) {
			DonationHandler.getInstance().addCampaign(CONFIG.getInt("tiltify.campaigns"));
		} else {
			for (int campaignId : CONFIG.getIntegerList("tiltify.campaigns")) {
				DonationHandler.getInstance().addCampaign(campaignId);
			}
		}

		ConfigurationSection actions = CONFIG.getConfigurationSection("tiltify.actions");
		for (String actionName : actions.getKeys(false)) {
			ConfigurationSection action = actions.getConfigurationSection(actionName);
			TiltifyActions.Type type = null;
			int amount = action.getInt("amount", 0);
			List<String> commands = action.getStringList("commands");
			List<String> messages = action.getStringList("messages");
			List<String> calls = action.getStringList("calls");

			try {
				type = TiltifyActions.Type.valueOf(action.getString("type", "null").toUpperCase());
			} catch (IllegalArgumentException | NullPointerException e) {}

			new TiltifyActions(actionName, type, amount, commands, messages, calls);
		}
	}

	public void onDisable() {
		LOGGER.info("Disabling Titlify...");
		isRunning = false;

		LOGGER.info("Saving Donations...");
		PlayerHandler.getInstance().save();
		DonationHandler.getInstance().save();
	}

	public static TiltifyRequest getTiltifyRequest() {
		return REQUESTS;
	}
}
