package org.teapotech.taskforce.config.docker;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConditionalOnProperty(name = "taskforce.execution.driver", havingValue = "docker", matchIfMissing = false)
@ConfigurationProperties("taskforce.execution.docker")
public class TaskforceExecutionDockerDriverProperties {

	private String url;

	private boolean pullImageEnabled = true;

	private boolean hostNetworkMode = false;

	private String apiVersion = "1.23";

	private String registryUrl;

	private String registryUsername;

	private String registryPassword;

	private String logAgentAddress;

	private String containerAdditionalLinks;

	private int readTimeout = 0;

	private int connectionTimeout = 5 * 1000;

	private int connectionPoolSize = 100;

	private int threadPoolMaxSize = 50;

	private int threadPoolKeepAlive = 30;

	private int eventThreadPoolMaxSize = 100;

	private int eventThreadPoolKeepAlive = 30;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isPullImageEnabled() {
		return pullImageEnabled;
	}

	public void setPullImageEnabled(boolean pullImageEnabled) {
		this.pullImageEnabled = pullImageEnabled;
	}

	public boolean isHostNetworkMode() {
		return hostNetworkMode;
	}

	public void setHostNetworkMode(boolean hostNetworkMode) {
		this.hostNetworkMode = hostNetworkMode;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getRegistryUrl() {
		return registryUrl;
	}

	public void setRegistryUrl(String registryUrl) {
		this.registryUrl = registryUrl;
	}

	public String getRegistryUsername() {
		return registryUsername;
	}

	public void setRegistryUsername(String registryUsername) {
		this.registryUsername = registryUsername;
	}

	public String getRegistryPassword() {
		return registryPassword;
	}

	public void setRegistryPassword(String registryPassword) {
		this.registryPassword = registryPassword;
	}

	public String getLogAgentAddress() {
		return logAgentAddress;
	}

	public void setLogAgentAddress(String logAgentAddress) {
		this.logAgentAddress = logAgentAddress;
	}

	public String getContainerAdditionalLinks() {
		return containerAdditionalLinks;
	}

	public void setContainerAdditionalLinks(String containerAdditionalLinks) {
		this.containerAdditionalLinks = containerAdditionalLinks;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getConnectionPoolSize() {
		return connectionPoolSize;
	}

	public void setConnectionPoolSize(int connectionPoolSize) {
		this.connectionPoolSize = connectionPoolSize;
	}

	public int getThreadPoolMaxSize() {
		return threadPoolMaxSize;
	}

	public void setThreadPoolMaxSize(int threadPoolMaxSize) {
		this.threadPoolMaxSize = threadPoolMaxSize;
	}

	public int getThreadPoolKeepAlive() {
		return threadPoolKeepAlive;
	}

	public void setThreadPoolKeepAlive(int threadPoolKeepAlive) {
		this.threadPoolKeepAlive = threadPoolKeepAlive;
	}

	public int getEventThreadPoolMaxSize() {
		return eventThreadPoolMaxSize;
	}

	public void setEventThreadPoolMaxSize(int eventThreadPoolMaxSize) {
		this.eventThreadPoolMaxSize = eventThreadPoolMaxSize;
	}

	public int getEventThreadPoolKeepAlive() {
		return eventThreadPoolKeepAlive;
	}

	public void setEventThreadPoolKeepAlive(int eventThreadPoolKeepAlive) {
		this.eventThreadPoolKeepAlive = eventThreadPoolKeepAlive;
	}

}
