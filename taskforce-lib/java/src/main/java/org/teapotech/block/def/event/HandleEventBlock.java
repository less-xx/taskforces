package org.teapotech.block.def.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teapotech.block.def.CustomBlockDefinition;
import org.teapotech.block.executor.BlockExecutor;
import org.teapotech.block.executor.event.HandleEventBlockExecutor;

public class HandleEventBlock extends CustomBlockDefinition {

	private final static Logger LOG = LoggerFactory.getLogger(HandleEventBlock.class);

	@Override
	public String getBlockType() {
		return "handle_event";
	}

	@Override
	public String getCategory() {
		return "Events";
	}

	@Override
	public Class<? extends BlockExecutor> getExecutorClass() {
		return HandleEventBlockExecutor.class;
	}

}
