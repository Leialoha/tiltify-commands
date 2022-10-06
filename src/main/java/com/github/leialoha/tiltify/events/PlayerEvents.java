package com.github.leialoha.tiltify.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.leialoha.tiltify.checker.PlayerHandler;

public class PlayerEvents implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		PlayerHandler.getInstance().updatePlayer(event.getPlayer());
	}

}
