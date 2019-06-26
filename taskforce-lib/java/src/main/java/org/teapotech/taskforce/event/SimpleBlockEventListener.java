package org.teapotech.taskforce.event;

import org.teapotech.block.event.BlockEvent;

public class SimpleBlockEventListener implements BlockEventListener {

	private BlockEvent event;
	private String id;
	private String routingKey;
	private Object lock = new Object();

	public void setEvent(BlockEvent event) {
		synchronized (lock) {
			this.event = event;
			this.notifyAll();
		}

	}

	@Override
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

	@Override
	public String getRoutingKey() {
		return this.routingKey;
	}

	@Override
	public BlockEvent receive() {
		BlockEvent _evt = null;
		synchronized (lock) {
			while (this.event == null) {
				try {
					this.wait(5000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			_evt = this.event;
			this.event = null;
		}
		return _evt;
	}

	@Override
	public void initialize() {

	}

	@Override
	public void destroy() {

	}

}
