/**
 * 
 */
package org.teapotech.block.def.file;

import org.teapotech.block.def.CustomBlockDefinition;
import org.teapotech.block.executor.BlockExecutor;
import org.teapotech.block.executor.event.EventHandlerBlockExecutor;

/**
 * @author jiangl
 *
 */
public class FileEventHandlerBlock extends CustomBlockDefinition {

	@Override
	public String getBlockType() {
		return "handle_file_event";
	}

	@Override
	public String getCategory() {
		return "Events";
	}

	@Override
	public Class<? extends BlockExecutor> getExecutorClass() {
		return EventHandlerBlockExecutor.class;
	}

}
