package com.mans.imq;

import com.mans.imq.consumer.Consumer;
import com.mans.imq.producer.Producer;
import com.mans.imq.queue.MessageQueue;
import com.mans.imq.subscription.Subscription;
import com.mans.imq.subscription.SubscriptionManager;

public class Runner {

	public static void main(String... args) throws InterruptedException {
		// Create Subscription manager
		SubscriptionManager subscriptionManager = new SubscriptionManager();

		// Create producers
		MessageQueue queue = new MessageQueue("queue1", 3);
		Producer producer1 = new Producer("producer1", queue, subscriptionManager);
		//Producer producer2 = new Producer("producer2", queue, subscriptionManager);

		// Start producers
		new Thread(producer1).start();
		//new Thread(producer2).start();
		Thread.sleep(5000);

		// Create subscription and start consumers
		Subscription subscription1 = new Subscription("httpCode:200");
		Consumer consumer1 = new Consumer("consumer1", queue, subscription1);
		subscriptionManager.subscribe(queue.getName(), consumer1);

		Subscription subscription2 = new Subscription("httpCode:400");
		Consumer consumer2 = new Consumer("consumer2", queue, subscription2);
		subscriptionManager.subscribe(queue.getName(), consumer2);


		/*Consumer consumer1 = new Consumer("consumer1", queue);
		Consumer consumer2 = new Consumer("consumer2", queue);*/
		/*new Thread(consumer1).start();
		new Thread(consumer2).start();*/
	}
}
