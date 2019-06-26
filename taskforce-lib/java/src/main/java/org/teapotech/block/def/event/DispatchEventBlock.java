package org.teapotech.block.def.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teapotech.block.def.CustomBlockDefinition;
import org.teapotech.block.executor.BlockExecutor;
import org.teapotech.block.executor.file.CopyFileBlockExecutor;

public class DispatchEventBlock extends CustomBlockDefinition {

	private final static Logger LOG = LoggerFactory.getLogger(DispatchEventBlock.class);

	@Override
	public String getBlockType() {
		return "dispatch_event";
	}

	@Override
	public String getCategory() {
		return "Events";
	}

	@Override
	public Class<? extends BlockExecutor> getExecutorClass() {
		return CopyFileBlockExecutor.class;
	}

}
