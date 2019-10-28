package org.teapotech.block.def.control;

import org.teapotech.block.def.Category;
import org.teapotech.block.def.CustomBlockDefinition;
import org.teapotech.block.executor.BlockExecutor;
import org.teapotech.block.executor.control.WaitSecondsBlockExecutor;

public class WaitSecondsBlock extends CustomBlockDefinition {

	public final static String TYPE = "wait_seconds";

	@Override
	public String getBlockType() {
		return TYPE;
	}

	@Override
	public String getCategory() {
		return Category.ID_BASIC + "/" + Category.ID_CONTROL;
	}

	@Override
	public Class<? extends BlockExecutor> getExecutorClass() {
		return WaitSecondsBlockExecutor.class;
	}

}
