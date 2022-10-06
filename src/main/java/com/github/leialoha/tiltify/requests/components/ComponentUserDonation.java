package com.github.leialoha.tiltify.requests.components;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ComponentUserDonation {
	public final int id;
	public final float amount;
	public final String name;
	public final String comment;
	public final long completedAt;
	public final long updatedAt;
	public final boolean sustained;
	public final int rewardId;

    public ComponentUserDonation(@JsonProperty("id") int id,
		@JsonProperty("amount") float amount,
		@JsonProperty("name") String name,
		@JsonProperty("comment") String comment,
		@JsonProperty("completedAt") long completedAt,
		@JsonProperty("updatedAt") long updatedAt,
		@JsonProperty("sustained") boolean sustained,
		@JsonProperty("rewardId") int rewardId
	) {
        this.id = id;
		this.amount = amount;
		this.name = name;
		this.comment = comment;
		this.completedAt = completedAt;
		this.updatedAt = updatedAt;
		this.sustained = sustained;
		this.rewardId = rewardId;
    }
}








