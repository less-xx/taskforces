/**
 *
 */
package org.teapotech.resource.db;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author jiangl
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", defaultImpl = DatabaseConnectionParameter.class)
@JsonSubTypes({ @JsonSubTypes.Type(value = FQDN_IP_Parameter.class, name = FQDN_IP_Parameter.TYPE),
		@JsonSubTypes.Type(value = PORT_Parameter.class, name = PORT_Parameter.TYPE),
		@JsonSubTypes.Type(value = PASSWORD_Parameter.class, name = PASSWORD_Parameter.TYPE),
		@JsonSubTypes.Type(value = STRING_Parameter.class, name = STRING_Parameter.TYPE) })
public abstract class DatabaseConnectionParameter {

	protected String id;
	protected boolean required = false;
	protected Object defaultValue;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	abstract public String getType();

	public void validate(Object value) throws DatabaseConnectionException {
		if (required && defaultValue == null && value == null) {
			throw new DatabaseConnectionException("Missing required value for parameter " + this.id);
		}
	}

}
