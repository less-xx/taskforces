package org.teapotech.block.def.variable;

import org.teapotech.block.def.Category;
import org.teapotech.block.def.CustomBlockDefinition;
import org.teapotech.block.executor.BlockExecutor;
import org.teapotech.block.executor.variable.LocalVariableGetBlockExecutor;

public class GetLocalVariableBlock extends CustomBlockDefinition {

	public final static String TYPE = "get_local_variable";

	@Override
	public String getBlockType() {
		return TYPE;
	}

	@Override
	public String getCategory() {
		return Category.ID_VARIABLES;
	}

	@Override
	public Class<? extends BlockExecutor> getExecutorClass() {
		return LocalVariableGetBlockExecutor.class;
	}

}
