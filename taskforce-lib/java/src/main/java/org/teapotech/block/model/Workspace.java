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
	private List<Category> categories = null;

	@XmlElement(name = "block")
	private Block block;

	@XmlAttribute
	private String id = "toolbox";

	@XmlAttribute
	private String style = "display: none";

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

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public Category getCategoryByName(String name, boolean createIfNotExist) {
		String[] cc = name.split("\\s*/\\s*");

		Category cat = null;
		if (this.categories == null) {
			if (createIfNotExist) {
				this.categories = new ArrayList<>();
			} else {
				return cat;
			}
		}
		List<Category> cl = this.categories;
		for (String cname : cc) {
			Optional<Category> op = cl.stream().filter(c -> c.getName().equalsIgnoreCase(cname)).findFirst();
			if (op.isPresent()) {
				cat = op.get();
				cl = cat.getCategories();
			} else {
				cat = new Category(cname);
				cl.add(cat);
				cl = cat.getCategories();
			}
			if (cl == null && createIfNotExist) {
				cl = new ArrayList<>();
				cat.setCategories(cl);
			}
		}
		return cat;
	}

}
