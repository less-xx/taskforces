/**
 * 
 */
package org.teapotech.taskforce.block.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author jiangl
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Shadow {

	@XmlAttribute
	private String type;

	@XmlAttribute
	private String id;

	@XmlElement(namespace = Workspace.NAMESPACE)
	private Field field;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

}
