/**
 * 
 */
package org.teapotech.taskforce.event;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.beanutils.BeanUtils;
import org.teapotech.block.event.BlockEvent;

/**
 * @author jiangl
 *
 */
public class SimpleEventExchange {

	private final ConcurrentMap<String, SimpleBlockEventListener> listeners = new ConcurrentHashMap<>();
	private final ExecutorService threadPool = Executors.newFixedThreadPool(5);
	private final BlockingQueue<BlockEvent> eventQueue = new LinkedBlockingQueue<>();

	public void addBlockEventListener(SimpleBlockEventListener listener) {

		listeners.put(listener.getId(), listener);
	}

	public void removeBlockEventListener(SimpleBlockEventListener listener) {
		listeners.remove(listener.getId());
	}

	public void dispatch(final String routingKey, final BlockEvent event) {

		threadPool.submit(new Runnable() {

			@Override
			public void run() {

				BlockEvent evt = eventQueue.poll();

				for (SimpleBlockEventListener listener : listeners.values()) {
					if (matchRoutingKey(listener, routingKey)) {
						try {
							listener.setEvent((BlockEvent) BeanUtils.cloneBean(evt));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}

	private boolean matchRoutingKey(SimpleBlockEventListener listener, String routingKey) {
		String rkey = listener.getRoutingKey();
		return rkey.equalsIgnoreCase(routingKey);
	}

}
