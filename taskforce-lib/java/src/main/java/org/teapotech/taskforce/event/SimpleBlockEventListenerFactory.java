package org.teapotech.taskforce.event;

import org.teapotech.block.model.Block;

public class SimpleBlockEventListenerFactory implements BlockEventListenerFactory {

	private final SimpleEventExchange exchange = new SimpleEventExchange();

	@Override
	public BlockEventListener createBlockEventListener(String workspaceId, Block block) {
		return null;
	}

}
