package org.teapotech.taskforce.event;

import org.teapotech.block.model.Block;

public interface BlockEventListenerFactory {

	BlockEventListener createBlockEventListener(String workspaceId, Block block);
}
