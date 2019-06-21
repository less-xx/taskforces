package org.teapotech.block;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

import com.spotify.docker.client.DockerClient;

public class DependencyRepository {

	private DockerClient dockerClient;
	private TopicExchange eventExchange;
	private RabbitAdmin rabbitAdmin;

	public DockerClient getDockerClient() {
		return dockerClient;
	}

	public void setDockerClient(DockerClient dockerClient) {
		this.dockerClient = dockerClient;
	}

	public TopicExchange getEventExchange() {
		return eventExchange;
	}

	public void setEventExchange(TopicExchange eventExchange) {
		this.eventExchange = eventExchange;
	}

	public RabbitAdmin getRabbitAdmin() {
		return rabbitAdmin;
	}

	public void setRabbitAdmin(RabbitAdmin rabbitAdmin) {
		this.rabbitAdmin = rabbitAdmin;
	}

}
