package org.teapotech.block.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

/**
 * 
 * @author jiangl
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "xml")
public class Workspace {

	// public final static String NAMESPACE = "http://www.w3.org/1999/xhtml";

	@XmlElementWrapper(name = "variables")
	@XmlElement(name = "variable")
	private List<Variable> variables;

	@XmlElement(name = "category")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Category> categories = new ArrayList<>();

	@XmlElement(name = "block")
	private List<Block> blocks = null;

	@XmlAttribute
	private String style = "display: none";

	public List<Variable> getVariables() {
		return variables;
	}

	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}

	public List<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public Category findCategoryById(String idChain) {
		String[] cc = idChain.split("\\s*/\\s*");
		Category cat = null;
		List<Category> cl = this.categories;
		for (String cid : cc) {
			Optional<Category> op = cl.stream().filter(c -> c.getId().equalsIgnoreCase(cid)).findFirst();
			if (!op.isPresent()) {
				return null;
			}
			cat = op.get();
			cl = cat.getCategories();
		}
		return cat;
	}
}
