package org.teapotech.block.def.event;

import org.teapotech.block.def.Category;
import org.teapotech.block.def.CustomBlockDefinition;
import org.teapotech.block.executor.BlockExecutor;
import org.teapotech.block.executor.event.HandleEventBlockExecutor;

public class HandleEventBlock extends CustomBlockDefinition {

	public final static String TYPE = "handle_event";

	@Override
	public String getBlockType() {
		return TYPE;
	}

	@Override
	public String getCategory() {
		return Category.ID_EVENTS;
	}

	@Override
	public Class<? extends BlockExecutor> getExecutorClass() {
		return HandleEventBlockExecutor.class;
	}

}
