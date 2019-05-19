package org.teapotech.block;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlockRegistry {
	String type;
	String category;
	String executorClass;
	String definition;
	Integer colour;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getExecutorClass() {
		return executorClass;
	}

	public void setExecutorClass(String executorClass) {
		this.executorClass = executorClass;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getDefinition() {
		return definition;
	}

	public Integer getColour() {
		return colour;
	}

	public void setColour(Integer colour) {
		this.colour = colour;
	}

}
