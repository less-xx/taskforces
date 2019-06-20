/**
 * 
 */
package org.teapotech.taskforce.event;

import org.teapotech.block.event.BlockEvent;

/**
 * @author jiangl
 *
 */
public interface BlockEventDispatcher {

	void dispatchBlockEvent(BlockEvent event);

}
