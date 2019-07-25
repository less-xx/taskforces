/**
 * 
 */
package org.teapotech.block.executor.file;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.teapotech.block.exception.BlockExecutionException;
import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
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
public class CopyFileBlockExecutor extends AbstractBlockExecutor implements CustomResourcePathLoaderSupport {

	private CustomResourcePathLoader customResourcePathLoader;

	public CopyFileBlockExecutor(Block block) {
		super(block);
	}

	public CopyFileBlockExecutor(BlockValue blockValue) {
		super(blockValue);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {

		Logger LOG = context.getLogger();

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
		File toPath = new File(resPath.getPath());
		if (!toPath.exists()) {
			throw new BlockExecutionException("File path does not exist. " + toPath.getAbsolutePath());
		}
		BlockValue bv = this.block.getBlockValueByName("files", this.block.getValues().get(0));
		File[] files = (File[]) BlockExecutorUtils.execute(bv.getBlock(), context);
		for (File f : files) {
			FileUtils.copyFileToDirectory(f, toPath);
			LOG.info("Copied file {} to {}", f.getName(), resPath.getName());
			// FileEvent evt = new FileEvent(context.getWorkspaceId(), this.block.getType(),
			// this.block.getId());
			// evt.setFilePath(f.getAbsolutePath());
			// evt.setOperation(Operation.Create);
			// context.getBlockEventDispatcher().dispatchBlockEvent(evt);
		}
		return null;
	}

	@Override
	public void setCustomResourcePathLoader(CustomResourcePathLoader loader) {
		this.customResourcePathLoader = loader;
	}

}
