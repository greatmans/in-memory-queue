package com.mans.imq.subscription;

public class Subscription {

	private String expression;
	private SubscriptionStatus status;

	public Subscription() {
		this.expression = "*";
		this.status = SubscriptionStatus.ACTIVE;
	}

	public Subscription(String expression) {
		this.expression = expression;
		this.status = SubscriptionStatus.ACTIVE;
	}

	public Subscription(String expression, SubscriptionStatus status) {
		this.expression = expression;
		this.status = status;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
}
