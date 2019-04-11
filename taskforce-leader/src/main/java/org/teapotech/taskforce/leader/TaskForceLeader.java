/**
 * 
 */
package org.teapotech.taskforce.leader;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.teapotech.taskforce.context.TaskforceContext;

import com.spotify.docker.client.DockerClient;

/**
 * @author jiangl
 *
 */
@Component
public class TaskForceLeader {

	private static Logger LOG = LoggerFactory.getLogger(TaskForceLeader.class);

	@Autowired
	TaskforceContext taskforceContext;

	@Autowired
	DockerClient dockerClient;

	@PostConstruct
	void init() {
		String taskforceId = taskforceContext.getTaskforceId();
		LOG.info("Taskforce leader started, id: {}", taskforceId);
	}
}
