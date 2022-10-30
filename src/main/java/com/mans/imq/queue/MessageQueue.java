package com.mans.imq.queue;

import com.mans.imq.data.Message;

public class MessageQueue implements Queue {

	private final String name;
	private final int capacity;
	private Node front;
	private Node rear;
	private int size = 0;
	private final Object QUEUE_EMPTY = new Object();
	private final Object QUEUE_FULL = new Object();

	public MessageQueue(String name, int capacity) {
		this.front = this.rear = null;
		this.name = name;
		this.capacity = capacity;
	}

	@Override
	public void add(Message message) {
		Node temp = new Node(message);

		//synchronized (this) {
			if (this.rear == null) {
				this.front = this.rear = temp;
				size++;
				return;
			}

			this.rear.next = temp;
			this.rear = temp;
			size++;
		//}
	}

	@Override
	public Message remove() {
		synchronized (this) {
			if (this.front == null)
				return null;

			Node temp = this.front;
			this.front = this.front.next;

			if (this.front == null)
				this.rear = null;

			size--;
			return temp.getMessage();
		}
	}

	@Override
	public Message peek() {
		synchronized (this) {
			if (this.front == null)
				return null;
			Node temp = this.front;
			return temp.getMessage();
		}
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean isFull() {
		return size == capacity;
	}

	public Object getQUEUE_EMPTY() {
		return QUEUE_EMPTY;
	}

	public Object getQUEUE_FULL() {
		return QUEUE_FULL;
	}

	public int getCapacity() {
		return capacity;
	}

	public int getSize() {
		return size;
	}

	public String getName() {
		return name;
	}

	public static class Node {
		private Message message;
		private Node next;

		Node(Message message) {
			this.message = message;
			this.next = null;
		}

		public Message getMessage() {
			return message;
		}

		public Node getNext() {
			return next;
		}
	}
}
