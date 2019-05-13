package org.teapotech.block.def;

import org.teapotech.block.executor.BlockExecutor;

public interface BlockDefinition {

	String getBlockDefinition();

	String getBlockType();

	String getCategory();

	Class<? extends BlockExecutor> getExecutorClass();
}
