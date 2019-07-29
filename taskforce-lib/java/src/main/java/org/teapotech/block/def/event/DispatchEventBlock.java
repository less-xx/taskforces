package org.teapotech.block.def.event;

import org.teapotech.block.def.Category;
import org.teapotech.block.def.CustomBlockDefinition;
import org.teapotech.block.executor.BlockExecutor;
import org.teapotech.block.executor.event.DispatchEventBlockExecutor;

public class DispatchEventBlock extends CustomBlockDefinition {

	@Override
	public String getBlockType() {
		return "dispatch_event";
	}

	@Override
	public String getCategory() {
		return Category.ID_EVENTS;
	}

	@Override
	public Class<? extends BlockExecutor> getExecutorClass() {
		return DispatchEventBlockExecutor.class;
	}

}
