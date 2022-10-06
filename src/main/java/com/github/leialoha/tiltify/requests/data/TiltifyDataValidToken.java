package com.github.leialoha.tiltify.requests.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.leialoha.tiltify.requests.components.ComponentMeta;

public class TiltifyDataValidToken {
    public final boolean valid;

    public TiltifyDataValidToken(@JsonProperty("meta") Object meta,
		@JsonProperty("data") Object data,
		@JsonProperty("error") Object error
	) {
		ObjectMapper mapper = new ObjectMapper();

        this.valid = mapper.convertValue(meta, ComponentMeta.class).status == 200;
    }
}
