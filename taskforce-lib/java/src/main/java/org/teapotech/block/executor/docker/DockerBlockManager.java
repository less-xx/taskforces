/**
 * 
 */
package org.teapotech.block.executor.docker;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.teapotech.block.docker.DockerBlockDescriptor;
import org.teapotech.block.exception.DockerBlockException;

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
	public static String LABEL_TASK_CATEGORY = "taskforce.task.category";
	public static String LABEL_TASK_DEFINITION = "taskforce.task.def";

	final DockerClient dockerClient;

	public DockerBlockManager(DockerClient dockerClient) {
		this.dockerClient = dockerClient;
	}

	public Collection<DockerBlockDescriptor> getAllTaskDescriptors() throws DockerBlockException {
		List<Image> images;
		try {
			images = dockerClient.listImages(DockerClient.ListImagesParam.withLabel(LABEL_TASK_NAME),
					DockerClient.ListImagesParam.danglingImages(false));

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
					String category = image.labels().get(LABEL_TASK_CATEGORY);
					if (category != null) {
						td.setCategory(category);
					}

					String def = image.labels().get(LABEL_TASK_DEFINITION);
					if (def != null) {
						td.setDefinition(def);
					}
				}
			}
			return results.values();
		} catch (DockerException | InterruptedException e) {
			throw new DockerBlockException(e.getMessage(), e);
		}
	}

	public DockerBlockDescriptor getTaskDescriptorByName(String name) throws DockerBlockException {
		try {
			List<Image> images = dockerClient.listImages(DockerClient.ListImagesParam.byName(name));
			if (images == null || images.isEmpty()) {
				throw new DockerBlockException("Docker task image does not exist. Name: " + name);
			}
			Image img = images.get(0);
			return buildDescriptor(img);
		} catch (DockerException | InterruptedException e) {
			throw new DockerBlockException(e.getMessage(), e);
		}
	}

	private DockerBlockDescriptor buildDescriptor(Image img) {
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
		String category = img.labels().get(LABEL_TASK_CATEGORY);
		if (category != null) {
			td.setCategory(category);
		}
		String def = img.labels().get(LABEL_TASK_DEFINITION);
		if (def != null) {
			td.setDefinition(def);
		}
		return td;
	}

}
