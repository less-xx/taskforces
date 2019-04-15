/**
 * 
 */
package org.teapotech.taskforce.leader.docker;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.teapotech.taskforce.context.TaskforceContext;
import org.teapotech.taskforce.dto.TaskDescriptor;
import org.teapotech.taskforce.exception.TaskDescriptorException;
import org.teapotech.taskforce.exception.TaskExecutionException;
import org.teapotech.taskforce.leader.TaskForceLeader;

import com.google.common.collect.ImmutableList;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Image;

/**
 * @author jiangl
 *
 */
public class TaskforceLeaderDockerImpl implements TaskForceLeader {

	public static String LABEL_TASK_NAME = "taskforce.task.name";

	final TaskforceContext taskforceContext;
	final DockerClient dockerClient;

	public TaskforceLeaderDockerImpl(TaskforceContext context, DockerClient dockerClient) {
		this.taskforceContext = context;
		this.dockerClient = dockerClient;
	}

	@Override
	public TaskforceContext getContext() {
		return this.taskforceContext;
	}

	@Override
	public Collection<TaskDescriptor> getAllTaskDescriptors() throws TaskDescriptorException {
		List<Image> images;
		try {
			images = dockerClient.listImages(DockerClient.ListImagesParam.withLabel(LABEL_TASK_NAME));

			Map<String, TaskDescriptor> results = new HashMap<>();
			for (Image image : images) {
				ImmutableList<String> tags = image.repoTags();
				if (tags == null) {
					continue;
				}

				for (String tag : tags) {
					String[] tagArray = tag.split(":");
					Boolean active = null;
					String version = null;
					if (tagArray.length > 1) {
						if ("active".equalsIgnoreCase(tagArray[1])) {
							active = true;
						} else {
							version = tagArray[1];
						}
					}
					TaskDescriptor td = results.get(image.id());
					if (td == null) {
						td = new TaskDescriptor();
						td.setId(image.id());
						td.setCreatedTime(image.created());
						td.setName(tagArray[0]);
						results.put(td.getId(), td);
					}
					if (active != null) {
						td.setActive(active);
					}
					if (version != null) {
						td.setVersion(version);
					}
				}
			}
			return results.values();
		} catch (DockerException | InterruptedException e) {
			throw new TaskDescriptorException(e.getMessage(), e);
		}
	}

	@Override
	public TaskDescriptor getTaskDescriptorByName(String name) throws TaskDescriptorException {
		try {
			List<Image> images = dockerClient.listImages(DockerClient.ListImagesParam.byName(name));
			if (images == null || images.isEmpty()) {
				return null;
			}
			Image img = images.get(0);
			TaskDescriptor td = new TaskDescriptor();
			td.setId(img.id());
			td.setCreatedTime(img.created());
			for (String tag : img.repoTags()) {
				String[] tagArray = tag.split(":");
				Boolean active = null;
				String version = null;
				if (tagArray.length > 1) {
					if ("active".equalsIgnoreCase(tagArray[1])) {
						active = true;
					} else {
						version = tagArray[1];
					}
				}
				td.setName(tagArray[0]);
				if (active != null) {
					td.setActive(active);
				}
				if (version != null) {
					td.setVersion(version);
				}
			}
			return td;
		} catch (DockerException | InterruptedException e) {
			throw new TaskDescriptorException(e.getMessage(), e);
		}
	}

	@Override
	public void execute(TaskDescriptor taskDescriptor) throws TaskExecutionException {
		// TODO Auto-generated method stub

	}

}
