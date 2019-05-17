package org.teapotech.block.model.toolbox;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.teapotech.block.model.Field;
import org.teapotech.block.model.Statement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ToolboxBlock {

	@XmlAttribute
	private String type;

	@XmlElement(name = "value", namespace = "")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<ToolboxBlockValue> values;

	@XmlElement(name = "statement", namespace = "")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Statement> statements;

	@XmlElement(name = "field", namespace = "")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Field> fields;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<ToolboxBlockValue> getValues() {
		return values;
	}

	public void setValues(List<ToolboxBlockValue> values) {
		this.values = values;
	}

	public List<Statement> getStatements() {
		return statements;
	}

	public void setStatements(List<Statement> statements) {
		this.statements = statements;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

}
