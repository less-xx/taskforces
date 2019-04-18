/**
 * 
 */
package org.teapotech.block.executor.docker;

import org.teapotech.block.docker.DockerBlockDescriptor;
import org.teapotech.block.docker.DockerBlockUtil;
import org.teapotech.block.exception.BlockExecutionException;
import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;

import com.spotify.docker.client.DockerClient;

/**
 * @author jiangl
 *
 */
public class DockerBlockExecutor extends AbstractBlockExecutor {

	private final DockerBlockUtil taskmanager;

	/**
	 * @param block
	 */
	public DockerBlockExecutor(Block block, DockerClient dockerClient) {
		super(block);
		this.taskmanager = new DockerBlockUtil(dockerClient);
	}

	/**
	 * @param blockValue
	 */
	public DockerBlockExecutor(BlockValue blockValue, DockerClient dockerClient) {
		super(blockValue);
		this.taskmanager = new DockerBlockUtil(dockerClient);
	}

	public DockerBlockUtil getDockerTaskManager() {
		return this.taskmanager;
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {
		if (!(context instanceof DockerBlockExecutionContext)) {
			throw new BlockExecutionException(
					"The context type is " + context.getClass() + ", it should be "
							+ DockerBlockExecutionContext.class);
		}
		String blockType = this.block.getType();

		DockerBlockDescriptor td = this.taskmanager.getTaskDescriptorByName(blockType + ":active");
		if (td == null) {
			throw new BlockExecutionException("Cannot find task " + blockType);
		}
		taskmanager.runDockerTask(td);
		return null;
	}

}
