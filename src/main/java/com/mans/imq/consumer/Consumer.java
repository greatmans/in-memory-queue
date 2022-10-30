package com.mans.imq.consumer;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mans.imq.data.Message;
import com.mans.imq.queue.MessageQueue;
import com.mans.imq.subscription.Subscription;

public class Consumer implements Runnable {

	private final String name;
	private final MessageQueue queue;
	private Subscription subscription;
	private volatile boolean subscriptionActive;

	public Consumer(String name, MessageQueue queue, Subscription subscription) {
		this.name = name;
		this.queue = queue;
		this.subscription = subscription;
	}

	@Override
	public void run() {
		while(subscriptionActive) {
			synchronized (queue) {
				waitIfQueueIsEmpty();
				try {
					consume();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				queue.notifyAll();
			}
		}
	}

	private void waitIfQueueIsEmpty() {
		while (queue.isEmpty()) {
			try {
				System.out.println("Consumer [" + name + "] is waiting since queue is empty. " +
						"Queue size [" + queue.getSize() + "] and capacity [" + queue.getCapacity() + "]");
				queue.wait();
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	private void consume() throws InterruptedException {
		Message message = queue.peek();
		// Thread.sleep(3000); // Uncomment to simulate message expiration
		if(ChronoUnit.SECONDS.between(message.getCreated(), Instant.now()) <= message.getTimeToLive()) {
			String expression = subscription.getExpression();
			if (expression.equals("*")) {
				queue.remove();
				System.out.println("Consumer [" + name + "] consumed message" + message.getData() +
						" Queue size [" + queue.getSize() + "] and capacity [" + queue.getCapacity() + "]");
			} else {
				JSONObject jsonObject = parseMessage(message);

				String[] expressionArr = expression.split(":");
				if (jsonObject != null && jsonObject.containsKey(expressionArr[0])
						&& jsonObject.get(expressionArr[0]).toString().equals(expressionArr[1])) {
					//Message message = queue.remove();
					queue.remove();
					System.out.println("Consumer [" + name + "] consumed message" + message.getData() +
							" Queue size [" + queue.getSize() + "] and capacity [" + queue.getCapacity() + "]");
				}
			}
		}
		else {
			System.out.println("Message expired " + message.getData() + " Skipped processing.");
			queue.remove();
		}
	}

	private static JSONObject parseMessage(Message message) {
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject) parser.parse(message.getData());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return jsonObject;
	}

	public boolean isSubscriptionActive() {
		return subscriptionActive;
	}

	public void setSubscriptionActive(boolean subscriptionActive) {
		this.subscriptionActive = subscriptionActive;
	}
}
