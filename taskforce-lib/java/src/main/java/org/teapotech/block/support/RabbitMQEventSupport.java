package org.teapotech.block.support;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

/**
 * 
 * @author jiangl
 *
 */
public interface RabbitMQEventSupport {

	void setRabbitAdmin(RabbitAdmin rabbitAdmin);

	void setEventExchange(TopicExchange eventExchange);
}