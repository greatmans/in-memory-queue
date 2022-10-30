package com.mans.imq.subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mans.imq.consumer.Consumer;

public class SubscriptionManager {

	private Map<String, List<Consumer>> subscriptionsMap;

	public SubscriptionManager() {
		this.subscriptionsMap = new HashMap<>();
	}

	public void createQueueSubscription(String queue) {
		subscriptionsMap.put(queue, new ArrayList<Consumer>());
	}

	public void subscribe(String queue, final Consumer consumer) {
		List<Consumer> consumers = subscriptionsMap.get(queue);
		if(consumers == null) {
			throw new RuntimeException("No such queue to subscribe");
		}
		else {
			consumers.add(consumer);
		}
		consumer.setSubscriptionActive(true);
		new Thread(consumer).start();
	}

	public void unsubscribe(String queue, final Consumer consumer) {
		List<Consumer> consumers = subscriptionsMap.get(queue);
		consumer.setSubscriptionActive(false);
		consumers.remove(consumer);
	}

	/*public void notify(String queue) {
		List<Subscription> subscriptions = subscriptionsMap.get(queue);
		for(Subscription subscription : subscriptions) {
			subscription.getConsumer().update(subscription);
		}
	}*/
}
