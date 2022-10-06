package com.github.leialoha.tiltify.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;

public class ComponentUtils implements Cloneable {
	
	private boolean toConsole = false;
	private boolean format = true;
	private Builder text = Component.text();
	private TextColor color = NamedTextColor.WHITE;
	private HashMap<TextDecoration, State> style = new HashMap<>();

	StringBuilder msg = new StringBuilder();

	public ComponentUtils() {
		style.put(TextDecoration.BOLD, State.FALSE);
		style.put(TextDecoration.ITALIC, State.FALSE);
		style.put(TextDecoration.OBFUSCATED, State.FALSE);
		style.put(TextDecoration.STRIKETHROUGH, State.FALSE);
		style.put(TextDecoration.UNDERLINED, State.FALSE);

		text.append(Component.text().content("").decorations(style).color(color).build());
	}

	public ComponentUtils(String string) {
		new ComponentUtils();
		this.text(string);
	}

	public ComponentUtils toConsole(boolean toConsole) {
		this.toConsole = toConsole;
		return this;	
	}

	public ComponentUtils format(boolean format) {
		this.format = format;
		return this;	
	}

	public ComponentUtils text(String message) {
		if (toConsole && format) message = message.replaceAll("&(k-oK-O)", "");
		msg.append(message);

		Pattern pattern = Pattern.compile("(&x(&[a-f0-9]){6}|&[a-f0-9k-or])", Pattern.CASE_INSENSITIVE);
		Pattern hexPattern = Pattern.compile("&x(&[a-f0-9]){6}", Pattern.CASE_INSENSITIVE);
		
		List<String> unformatted = List.of(pattern.split(message));
		List<String> matches = pattern.matcher(message).results().map(mapper -> { return mapper.group(); }).toList();

		List<String> split = (format) ? alternate(unformatted, matches) : new ArrayList<>();
		if (!format) split.add(message);

		for (String string : split) {
			if (pattern.matcher(string).matches() && format) {
				if (hexPattern.matcher(string).matches()) {
					String textColor = string;
					textColor = textColor.replaceFirst("x", "#").replaceAll("&", "");
					color = TextColor.fromHexString(textColor);
				} else {
					switch (string.replaceAll("&", "").toUpperCase()) {
						case "K" -> style.put(TextDecoration.OBFUSCATED, State.TRUE);
						case "L" -> style.put(TextDecoration.BOLD, State.TRUE);
						case "M" -> style.put(TextDecoration.STRIKETHROUGH, State.TRUE);
						case "N" -> style.put(TextDecoration.UNDERLINED, State.TRUE);
						case "O" -> style.put(TextDecoration.ITALIC, State.TRUE);
						case "0" -> changeColor(NamedTextColor.BLACK);
						case "1" -> changeColor(NamedTextColor.BLUE);
						case "2" -> changeColor(NamedTextColor.DARK_GREEN);
						case "3" -> changeColor(NamedTextColor.DARK_AQUA);
						case "4" -> changeColor(NamedTextColor.DARK_RED);
						case "5" -> changeColor(NamedTextColor.DARK_PURPLE);
						case "6" -> changeColor(NamedTextColor.GOLD);
						case "7" -> changeColor(NamedTextColor.GRAY);
						case "8" -> changeColor(NamedTextColor.DARK_GRAY);
						case "9" -> changeColor(NamedTextColor.DARK_BLUE);
						case "A" -> changeColor(NamedTextColor.GREEN);
						case "B" -> changeColor(NamedTextColor.AQUA);
						case "C" -> changeColor(NamedTextColor.RED);
						case "D" -> changeColor(NamedTextColor.LIGHT_PURPLE);
						case "E" -> changeColor(NamedTextColor.YELLOW);
						case "F" -> changeColor(NamedTextColor.WHITE);
						case "R" -> changeColor(NamedTextColor.WHITE);
					}
				}
			} else if (string != "") {
				Set<TextDecoration> styles = new HashSet<>();

				for (TextDecoration decoration: style.keySet()) {
					if (style.get(decoration) == State.TRUE)
						styles.add(decoration);
				}

				Component text = Component.text(string, color, styles);
				this.text.append(text);
			}
		}

		return this;	
	}

	private List<String> alternate(List<String> array1, List<String> array2) {
		List<String> returnValues = new ArrayList<>();
		int length = Math.max(array1.size(), array2.size());

		for (int i = 0; i < length; i++) {
			if (array1.size() > i) returnValues.add(array1.get(i));
			if (array2.size() > i) returnValues.add(array2.get(i));
		}

		return returnValues;
	}

	public Component build() {
		return text.build();
	}

	private void changeColor(TextColor color) {
		this.color = color;

		style.remove(TextDecoration.BOLD);
		style.remove(TextDecoration.ITALIC);
		style.remove(TextDecoration.OBFUSCATED);
		style.remove(TextDecoration.STRIKETHROUGH);
		style.remove(TextDecoration.UNDERLINED);

		style.put(TextDecoration.BOLD, State.FALSE);
		style.put(TextDecoration.ITALIC, State.FALSE);
		style.put(TextDecoration.OBFUSCATED, State.FALSE);
		style.put(TextDecoration.STRIKETHROUGH, State.FALSE);
		style.put(TextDecoration.UNDERLINED, State.FALSE);
	}

	@Override
	public ComponentUtils clone() {
		return new ComponentUtils(msg.toString());
	}
}
