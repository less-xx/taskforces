/**
 * 
 */
package org.teapotech.block.executor.docker;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.teapotech.block.docker.DockerBlockDescriptor;
import org.teapotech.block.exception.InvalidBlockException;

import com.google.common.collect.ImmutableList;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Image;

/**
 * @author jiangl
 *
 */
public class DockerBlockManager {

	public static String LABEL_TASK_NAME = "taskforce.task.name";

	final DockerClient dockerClient;

	public DockerBlockManager(DockerClient dockerClient) {
		this.dockerClient = dockerClient;
	}

	public Collection<DockerBlockDescriptor> getAllTaskDescriptors() throws InvalidBlockException {
		List<Image> images;
		try {
			images = dockerClient.listImages(DockerClient.ListImagesParam.withLabel(LABEL_TASK_NAME));

			Map<String, DockerBlockDescriptor> results = new HashMap<>();
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
					DockerBlockDescriptor td = results.get(image.id());
					if (td == null) {
						td = new DockerBlockDescriptor();
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
			throw new InvalidBlockException(e.getMessage(), e);
		}
	}

	public DockerBlockDescriptor getTaskDescriptorByName(String name) throws InvalidBlockException {
		try {
			List<Image> images = dockerClient.listImages(DockerClient.ListImagesParam.byName(name));
			if (images == null || images.isEmpty()) {
				throw new InvalidBlockException("Docker task image does not exist. Name: " + name);
			}
			Image img = images.get(0);
			DockerBlockDescriptor td = new DockerBlockDescriptor();
			td.setId(img.id());
			td.setCreatedTime(img.created());
			for (String tag : img.repoTags()) {
				String[] tagArray = tag.split(":");
				Boolean active = null;
				String version = null;
				if (tagArray.length > 1) {
					if ("active".equalsIgnoreCase(tagArray[1])) {
						active = true;
						version = "active";
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
			throw new InvalidBlockException(e.getMessage(), e);
		}
	}

}
