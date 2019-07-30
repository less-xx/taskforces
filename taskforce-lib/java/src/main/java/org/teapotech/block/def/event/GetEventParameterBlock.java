package org.teapotech.block.def.event;

import org.teapotech.block.def.Category;
import org.teapotech.block.def.CustomBlockDefinition;
import org.teapotech.block.executor.BlockExecutor;
import org.teapotech.block.executor.event.GetEventParamBlockExecutor;

public class GetEventParameterBlock extends CustomBlockDefinition {

	public final static String TYPE = "get_event_param";

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
		return GetEventParamBlockExecutor.class;
	}

}
