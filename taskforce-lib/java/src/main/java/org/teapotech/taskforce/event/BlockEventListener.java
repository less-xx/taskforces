package org.teapotech.taskforce.event;

import org.teapotech.block.event.BlockEvent;

public interface BlockEventListener {

	String getId();

	String getRoutingKey();

	BlockEvent receive();

	void initialize();

	void destroy();
}
