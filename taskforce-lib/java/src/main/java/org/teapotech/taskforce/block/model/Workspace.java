package org.teapotech.taskforce.block.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author jiangl
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml", namespace = Workspace.NAMESPACE)
public class Workspace {

	public final static String NAMESPACE = "http://www.w3.org/1999/xhtml";

	@XmlElementWrapper(name = "variables", namespace = Workspace.NAMESPACE)
	@XmlElement(name = "variable", namespace = Workspace.NAMESPACE)
	private List<Variable> variables;

	@XmlElement(name = "block", namespace = NAMESPACE)
	private Block block;

	public List<Variable> getVariables() {
		return variables;
	}

	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

}
