/**
 * 
 */
package org.teapotech.application.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author jiangl
 *
 */
@Entity
@Table(name = "app_config")
public class AppConfiguration {

	@Id
	@Column(name = "config_id")
	private String configId;

	@Column(name = "config_value", columnDefinition = "TEXT", nullable = false)
	private String value;

	@Column(name = "last_updated")
	@UpdateTimestamp
	private Date lastUpdatedTime;

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(Date lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

}
