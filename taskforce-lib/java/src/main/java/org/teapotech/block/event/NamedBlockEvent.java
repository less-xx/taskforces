/**
 * 
 */
package org.teapotech.block.event;

/**
 * @author jiangl
 *
 */
public class NamedBlockEvent extends BlockEvent {

	private String eventName;

	public NamedBlockEvent() {
		super();
	}

	public NamedBlockEvent(String workspaceId, String type, String id) {
		super(workspaceId, type, id);
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
}
