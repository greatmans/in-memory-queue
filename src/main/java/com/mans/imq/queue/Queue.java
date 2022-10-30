package com.mans.imq.queue;

import com.mans.imq.data.Message;

public interface Queue {

	public void add(Message message);

	public Message remove();

	public Message peek();

	public boolean isEmpty();

	public boolean isFull();
}
