/**
 * 
 */
package org.teapotech.resource;

import org.teapotech.resource.exception.InvalidParameterValueException;

/**
 * @author jiangl
 *
 */
public class ResourceParameter<T> {

	private String name;
	private boolean required = true;
	private Class<T> type;

	public ResourceParameter() {
	}

	public ResourceParameter(String name, Class<T> type) {
		this(name, type, true);
	}

	public ResourceParameter(String name, Class<T> type, boolean required) {
		this.name = name;
		this.type = type;
		this.required = required;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<T> getType() {
		return type;
	}

	public void setType(Class<T> type) {
		this.type = type;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof ResourceParameter)) {
			return false;
		}
		ResourceParameter dsp = (ResourceParameter) obj;
		if (dsp.name != null && this.name != null) {
			return dsp.name.equals(this.name);
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (this.name != null) {
			return (this.getClass().getName() + "$" + this.name).hashCode();
		}
		return super.hashCode();
	}

	@Override
	public String toString() {
		return this.name + " [" + this.type + "]";
	}

	public Object wrapValue(Object value) throws InvalidParameterValueException {
		throw new InvalidParameterValueException(
				"Parameter value should be explicitly converted to type " + type.getName());
	}

}
