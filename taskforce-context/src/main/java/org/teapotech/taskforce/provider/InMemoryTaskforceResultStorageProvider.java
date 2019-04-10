/**
 * 
 */
package org.teapotech.taskforce.provider;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author jiangl
 *
 */
public class InMemoryTaskforceResultStorageProvider implements TaskforceResultStorageProvider {

	private final ConcurrentMap<String, Object> resultMap = new ConcurrentHashMap<>();

	@Override
	public Object getResult(String key) {
		return resultMap.get(key);
	}

}
