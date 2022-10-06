package com.github.leialoha.tiltify.requests.components;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ComponentUser {
	public final int id;
	public final String username;
	public final String slug;
	public final String url;
	public final float totalAmountRaised;

    public ComponentUser(@JsonProperty("id") int id,
		@JsonProperty("username") String username,
		@JsonProperty("slug") String slug,
		@JsonProperty("url") String url,
		@JsonProperty("avatar") Object avatar,
		@JsonProperty("email") String email,
		@JsonProperty("about") String about,
		@JsonProperty("totalAmountRaised") float totalAmountRaised,
		@JsonProperty("social") Object social,
		@JsonProperty("firstName") String firstName,
		@JsonProperty("lastName") String lastName,
		@JsonProperty("newsletter") String newsletter,
		@JsonProperty("confirmed") boolean confirmed
	) {
        this.id = id;
        this.username = username;
        this.slug = slug;
        this.url = url;
		this.totalAmountRaised = totalAmountRaised;
    }
}