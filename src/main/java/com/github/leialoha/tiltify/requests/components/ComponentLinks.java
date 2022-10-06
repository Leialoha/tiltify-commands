package com.github.leialoha.tiltify.requests.components;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ComponentLinks {
	public final String prev;
	public final String next;
	public final String self;

    public ComponentLinks(@JsonProperty("prev") String prev,
		@JsonProperty("next") String next,
		@JsonProperty("self") String self
	) {
        this.prev = prev;
        this.next = next;
        this.self = self;
    }
}
