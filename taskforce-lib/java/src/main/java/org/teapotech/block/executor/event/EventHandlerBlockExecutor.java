/**
 * 
 */
package org.teapotech.block.executor.event;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.teapotech.block.executor.AbstractBlockExecutor;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.support.RabbitMQEventSupport;

/**
 * @author jiangl
 *
 */
public class EventHandlerBlockExecutor extends AbstractBlockExecutor implements RabbitMQEventSupport {

	private RabbitAdmin rabbitAdmin;
	private Exchange eventExchange;

	public EventHandlerBlockExecutor(Block block) {
		super(block);
	}

	public EventHandlerBlockExecutor(BlockValue blockValue) {
		super(blockValue);
	}

	@Override
	public void setRabbitAdmin(RabbitAdmin rabbitAdmin) {
		this.rabbitAdmin = rabbitAdmin;
	}

	@Override
	public void setEventExchange(Exchange eventExchange) {
		this.eventExchange = eventExchange;
	}

	@Override
	protected Object doExecute(BlockExecutionContext context) throws Exception {
		String workspaceId = context.getWorkspaceId();
		return null;
	}

	private void initQueue() {

	}

}
