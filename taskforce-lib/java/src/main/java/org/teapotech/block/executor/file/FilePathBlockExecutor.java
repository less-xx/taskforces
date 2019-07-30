/**
 * 
 */
package org.teapotech.block.executor.file;

import java.io.File;
import java.io.FileFilter;

import org.teapotech.block.exception.BlockExecutionException;
import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.BlockExecutionProgress.BlockStatus;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.model.Field;
import org.teapotech.block.support.CustomResourcePathLoader;
import org.teapotech.block.support.CustomResourcePathLoaderSupport;
import org.teapotech.block.util.BlockExecutorUtils;
import org.teapotech.taskforce.entity.FileSystemPath;

/**
 * @author lessdev
 *
 */
public class FilePathBlockExecutor extends AbstractBlockExecutor implements CustomResourcePathLoaderSupport {

	private CustomResourcePathLoader customResourcePathLoader;

	public FilePathBlockExecutor(Block block) {
		super(block);
	}

	public FilePathBlockExecutor(BlockValue blockValue) {
		super(blockValue);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {

		updateBlockStatus(context, BlockStatus.Running);

		Field field = null;
		if (this.block != null) {
			field = this.block.getFieldByName("customFilePathId", this.block.getFields().get(0));
		} else {
			field = this.shadow.getField();
		}
		String customFilePathId = field.getValue();
		FileSystemPath resPath = this.customResourcePathLoader.getFileSystemPathById(customFilePathId);
		if (resPath == null) {
			throw new BlockExecutionException("Cannot find custom file path by id " + customFilePathId);
		}
		File path = new File(resPath.getPath());
		if (!path.exists()) {
			throw new BlockExecutionException("File path does not exist. " + path.getAbsolutePath());
		}

		String include = ".*";
		BlockValue includeBV = this.block.getBlockValueByName("include", null);
		if (includeBV != null) {
			include = (String) BlockExecutorUtils.execute(includeBV, context);
		}

		final String pattern = include;
		File[] files = path.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.getName().matches(pattern);
			}
		});

		return files;
	}

	@Override
	public void setCustomResourcePathLoader(CustomResourcePathLoader loader) {
		this.customResourcePathLoader = loader;
	}

}
