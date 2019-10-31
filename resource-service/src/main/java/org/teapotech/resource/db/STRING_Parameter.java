package org.teapotech.resource.db;

public class STRING_Parameter extends DatabaseConnectionParameter {

	public static final String TYPE = "STRING";

	@Override
	public String getType() {
		return TYPE;
	}
}
