package org.teapotech.taskforce.task.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("taskforce.execution")
public class TaskforceExecutionProperties {

	private String homeDir;
	private String logPattern;

	public String getHomeDir() {
		return homeDir;
	}

	public void setHomeDir(String homeDir) {
		this.homeDir = homeDir;
	}

	public String getLogPattern() {
		return logPattern;
	}

	public void setLogPattern(String logPattern) {
		this.logPattern = logPattern;
	}

}
