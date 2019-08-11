/**
 * 
 */
package org.teapotech.block.def.file;

import org.teapotech.block.def.Category;
import org.teapotech.block.def.CustomBlockDefinition;
import org.teapotech.block.executor.BlockExecutor;
import org.teapotech.block.executor.file.GetFilePathBlockExecutor;

/**
 * @author jiangl
 *
 */
public class GetFilePathBlock extends CustomBlockDefinition {

	@Override
	public String getBlockType() {
		return "get_file_path";
	}

	@Override
	public String getCategory() {
		return Category.ID_FILE_OPERATIONS;
	}

	@Override
	public Class<? extends BlockExecutor> getExecutorClass() {
		return GetFilePathBlockExecutor.class;
	}

}
