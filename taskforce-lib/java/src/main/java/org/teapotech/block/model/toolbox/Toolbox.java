package org.teapotech.block.model.toolbox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml", namespace = "")
public class Toolbox {

	@XmlAttribute
	private String id = "toolbox";

	@XmlAttribute
	private String style = "display: none";

	@XmlElement(name = "category", namespace = "")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Category> categories = new ArrayList<>();

	public Toolbox() {
	}

	public Toolbox(String id) {
		this.id = id;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
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

	public Category getCategoryByName(String name) {
		String[] cc = name.split("\\s*/\\s*");
		List<Category> cl = this.categories;
		Category cat = null;
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
			if (cl == null) {
				cl = new ArrayList<>();
				cat.setCategories(cl);
			}
		}
		return cat;
	}

}
