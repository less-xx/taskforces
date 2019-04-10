/**
 * 
 */
package org.teapotech.taskforce.context;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author lessdev
 *
 */
public class DefaultTaskForceContext implements TaskforceContext {

	@Override
	public String getTaskforceId() {
		return RandomStringUtils.randomAlphanumeric(13);
	}
}
