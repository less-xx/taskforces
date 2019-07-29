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

/**
 * @author jiangl
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Block {

	@XmlAttribute
	private String type;

	@XmlAttribute
	private String id;

	@XmlAttribute
	private Integer x;

	@XmlAttribute
	private Integer y;

	@XmlElement(name = "value")
	private List<BlockValue> values;

	@XmlElement(name = "statement")
	private List<Statement> statements;

	@XmlElement
	private Next next;

	@XmlElement(name = "mutation")
	private BlockMutation mutation;

	@XmlElement(name = "field")
	private List<Field> fields;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public List<BlockValue> getValues() {
		return values;
	}

	public void setValues(List<BlockValue> values) {
		this.values = values;
	}

	public Next getNext() {
		return next;
	}

	public void setNext(Next next) {
		this.next = next;
	}

	public BlockMutation getMutation() {
		return mutation;
	}

	public void setMutation(BlockMutation mutation) {
		this.mutation = mutation;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public List<Statement> getStatements() {
		return statements;
	}

	public void setStatement(List<Statement> statements) {
		this.statements = statements;
	}

	public Field getFieldByName(String name, Field orElse) {
		if (this.fields == null) {
			return null;
		}
		return this.fields.stream().filter(f -> f.getName().equalsIgnoreCase(name)).findFirst().orElse(orElse);
	}

	public BlockValue getBlockValueByName(String name, BlockValue orElse) {
		if (this.values == null) {
			return null;
		}
		return this.values.stream().filter(bv -> bv.getName().equalsIgnoreCase(name)).findFirst().orElse(orElse);
	}

	public Statement getStatementByName(String name, Statement orElse) {
		if (this.statements == null) {
			return null;
		}
		return this.statements.stream().filter(stmt -> stmt.getName().equalsIgnoreCase(name)).findFirst()
				.orElse(orElse);
	}

	@Override
	public String toString() {
		return "Block, id: " + this.id + ", type: " + this.type;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Next {

		@XmlElement(name = "block")
		private Block block;

		public Block getBlock() {
			return block;
		}

		public void setBlock(Block block) {
			this.block = block;
		}
	}

}
