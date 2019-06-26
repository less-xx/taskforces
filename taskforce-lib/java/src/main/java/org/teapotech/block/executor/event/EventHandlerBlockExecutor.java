/**
 * 
 */
package org.teapotech.block.executor.event;

import org.springframework.amqp.AmqpException;
import org.teapotech.block.event.BlockEvent;
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
public class EventHandlerBlockExecutor extends AbstractBlockExecutor implements BlockEventListenerSupport {

	private BlockEventListener blockEventListener;

	public EventHandlerBlockExecutor(Block block) {
		super(block);
	}

	public EventHandlerBlockExecutor(BlockValue blockValue) {
		super(blockValue);
	}

	@Override
	public void setBlockEventListener(BlockEventListener listener) {
		this.blockEventListener = listener;
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {

		blockEventListener.initialize();

		try {
			while (!context.isStopped()) {
				BlockEvent evt = blockEventListener.receive();
				if (evt == null) {
					continue;
				}
				LOG.info("Received event: {}", evt);

				Next next = this.block.getNext();
				if (next != null && next.getBlock() != null) {
					BlockExecutorUtils.execute(next.getBlock(), context);
				}
			}
		} catch (AmqpException ie) {
			LOG.error(ie.getMessage());
		}
		blockEventListener.destroy();
		return null;
	}

}
