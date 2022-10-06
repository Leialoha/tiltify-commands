package com.github.leialoha.tiltify.checker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;

import com.github.leialoha.tiltify.Tiltify;
import com.github.leialoha.tiltify.utils.ComponentUtils;

public class TiltifyActions {

	public final String name;
	public final Type type;
	public final double amount;
	private final List<String> commands;
	private final List<String> messages;
	private final List<String> calls;

	public enum Type {
		TOTAL,
		ONCE
	}

	public TiltifyActions(String name, Type type, double amount, List<String> commands, List<String> messages, List<String> calls) {
		this.name = name;
		this.type = type;
		this.amount = amount;
		this.commands = commands;
		this.messages = messages;
		this.calls = calls;

		DonationHandler.getInstance().addAction(this);
	}

	public void run(HashMap<String, Object> options) {
		List<String> list = new ArrayList<>();
		run(options, list);
	}
	
	public void run(HashMap<String, Object> options, List<String> path) {
		if (path.contains(name)) return;
		path.add(name);

		PlayerHandler playerHandler = PlayerHandler.getInstance();
		DonationHandler checker = DonationHandler.getInstance();

		List<String> formattedCommands = List.copyOf(commands).stream().map((string) -> {
			String editedString = string;
			for (String key: options.keySet()) {
				editedString = Pattern.compile("%" + key).matcher(editedString).replaceAll(options.get(key) + "");
			}

			return editedString;
		}).toList();

		List<String> formattedMessages = List.copyOf(messages).stream().map((string) -> {
			String editedString = string;
			for (String key: options.keySet()) {
				editedString = Pattern.compile("%" + key).matcher(editedString).replaceAll(options.get(key) + "");
			}

			return editedString;
		}).toList();

		List<String> serverCmds = List.copyOf(formattedCommands).stream().filter((string) -> { return !string.startsWith("*"); }).toList();
		List<String> playerCmds = List.copyOf(formattedCommands).stream().filter((string) -> { return string.startsWith("*"); }).toList();
		List<String> serverMsgs = List.copyOf(formattedMessages).stream().filter((string) -> { return !string.startsWith("*"); }).toList();
		List<String> playerMsgs = List.copyOf(formattedMessages).stream().filter((string) -> { return string.startsWith("*"); }).toList();

		for (String command: serverCmds) Bukkit.getScheduler().callSyncMethod( Tiltify.getPlugin(Tiltify.class), () -> Bukkit.dispatchCommand( Bukkit.getConsoleSender(), command ) );;
		for (String message: serverMsgs) Bukkit.broadcast(new ComponentUtils(message).build());

		PlayerActions playerAction = playerHandler.getPlayer((String) options.get("player"), (int) options.get("campaignId"));
		playerAction.commandsToSend.addAll(playerCmds);
		playerAction.messagesToSend.addAll(playerMsgs);
		playerAction.execute();

		for (String callString : calls) {
			if (checker.hasAction(callString))
				checker.getAction(callString).run(options, path);
		}
	}

	public Type getType() {
		return type;
	}

	public double getAmount() {
		return amount;
	}

	// public static void main(String[] args) {
	// 	Type type = null;

	// 	try {
	// 		type = Type.valueOf(null);
	// 	} catch (IllegalArgumentException | NullPointerException ignored) {}
	// 	System.out.println(type);
	// }

}
