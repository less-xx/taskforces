/**
 * 
 */
package org.teapotech.block.executor.file;

import org.teapotech.block.exception.BlockExecutionException;
import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.model.Field;
import org.teapotech.block.support.CustomResourcePathLoader;
import org.teapotech.block.support.CustomResourcePathLoaderSupport;
import org.teapotech.taskforce.entity.CustomResourcePath;

/**
 * @author lessdev
 *
 */
public class ResourcePathBlockExecutor extends AbstractBlockExecutor implements CustomResourcePathLoaderSupport {

	private CustomResourcePathLoader customResourcePathLoader;

	public ResourcePathBlockExecutor(Block block) {
		super(block);
	}

	public ResourcePathBlockExecutor(BlockValue blockValue) {
		super(blockValue);
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {
		Field field = null;
		if (this.block != null) {
			field = this.block.getFieldByName("customResourcePathId", this.block.getFields().get(0));
		} else {
			field = this.shadow.getField();
		}
		String customResourcePathId = field.getValue();
		CustomResourcePath resPath = this.customResourcePathLoader.getCustomResourcePathById(customResourcePathId);
		if (resPath == null) {
			throw new BlockExecutionException("Cannot find custom resource path by id " + customResourcePathId);
		}
		return resPath;
	}

	@Override
	public void setCustomResourcePathLoader(CustomResourcePathLoader loader) {
		this.customResourcePathLoader = loader;
	}

}
