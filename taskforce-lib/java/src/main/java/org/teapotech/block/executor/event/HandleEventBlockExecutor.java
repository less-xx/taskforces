/**
 * 
 */
package org.teapotech.block.executor.event;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.amqp.AmqpException;
import org.teapotech.block.event.NamedBlockEvent;
import org.teapotech.block.exception.BlockExecutionException;
import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.Block.Next;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.support.BlockEventListenerSupport;
import org.teapotech.block.util.BlockExecutorUtils;
import org.teapotech.taskforce.event.BlockEventListener;

/**
 * @author jiangl
 *
 */
public class HandleEventBlockExecutor extends AbstractBlockExecutor implements BlockEventListenerSupport {

	private BlockEventListener blockEventListener;
	private final static int DEFAULT_TIMEOUT_SECONDS = 5;

	public HandleEventBlockExecutor(Block block) {
		super(block);
	}

	public HandleEventBlockExecutor(BlockValue blockValue) {
		super(blockValue);
	}

	@Override
	public void setBlockEventListener(BlockEventListener listener) {
		this.blockEventListener = listener;
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {

		String eventName = null;
		BlockValue evtNameBlock = this.block.getBlockValueByName("eventName", null);
		if (evtNameBlock != null) {
			eventName = (String) BlockExecutorUtils.execute(evtNameBlock, context);
		}
		if (StringUtils.isBlank(eventName)) {
			throw new BlockExecutionException("Missing event name");
		}
		String evtName = eventName.replace("\\s*", "_");
		String routingKey = "workspace." + context.getWorkspaceId() + "." + evtName;
		blockEventListener.initialize(routingKey);
		Logger LOG = context.getLogger();
		try {
			while (!context.isStopped()) {
				NamedBlockEvent evt = null;
				try {
					evt = blockEventListener.receive(DEFAULT_TIMEOUT_SECONDS);
				} catch (InterruptedException ie) {
					LOG.debug("Thread interruptted.");
				}
				if (evt == null) {
					continue;
				}
				LOG.info("Received event: {}", evt);

				Next next = this.block.getNext();
				if (next != null && next.getBlock() != null) {
					BlockExecutorUtils.execute(next.getBlock(), context);
				}
			}
			blockEventListener.destroy();
		} catch (AmqpException ie) {
			LOG.error(ie.getMessage());
		}
		return null;
	}

}
