package com.mans.imq.producer;

import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.mans.imq.data.Message;
import com.mans.imq.queue.MessageQueue;
import com.mans.imq.subscription.SubscriptionManager;

public class Producer implements Runnable {

	private final String name;
	private final MessageQueue queue;
	private SubscriptionManager subscriptionManager;

	public Producer(String name, MessageQueue queue, SubscriptionManager subscriptionManager) {
		this.name = name;
		this.queue = queue;
		this.subscriptionManager = subscriptionManager;
		subscriptionManager.createQueueSubscription(queue.getName());
	}

	@Override
	public void run() {
		int i = 1;
		int[] statusCodes = new int[] { 200, 400 };
		int[] ttl = new int[] { 0, 1, 2 };

		while (true) {
			String key = "key" + i;
			JSONObject obj = new JSONObject();
			obj.put("messageId", "msg" + (i++));
			Random random = new Random();
			int index = random.nextInt(2);
			obj.put("httpCode", statusCodes[index]);
			random = new Random();
			int idx = random.nextInt(3);
			Message message = new Message(key, JSONValue.toJSONString(obj), ttl[idx]);

			synchronized (queue) {
				while (queue.isFull()) {

					try {
						System.out.println("Producer [" + name + "] is waiting since queue is full. " +
								"Queue size [" + queue.getSize() + "] and capacity [" + queue.getCapacity() + "]");
						queue.wait();


					} catch (InterruptedException e) {
						break;
					}
				}

				queue.add(message);
				System.out.println("Producer [" + name + "] produced message " + message.getData() +
						" Queue size [" + queue.getSize() + "] and capacity [" + queue.getCapacity() + "]");

				queue.notifyAll();
			}
		}
	}
}
