package com.github.leialoha.tiltify.requests.components;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ComponentMeta {
	public final int status;

    public ComponentMeta(@JsonProperty("status") int status) {
        this.status = status;
    }
}
