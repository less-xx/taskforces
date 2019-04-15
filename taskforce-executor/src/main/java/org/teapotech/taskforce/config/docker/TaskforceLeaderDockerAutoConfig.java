package org.teapotech.taskforce.config.docker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.teapotech.taskforce.context.TaskforceContext;
import org.teapotech.taskforce.leader.TaskForceLeader;
import org.teapotech.taskforce.leader.docker.TaskforceLeaderDockerImpl;

import com.spotify.docker.client.DockerClient;

@Configuration
@ConditionalOnProperty(name = "taskforce.execution.driver", havingValue = "docker", matchIfMissing = false)
public class TaskforceLeaderDockerAutoConfig {

	@Autowired
	DockerClient dockerClient;

	@Autowired
	TaskforceContext taskforceContext;

	@Bean
	TaskForceLeader taskforceLeader() {

		TaskForceLeader tfl = new TaskforceLeaderDockerImpl(taskforceContext, dockerClient);
		return tfl;
	}
}
