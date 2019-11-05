/**
 * 
 */
package org.teapotech.resource;

import org.teapotech.resource.exception.ResourceExecutionException;

/**
 * @author jiangl
 *
 */
public interface Resource<T> {
	T getResource() throws ResourceExecutionException;
}
