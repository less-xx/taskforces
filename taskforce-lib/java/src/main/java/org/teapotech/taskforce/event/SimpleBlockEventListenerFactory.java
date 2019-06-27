package org.teapotech.taskforce.event;

import org.teapotech.block.model.Block;

public class SimpleBlockEventListenerFactory implements BlockEventListenerFactory {

	private final SimpleEventExchange eventExchange;

	public SimpleBlockEventListenerFactory(SimpleEventExchange eventExchange) {
		this.eventExchange = eventExchange;
	}

	@Override
	public BlockEventListener createBlockEventListener(String workspaceId, Block block) {
		SimpleBlockEventListener listener = new SimpleBlockEventListener();
		listener.setEventExchange(eventExchange);
		listener.setId(block.getType() + "." + block.getId());
		return listener;
	}

}
