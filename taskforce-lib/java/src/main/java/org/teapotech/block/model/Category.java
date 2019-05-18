/**
 * 
 */
package org.teapotech.block.model;

import java.util.List;

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
public class Category {

	@XmlAttribute
	private String name;

	@XmlElement(name = "category")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Category> categories;

	@XmlElement(name = "block")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Block> blocks;

	public Category() {
	}

	public Category(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}

	@Override
	public String toString() {
		return "Category: " + this.name;
	}
}