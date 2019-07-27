package org.teapotech.block.def.event;

import org.teapotech.block.def.Category;
import org.teapotech.block.def.CustomBlockDefinition;
import org.teapotech.block.executor.BlockExecutor;
import org.teapotech.block.executor.event.EventWithParamBlockExecutor;

public class EventWithParameterBlock extends CustomBlockDefinition {

	public final static String TYPE = "event_with_param";

	@Override
	public String getBlockType() {
		return TYPE;
	}

	@Override
	public String getCategory() {
		return Category.EVENTS;
	}

	@Override
	public Class<? extends BlockExecutor> getExecutorClass() {
		return EventWithParamBlockExecutor.class;
	}

}
