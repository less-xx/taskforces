/**
 * 
 */
package org.teapotech.resource;

/**
 * @author jiangl
 *
 */
public class ResourceParameter<T> {

	private final String name;
	private final boolean required;
	private final Class<T> type;
	private final T value;

	public ResourceParameter(String name, Class<T> type) {
		this(name, type, true);
	}

	public ResourceParameter(String name, Class<T> type, boolean required) {
		this(name, type, required, null);
	}

	public ResourceParameter(String name, Class<T> type, boolean required, T value) {
		this.name = name;
		this.type = type;
		this.required = required;
		this.value = value;
	}

	public ResourceParameter(String name, Class<T> type, T value) {
		this(name, type, true, value);
	}

	public String getName() {
		return name;
	}

	public Class<T> getType() {
		return type;
	}

	public boolean isRequired() {
		return required;
	}

	public T getValue() {
		return value;
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

}
