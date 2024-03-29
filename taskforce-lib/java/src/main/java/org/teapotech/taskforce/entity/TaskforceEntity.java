/**
 * 
 */
package org.teapotech.taskforce.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author jiangl
 *
 */
@Entity
@Table(name = "tf_entity")
public class TaskforceEntity {

	@Id
	@GeneratedValue(generator = "tf-entity-id-generator")
	@GenericGenerator(name = "tf-entity-id-generator", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id")
	private String id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(name = "description", nullable = true)
	private String description;

	@Column(name = "tf_config", columnDefinition = "TEXT", nullable = false)
	private String configuration;

	@Column(name = "last_updated")
	@UpdateTimestamp
	private Date lastUpdatedTime;

	@Version
	@Column(name = "version", columnDefinition = "int DEFAULT 0", nullable = false)
	private int version;

	@Column(name = "updated_by")
	private String updatedBy;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "group_id")
	TaskforceGroup group;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getConfiguration() {
		return configuration;
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	public Date getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(Date lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public TaskforceGroup getGroup() {
		return group;
	}

	public void setGroup(TaskforceGroup group) {
		this.group = group;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public SimpleTaskforceEntity toSimple() {
		SimpleTaskforceEntity ste = new SimpleTaskforceEntity();
		ste.setDescription(this.description);
		ste.setGroup(this.group.toSimple());
		ste.setId(this.id);
		ste.setLastUpdatedTime(this.lastUpdatedTime);
		ste.setName(this.name);
		ste.setUpdatedBy(this.updatedBy);
		return ste;
	}
}
