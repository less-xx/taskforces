package org.teapotech.taskforce.event;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teapotech.block.event.NamedBlockEvent;

public class SimpleBlockEventListener implements BlockEventListener {

	private static Logger LOG = LoggerFactory.getLogger(SimpleBlockEventListener.class);

	private SimpleEventExchange eventExchange;
	private final BlockingQueue<NamedBlockEvent> eventQueue = new LinkedBlockingQueue<>();
	private String id;
	private String routingKey;

	public void addEvent(NamedBlockEvent event) {
		eventQueue.add(event);
	}

	@Override
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getRoutingKey() {
		return this.routingKey;
	}

	@Override
	public NamedBlockEvent receive(int timeoutSeconds) throws InterruptedException {
		return eventQueue.poll(timeoutSeconds, TimeUnit.SECONDS);
	}

	void setEventExchange(SimpleEventExchange eventExchange) {
		this.eventExchange = eventExchange;
	}

	@Override
	public void initialize(String routingKey) {
		this.routingKey = routingKey;
		if (this.eventExchange != null) {
			this.eventExchange.addBlockEventListener(this);
		}
		LOG.info("Initialized event listener, routing key: {}", routingKey);

	}

	@Override
	public void destroy() {
		if (this.eventExchange != null) {
			this.eventExchange.removeBlockEventListener(this);
		}
	}

}
