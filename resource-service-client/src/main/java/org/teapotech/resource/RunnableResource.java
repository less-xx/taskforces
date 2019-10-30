package org.teapotech.resource;

import java.util.Map;

import org.teapotech.resource.exception.ResourceExecutionException;

public interface RunnableResource extends Resource {

	void run(Map<String, Object> parameterValues) throws ResourceExecutionException;

}
