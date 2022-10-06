package com.github.leialoha.tiltify.requests.data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.leialoha.tiltify.requests.components.ComponentCampaign;
import com.github.leialoha.tiltify.requests.components.ComponentError;
import com.github.leialoha.tiltify.requests.components.ComponentMeta;

public class TiltifyDataUserCampaigns {
    public final ComponentMeta meta;
    public final List<ComponentCampaign> data;
    public final ComponentError error;

    public TiltifyDataUserCampaigns(@JsonProperty("meta") Object meta,
		@JsonProperty("data") ArrayList<Object> data,
		@JsonProperty("error") Object error
	) {
		ObjectMapper mapper = new ObjectMapper();

        this.meta = mapper.convertValue(meta, ComponentMeta.class);
        this.data = new ArrayList<>();

		data.forEach((tiltifyCampaign) -> {
			ComponentCampaign campaign = mapper.convertValue(tiltifyCampaign, ComponentCampaign.class);
			this.data.add(campaign);
		});

        this.error = mapper.convertValue(error, ComponentError.class);
    }
}
