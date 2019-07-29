package org.teapotech.block.def.start_stop;

import org.teapotech.block.def.Category;
import org.teapotech.block.def.CustomBlockDefinition;
import org.teapotech.block.executor.BlockExecutor;
import org.teapotech.block.executor.start_stop.StartBlockExecutor;

public class StartBlock extends CustomBlockDefinition {

	public final static String TYPE = "start";

	@Override
	public String getBlockType() {
		return TYPE;
	}

	@Override
	public String getCategory() {
		return Category.ID_START_STOP;
	}

	@Override
	public Class<? extends BlockExecutor> getExecutorClass() {
		return StartBlockExecutor.class;
	}

}
