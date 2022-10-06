package com.github.leialoha.tiltify.requests.data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.leialoha.tiltify.requests.components.ComponentError;
import com.github.leialoha.tiltify.requests.components.ComponentLinks;
import com.github.leialoha.tiltify.requests.components.ComponentMeta;
import com.github.leialoha.tiltify.requests.components.ComponentUserDonation;

public class TiltifyDataCampaignDonations {
    public final ComponentMeta meta;
    public final List<ComponentUserDonation> data;
    public final ComponentLinks links;
    public final ComponentError error;

    public TiltifyDataCampaignDonations(@JsonProperty("meta") Object meta,
		@JsonProperty("data") ArrayList<Object> data,
		@JsonProperty("links") Object links,
		@JsonProperty("error") Object error
	) {
		ObjectMapper mapper = new ObjectMapper();

        this.meta = mapper.convertValue(meta, ComponentMeta.class);
        this.data = new ArrayList<>();

		data.forEach((tiltifyDonation) -> {
			ComponentUserDonation donation = mapper.convertValue(tiltifyDonation, ComponentUserDonation.class);
			this.data.add(donation);
		});

        this.links = mapper.convertValue(links, ComponentLinks.class);
        this.error = mapper.convertValue(error, ComponentError.class);
    }
}
