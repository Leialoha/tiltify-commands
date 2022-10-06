package com.github.leialoha.tiltify.requests.components;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ComponentError {
	public final String title;
	public final String detail;

    public ComponentError(@JsonProperty("title") String title,
		@JsonProperty("detail") String detail
	) {
        this.title = title;
        this.detail = detail;
    }
}
