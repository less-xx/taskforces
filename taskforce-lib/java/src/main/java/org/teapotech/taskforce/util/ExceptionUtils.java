/**
 * 
 */
package org.teapotech.taskforce.util;

import java.util.List;

/**
 * @author jiangl
 *
 */
public class ExceptionUtils {

	public static boolean isExceptionOfType(List<Class<? extends Throwable>> exTypes, Throwable exception) {
		Throwable t = exception;
		while (t != null) {
			if (exception.getClass().equals(t.getClass())) {
				return true;
			}
			t = t.getCause();
		}
		return false;
	}

	public static Throwable findExceptionOfType(List<Class<? extends Throwable>> exTypes, Throwable exception) {
		Throwable t = exception;
		while (t != null) {
			if (exception.getClass().equals(t.getClass())) {
				return t;
			}
			t = t.getCause();
		}
		return null;
	}

}
