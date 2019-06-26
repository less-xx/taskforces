package org.teapotech.block;

import com.spotify.docker.client.DockerClient;

public class DependencyRepository {

	private DockerClient dockerClient;

	public DockerClient getDockerClient() {
		return dockerClient;
	}

	public void setDockerClient(DockerClient dockerClient) {
		this.dockerClient = dockerClient;
	}

}
