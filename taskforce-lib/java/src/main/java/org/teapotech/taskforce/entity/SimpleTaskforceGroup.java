package org.teapotech.taskforce.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "tf_group")
@Immutable
public class SimpleTaskforceGroup {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	public SimpleTaskforceGroup() {
	}

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

}
