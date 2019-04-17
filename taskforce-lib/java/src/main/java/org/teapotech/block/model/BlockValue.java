/**
 * 
 */
package org.teapotech.block.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author jiangl
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BlockValue {

	@XmlAttribute
	private String name;

	@XmlElement(namespace = Workspace.NAMESPACE)
	private Shadow shadow;

	@XmlElement(namespace = Workspace.NAMESPACE)
	private Block block;

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

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

}
