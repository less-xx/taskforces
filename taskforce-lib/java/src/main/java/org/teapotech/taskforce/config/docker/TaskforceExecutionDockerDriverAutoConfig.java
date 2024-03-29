package org.teapotech.taskforce.config.docker;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.auth.RegistryAuthSupplier;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.RegistryAuth;
import com.spotify.docker.client.messages.RegistryConfigs;

@Configuration
@EnableConfigurationProperties(TaskforceExecutionDockerDriverProperties.class)
@ConditionalOnProperty(name = "taskforce.execution.driver", havingValue = "docker", matchIfMissing = false)
public class TaskforceExecutionDockerDriverAutoConfig {

	@Autowired
	TaskforceExecutionDockerDriverProperties dockerDriverProps;

	@Bean
	@ConditionalOnMissingBean
	public DockerClient getDockerClient()
			throws DockerCertificateException {
		final RegistryAuth registryAuth = RegistryAuth.builder()
				.username(dockerDriverProps.getRegistryUsername())
				.password(dockerDriverProps.getRegistryPassword())
				.serverAddress(dockerDriverProps.getRegistryUrl())
				.build();

		RegistryAuthSupplier registryAuthSupplier = new RegistryAuthSupplier() {
			@Override
			public RegistryAuth authFor(final String imageName)
					throws DockerException {
				if (imageName != null && imageName.startsWith(
						dockerDriverProps.getRegistryUrl())) {
					return registryAuth;
				} else {
					return null;
				}
			}

			@Override
			public RegistryAuth authForSwarm() {
				return null;
			}

			@Override
			public RegistryConfigs authForBuild() throws DockerException {
				return null;
			}
		};
		DefaultDockerClient.Builder dockerClientBuilder = StringUtils
				.isBlank(dockerDriverProps.getUrl())
						? DefaultDockerClient.fromEnv()
						: DefaultDockerClient.builder()
								.uri(dockerDriverProps.getUrl());
		return dockerClientBuilder
				.registryAuthSupplier(registryAuthSupplier)
				.connectTimeoutMillis(
						dockerDriverProps.getConnectionTimeout())
				.readTimeoutMillis(
						dockerDriverProps.getReadTimeout())
				.connectionPoolSize(
						dockerDriverProps.getConnectionPoolSize())
				.build();
	}

}
