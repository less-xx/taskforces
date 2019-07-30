/**
 * 
 */
package org.teapotech.block.def.file;

import org.teapotech.block.def.Category;
import org.teapotech.block.def.CustomBlockDefinition;
import org.teapotech.block.executor.BlockExecutor;
import org.teapotech.block.executor.file.FileAtPathBlockExecutor;

/**
 * @author jiangl
 *
 */
public class FileAtPathBlock extends CustomBlockDefinition {

	@Override
	public String getBlockType() {
		return "file_at_path";
	}

	@Override
	public String getCategory() {
		return Category.ID_FILE_OPERATIONS;
	}

	@Override
	public Class<? extends BlockExecutor> getExecutorClass() {
		return FileAtPathBlockExecutor.class;
	}

}
