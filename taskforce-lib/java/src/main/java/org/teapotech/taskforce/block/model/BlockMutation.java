/**
 * 
 */
package org.teapotech.taskforce.block.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author jiangl
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BlockMutation {

	@XmlAttribute
	private Integer items;

	@XmlAttribute(name = "elseif")
	private Integer elseif;

	@XmlAttribute(name = "else")
	private Integer _else;

	public Integer getItems() {
		return items;
	}

	public void setItems(Integer items) {
		this.items = items;
	}

	public Integer getElseif() {
		return elseif;
	}

	public void setElseif(Integer elseif) {
		this.elseif = elseif;
	}

	public Integer getElse() {
		return _else;
	}

	public void setElse(Integer _else) {
		this._else = _else;
	}

}
