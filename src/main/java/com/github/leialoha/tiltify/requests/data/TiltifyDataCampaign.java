package com.github.leialoha.tiltify.requests.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.leialoha.tiltify.requests.components.ComponentCampaign;
import com.github.leialoha.tiltify.requests.components.ComponentError;
import com.github.leialoha.tiltify.requests.components.ComponentMeta;

public class TiltifyDataCampaign {
    public final ComponentMeta meta;
    public final ComponentCampaign data;
    public final ComponentError error;

    public TiltifyDataCampaign(@JsonProperty("meta") Object meta,
		@JsonProperty("data") Object data,
		@JsonProperty("error") Object error
	) {
		ObjectMapper mapper = new ObjectMapper();

        this.meta = mapper.convertValue(meta, ComponentMeta.class);
        this.data = mapper.convertValue(data, ComponentCampaign.class);
        this.error = mapper.convertValue(error, ComponentError.class);
    }
}
