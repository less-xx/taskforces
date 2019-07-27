package org.teapotech.block.def;

public class Category {

	public final static String EVENTS = "Events";
	public final static String START_STOP = "Start | Stop";
	public final static String RESOURCES = "Resources";
	public final static String VARIABLES = "Variables";

	public static String getColor(final String category) {
		switch (category) {
		case EVENTS:
			return "#EDDD22";
		case START_STOP:
			return "#04E004";
		case RESOURCES:
			return "#0280EE";
		}
		return "#55AA80";
	}
}
