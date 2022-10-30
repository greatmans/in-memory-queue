package com.mans.imq.data;

import java.time.Instant;

public class Message {

	private String key;
	private String data;
	private Instant created;
	private long timeToLive;

	public Message(String key, String data, long timeToLive) {
		this.key = key;
		this.data = data;
		this.created = Instant.now();
		this.timeToLive = timeToLive;
	}

	public String getKey() {
		return key;
	}

	public String getData() {
		return data;
	}

	public Instant getCreated() {
		return created;
	}

	public long getTimeToLive() {
		return timeToLive;
	}
}
