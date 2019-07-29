/**
 * 
 */
package org.teapotech.block.def.file;

import org.teapotech.block.def.Category;
import org.teapotech.block.def.CustomBlockDefinition;
import org.teapotech.block.executor.BlockExecutor;
import org.teapotech.block.executor.file.LoopFileBlockExecutor;

/**
 * @author jiangl
 *
 */
public class LoopFileBlock extends CustomBlockDefinition {

	@Override
	public String getBlockType() {
		return "loop_file";
	}

	@Override
	public String getCategory() {
		return Category.ID_FILE_OPERATIONS;
	}

	@Override
	public Class<? extends BlockExecutor> getExecutorClass() {
		return LoopFileBlockExecutor.class;
	}

}
