package com.github.leialoha.tiltify.requests.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ComponentCampaign {
	public final int id;
	public final String name;
	public final String url;
	public final String owner;
	public final float totalAmountRaised;
	public final float fundraiserGoalAmount;

    public ComponentCampaign(@JsonProperty("id") int id,
		@JsonProperty("name") String name,
		@JsonProperty("slug") String slug,
		@JsonProperty("startsAt") long startsAt,
		@JsonProperty("endsAt") long endsAt,
		@JsonProperty("description") String description,
		@JsonProperty("causeId") int causeId,
		@JsonProperty("originalFundraiserGoal") int originalFundraiserGoal,
		@JsonProperty("fundraiserGoalAmount") int fundraiserGoalAmount,
		@JsonProperty("supportingAmountRaised") int supportingAmountRaised,
		@JsonProperty("amountRaised") float amountRaised,
		@JsonProperty("supportable") boolean supportable,
		@JsonProperty("status") String status,
		@JsonProperty("type") String type,
		@JsonProperty("avatar") Object avatar,
		@JsonProperty("livestream") Object livestream,
		@JsonProperty("causeCurrency") String causeCurrency,
		@JsonProperty("totalAmountRaised") int totalAmountRaised,
		@JsonProperty("user") Object user,
		@JsonProperty("fundraisingEventId") int fundraisingEventId,
		@JsonProperty("regionId") Object regionId,
		@JsonProperty("metadata") Object metadata
	) {
		ObjectMapper mapper = new ObjectMapper();
		
        this.id = id;
        this.name = name;
		
		ComponentUser owner = mapper.convertValue(user, ComponentUser.class);
		this.owner = owner.username;

        this.url = String.format("https://tiltify.com/@%s/%s", owner.slug, slug);
		this.totalAmountRaised = totalAmountRaised;
		this.fundraiserGoalAmount = fundraiserGoalAmount;
    }
}