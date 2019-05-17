/**
 * 
 */
package org.teapotech.block.model.toolbox;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author jiangl
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
@XmlRootElement(name = "xml", namespace = "")
public class ToolboxBlockWrapper {

	@XmlElement(name = "block", namespace = "")
	private ToolboxBlock block;

	public ToolboxBlock getBlock() {
		return block;
	}

	public void setBlock(ToolboxBlock block) {
		this.block = block;
	}
}
