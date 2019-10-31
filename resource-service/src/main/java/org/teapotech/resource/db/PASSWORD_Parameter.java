package org.teapotech.resource.db;

public class PASSWORD_Parameter extends DatabaseConnectionParameter {

	public static final String TYPE = "PASSWORD";

	public PASSWORD_Parameter() {
		super();
	}

	public String convertValue(Object value) {
		return value.toString();
	}

	@Override
	public String getType() {
		return TYPE;
	}

}
