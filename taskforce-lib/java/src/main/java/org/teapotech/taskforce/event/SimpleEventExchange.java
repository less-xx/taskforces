/**
 * 
 */
package org.teapotech.taskforce.event;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teapotech.block.event.NamedBlockEvent;

/**
 * @author jiangl
 *
 */
public class SimpleEventExchange {
	private static Logger LOG = LoggerFactory.getLogger(SimpleEventExchange.class);

	private final ConcurrentMap<String, SimpleBlockEventListener> listeners = new ConcurrentHashMap<>();

	public void addBlockEventListener(SimpleBlockEventListener listener) {
		listeners.put(listener.getId(), listener);
		LOG.info("Registered block event listener: {}", listener);
	}

	public void removeBlockEventListener(SimpleBlockEventListener listener) {
		listeners.remove(listener.getId());
		LOG.info("Removed block event listener: {}", listener);
	}

	public void dispatch(final String routingKey, final NamedBlockEvent event) {

		for (SimpleBlockEventListener listener : listeners.values()) {
			if (matchRoutingKey(listener, routingKey)) {
				try {
					listener.addEvent(event);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean matchRoutingKey(SimpleBlockEventListener listener, String routingKey) {
		String rkey = listener.getRoutingKey();
		return rkey.equalsIgnoreCase(routingKey);
	}

}
