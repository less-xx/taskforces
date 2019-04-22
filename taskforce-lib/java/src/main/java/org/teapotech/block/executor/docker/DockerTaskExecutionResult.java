/**
 * 
 */
package org.teapotech.block.executor.docker;

import java.util.Date;

/**
 * @author jiangl
 *
 */
public class DockerTaskExecutionResult {
	private Date createdTime;
	private String errorText;
	private String errorDescription;
	private Date startedAt;
	private Long exitCode;
	private String status;
	private String logs;
	private Date endAt;
	private String containerId;
	private DockerBlockValueType outputValueType;
	private Object outputValue;

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public Date getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}

	public Long getExitCode() {
		return exitCode;
	}

	public void setExitCode(Long exitCode) {
		this.exitCode = exitCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLogs() {
		return logs;
	}

	public void setLogs(String logs) {
		this.logs = logs;
	}

	public Date getEndAt() {
		return endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}

	public String getContainerId() {
		return containerId;
	}

	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	public Object getOutputValue() {
		return outputValue;
	}

	public void setOutputValue(Object outputValue) {
		this.outputValue = outputValue;
	}

	public void setOutputValueType(DockerBlockValueType outputValueType) {
		this.outputValueType = outputValueType;
	}

	public DockerBlockValueType getOutputValueType() {
		return outputValueType;
	}

}
