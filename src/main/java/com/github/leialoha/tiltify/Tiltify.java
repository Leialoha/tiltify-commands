package com.github.leialoha.tiltify;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Tiltify extends JavaPlugin {
	private static final Logger LOGGER = Logger.getLogger("Tiltify");
	private static FileConfiguration CONFIG;

	public void onEnable() {
		LOGGER.warning("Enabling Titlify...");
		loadConfigs();
	}

	private void loadConfigs() {
		LOGGER.warning("Loading configs...");
		File pluginFolder = this.getDataFolder();
		if (!pluginFolder.exists()) {
			LOGGER.warning("Could not find any config files... Creating some for you!");
			if (!pluginFolder.mkdirs()) {
				LOGGER.severe("Could create any config files!");
				this.setEnabled(false);
			} else {
				this.saveDefaultConfig();
			}
		}
	}

	public void onDisable() {
		LOGGER.info("Disabling Titlify...");
		if (CONFIG != null) {
			LOGGER.info("Saving configs...");
			saveConfig();
		}
	}
}
