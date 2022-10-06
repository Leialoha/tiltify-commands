package com.github.leialoha.tiltify.requests.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.leialoha.tiltify.requests.components.ComponentError;
import com.github.leialoha.tiltify.requests.components.ComponentMeta;
import com.github.leialoha.tiltify.requests.components.ComponentUser;

public class TiltifyDataUser {
    public final ComponentMeta meta;
    public final ComponentUser data;
    public final ComponentError error;

    public TiltifyDataUser(@JsonProperty("meta") Object meta,
		@JsonProperty("data") Object data,
		@JsonProperty("error") Object error
	) {
		ObjectMapper mapper = new ObjectMapper();

        this.meta = mapper.convertValue(meta, ComponentMeta.class);
        this.data = mapper.convertValue(data, ComponentUser.class);
        this.error = mapper.convertValue(error, ComponentError.class);
    }
}
