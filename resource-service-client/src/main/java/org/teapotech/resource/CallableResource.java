package org.teapotech.resource;

import java.util.Map;

import org.teapotech.resource.exception.ResourceExecutionException;

public interface CallableResource<R> extends Resource {

	R call(Map<String, Object> parameterValues) throws ResourceExecutionException;
}
