package org.teapotech.block.def.event;

import org.teapotech.block.def.Category;
import org.teapotech.block.def.CustomBlockDefinition;
import org.teapotech.block.executor.BlockExecutor;
import org.teapotech.block.executor.event.HandleEventBlockExecutor;

public class ExitBlock extends CustomBlockDefinition {

	public final static String TYPE = "exit";

	@Override
	public String getBlockType() {
		return TYPE;
	}

	@Override
	public String getCategory() {
		return Category.START_STOP;
	}

	@Override
	public Class<? extends BlockExecutor> getExecutorClass() {
		return HandleEventBlockExecutor.class;
	}

}
