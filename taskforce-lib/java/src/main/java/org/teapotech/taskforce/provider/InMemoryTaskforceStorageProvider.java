/**
 * 
 */
package org.teapotech.taskforce.provider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author jiangl
 *
 */
public class InMemoryTaskforceStorageProvider implements TaskforceStorageProvider {

	private final ConcurrentMap<String, Map<String, Object>> valueMap = new ConcurrentHashMap<>();

	@Override
	public Object get(String taskforceId, String key) {
		if (!valueMap.containsKey(taskforceId)) {
			return null;
		}
		return valueMap.get(taskforceId).get(key);
	}

	@Override
	public void put(String taskforceId, String key, Object value) {
		if (!valueMap.containsKey(taskforceId)) {
			valueMap.put(taskforceId, new HashMap<>());
		}
		valueMap.get(taskforceId).put(key, value);
	}

	@Override
	public void remove(String taskforceId, String key) {
		if (!valueMap.containsKey(taskforceId)) {
			return;
		}
		valueMap.get(taskforceId).remove(key);
	}

	@Override
	public Collection<String> getAllKeys(String taskforceId) {
		if (!valueMap.containsKey(taskforceId)) {
			return null;
		}
		return valueMap.get(taskforceId).keySet();
	}

	@Override
	public void destroy(String taskforceId) {
		if (!valueMap.containsKey(taskforceId)) {
			return;
		}
		valueMap.remove(taskforceId);
	}
}
