package org.teapotech.block.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 
 * @author jiangl
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "blockly.field")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Field {

	@XmlAttribute
	private String id;

	@XmlAttribute
	private String name;

	@XmlAttribute(name = "variabletype")
	private String variableType;

	@XmlValue
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getVariableType() {
		return variableType;
	}

	public void setVariableType(String variableType) {
		this.variableType = variableType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
