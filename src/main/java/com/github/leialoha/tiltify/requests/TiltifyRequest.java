package com.github.leialoha.tiltify.requests;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.leialoha.tiltify.requests.data.TiltifyDataValidToken;
import com.github.leialoha.tiltify.requests.exceptions.InvalidTokenException;

public class TiltifyRequest {
	
	private final String token;

	public TiltifyRequest(String token) throws InvalidTokenException, IOException {
		this.token = token;

		boolean isValue = this.call(TiltifyDataValidToken.class, "user").valid;
		if (!isValue) throw new InvalidTokenException(this.token + " is not a valid token! Please check out https://tiltify-commands.readthedocs.io/en/latest/howto/tokens/ for more information.");
	}

	public <T> T call (Class<T> clazz, String stringUrl) throws IOException {
		URL url = new URL("https://tiltify.com/api/v3/" + stringUrl);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestProperty("accept", "application/json");
		connection.setRequestProperty("authorization", "Bearer " + token);

		InputStream responseStream;

		try {
			responseStream = connection.getInputStream();
		} catch (Exception e) {
			responseStream = connection.getErrorStream();
		}

		ObjectMapper mapper = new ObjectMapper();
		T data = mapper.readValue(responseStream, clazz);

		return data;
	}

}
