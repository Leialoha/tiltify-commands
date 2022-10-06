package com.github.leialoha.tiltify.checker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.github.leialoha.tiltify.Tiltify;

public class PlayerHandler {
		
	private static PlayerHandler instance;
	private final HashMap<Integer, List<PlayerActions>> actions = new HashMap<>();
	private final File dataFolder = new File(Tiltify.getPlugin(Tiltify.class).getDataFolder(), "data");

	private PlayerHandler() {
		instance = this;
	}

	public static PlayerHandler getInstance() {
		if (instance == null) new PlayerHandler();
		return instance;
	}


	public void load() {
		if (!dataFolder.exists()) return;

		for (File folderInt : dataFolder.listFiles()) {
			if (!folderInt.isDirectory()) continue;
			
			List<PlayerActions> playerActions = new ArrayList<>();

			for (File file: folderInt.listFiles()) {
				try {
					FileInputStream fileInputStream = new FileInputStream(file);
					ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
					
					PlayerActions playerAction = (PlayerActions) objectInputStream.readObject();
					playerActions.add(playerAction);

					objectInputStream.close();
					fileInputStream.close();
					
				} catch (IOException | ClassNotFoundException e) {
					Tiltify.LOGGER.warning("Could not load player data from " + file.getName());
					e.printStackTrace();
				}
			}

			try {
				int folder = Integer.parseInt(folderInt.getName());
				actions.put(folder, playerActions);
			} catch (NumberFormatException e) {}
		}
	}

	public void save() {
		if (!dataFolder.exists()) dataFolder.mkdirs();

		for (int folderInt : actions.keySet()) {
			File folder = new File(dataFolder, folderInt + "");
			folder.mkdirs();

			for (PlayerActions playerAction : actions.get(folderInt)) {
				try {
					File file = new File(folder, playerAction.playerName + ".dat");

					FileOutputStream fileOutputStream = new FileOutputStream(file);
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

					objectOutputStream.writeObject(playerAction);

					objectOutputStream.close();
					fileOutputStream.close();
				} catch (IOException e) {
					Tiltify.LOGGER.warning("Could not save player data to " + playerAction.playerName + ".dat");
					e.printStackTrace();
				}
			}
		}
	}

	public PlayerActions getPlayer(String player, int campaignId) {
		if (!actions.containsKey(campaignId)) {
			actions.put(campaignId, new ArrayList<>());
		}

		if (!actions.get(campaignId).stream().anyMatch((playerAction) -> { return playerAction.playerName.equalsIgnoreCase(player); })) {
			actions.get(campaignId).add(new PlayerActions(player.toLowerCase()));
		}

		return actions.get(campaignId).stream().filter((playerAction) -> { return playerAction.playerName.equalsIgnoreCase(player); }).findFirst().get();
	}

	public void updatePlayer(@NotNull Player player) {
		for (int key : actions.keySet()) {
			List<PlayerActions> actionList = actions.get(key).stream().filter(action -> {
				return action.playerName.equalsIgnoreCase(player.getName());
			}).toList();

			for (PlayerActions action : actionList) {
				// action.playerUUID = player.getUniqueId();
				action.send(player);
			}
		}
	}

}
