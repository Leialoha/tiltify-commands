package com.github.leialoha.tiltify.checker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.leialoha.tiltify.Tiltify;
import com.github.leialoha.tiltify.utils.ComponentUtils;

public class PlayerActions implements Serializable {

	public final String playerName;
	public UUID playerUUID = null;
	public float donatedAmount = 0f;
	public final List<String> commandsToSend = new ArrayList<>();
	public final List<String> messagesToSend = new ArrayList<>();

	public PlayerActions(String playerName) {
		this.playerName = playerName;

		for (Player loopPlayer: Bukkit.getOnlinePlayers()) {
			if (loopPlayer.getName().equalsIgnoreCase(playerName))
				playerUUID = loopPlayer.getUniqueId();
		}
	}

	public void send(Player player) {
		// if (playerUUID == null) 
		playerUUID = player.getUniqueId();
		execute();
	}

	public void execute() {
		if (playerUUID == null) return;
		if (!Bukkit.getOfflinePlayer(playerUUID).isOnline()) return;

		Player player = Bukkit.getPlayer(playerUUID);
		
		for (String command : commandsToSend)
		Bukkit.getScheduler().callSyncMethod( Tiltify.getPlugin(Tiltify.class), () -> Bukkit.dispatchCommand( player, command.substring(1) ) );
		commandsToSend.clear();

		for (String message : messagesToSend) player.sendMessage(new ComponentUtils(message.substring(1)).build());
		messagesToSend.clear();
	}
}
