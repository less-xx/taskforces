/**
 * 
 */
package org.teapotech.taskforce.provider;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author jiangl
 *
 */
public class InMemoryTaskforceStorageProvider implements TaskforceStorageProvider {

	private final ConcurrentMap<String, Object> valueMap = new ConcurrentHashMap<>();

	@Override
	public void setTaskforceId(String taskforceId) {

	}

	@Override
	public Object get(String key) {
		return valueMap.get(key);
	}

	@Override
	public void put(String key, Object value) {
		valueMap.put(key, value);
	}

	@Override
	public Collection<String> getAllKeys() {
		return valueMap.keySet();
	}
}
