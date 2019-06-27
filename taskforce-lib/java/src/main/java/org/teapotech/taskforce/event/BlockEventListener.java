package org.teapotech.taskforce.event;

import org.teapotech.block.event.NamedBlockEvent;

public interface BlockEventListener {

	String getId();

	String getRoutingKey();

	NamedBlockEvent receive(int timeoutSeconds) throws InterruptedException;

	void initialize(String routingKey);

	void destroy();
}
