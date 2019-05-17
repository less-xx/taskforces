/**
 * 
 */
package org.teapotech.block.model.toolbox;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

/**
 * @author jiangl
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ToolboxBlockValue {

	@XmlAttribute
	private String name;

	@XmlElement(name = "shadow", namespace = "")
	private Shadow shadow;

	@XmlElement(namespace = "")
	@JacksonXmlElementWrapper(useWrapping = false)
	private ToolboxBlock block;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Shadow getShadow() {
		return shadow;
	}

	public void setShadow(Shadow shadow) {
		this.shadow = shadow;
	}

	public ToolboxBlock getBlock() {
		return block;
	}

	public void setBlock(ToolboxBlock block) {
		this.block = block;
	}

}
