/**
 * 
 */
package org.teapotech.resource.exec;

import org.teapotech.resource.ResourceConfig;
import org.teapotech.resource.exception.ResourceExecutionException;

/**
 * @author lessdev
 *
 */
public abstract class ResourceExecutor<R, T extends ResourceConfig<R>> {

	protected final T resource;

	public ResourceExecutor(T resource) {
		this.resource = resource;
	}

	abstract R getResource() throws ResourceExecutionException;
}
