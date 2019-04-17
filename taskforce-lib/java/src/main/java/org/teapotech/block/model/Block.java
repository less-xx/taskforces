/**
 * 
 */
package org.teapotech.block.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author jiangl
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "blockly.block")
public class Block {

	@XmlAttribute
	private String type;

	@XmlAttribute
	private String id;

	@XmlAttribute
	private Integer x;

	@XmlAttribute
	private Integer y;

	@XmlElement(name = "value", namespace = Workspace.NAMESPACE)
	private List<BlockValue> values;

	@XmlElement(name = "statement", namespace = Workspace.NAMESPACE)
	private List<Statement> statements;

	@XmlElement(namespace = Workspace.NAMESPACE)
	private Next next;

	@XmlElement(name = "mutation", namespace = Workspace.NAMESPACE)
	private BlockMutation mutation;

	@XmlElement(name = "field", namespace = Workspace.NAMESPACE)
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

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Next {

		@XmlElement(name = "block", namespace = Workspace.NAMESPACE)
		private Block block;

		public Block getBlock() {
			return block;
		}

		public void setBlock(Block block) {
			this.block = block;
		}
	}

}
