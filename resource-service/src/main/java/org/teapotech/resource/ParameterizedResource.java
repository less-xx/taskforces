package org.teapotech.resource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teapotech.resource.exception.InvalidParameterValueException;
import org.teapotech.resource.exception.MissingParameterValueException;
import org.teapotech.util.InvalidVariableException;
import org.teapotech.util.ObjectValueExtractor;
import org.teapotech.util.VariableParser;

import com.fasterxml.jackson.databind.JsonNode;

abstract public class ParameterizedResource implements Resource {

	protected static VariableParser varParser = new VariableParser(false);
	protected Logger LOG = LoggerFactory.getLogger(getClass());
	private final Map<String, Object> parameterValues = new TreeMap<>();
	private String name;
	private String id;
	private String type;

	public Map<String, Object> getBoundedParametersValues() {
		Map<String, Object> parameters = new HashMap<>();
		for (ResourceParameter<?> param : getResourceParameters()) {
			Object value = getBoundedParameterValue(param);
			parameters.put(param.getName(), value);
		}
		return parameters;
	}

	public Map<String, Object> getParameterValues() {
		return parameterValues;
	}

	public void setParameterValues(Map<String, Object> parameterValues) {
		this.parameterValues.putAll(parameterValues);
	}

	@SuppressWarnings({ "unchecked" })
	public <T> T getBoundedParameterValue(ResourceParameter<T> param) {
		return (T) this.parameterValues.get(param.getName());
	}

	public <T> void setResourceParameterValue(ResourceParameter<T> param, T value) {
		this.parameterValues.put(param.getName(), value);
	}

	public void setParameterValue(String parameterName, Object value) {
		ResourceParameter<?> param = findBoundedParameter(parameterName);
		if (param == null) {
			this.parameterValues.put(parameterName, value);
			return;
		}
		if (value == null) {
			if (param.isRequired()) {
				throw new InvalidParameterValueException(
						"Parameter value should not be null. Parameter name: " + parameterName);
			} else {
				return;
			}
		}
		if (ClassUtils.isAssignable(value.getClass(), param.getType())) {
			this.parameterValues.put(parameterName, value);
		} else {
			this.parameterValues.put(parameterName, param.wrapValue(value));
		}
		LOG.debug("set bound parameter {}={}", parameterName, value);
	}

	static void replaceParameterValueVariables(Map<String, Object> paramValueMap)
			throws InvalidParameterValueException, InvalidVariableException {

		Iterator<String> it = paramValueMap.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			Object pv = paramValueMap.get(key);
			if (!(pv instanceof String)) {
				continue;
			}
			if (varParser.isJustVariable((String) pv)) {
				String varName = varParser.findVariables((String) pv).get(0);
				Object varValue = paramValueMap.get(varName);
				if (varValue == null) {
					throw new InvalidParameterValueException("Value should not be null for variable " + varName);
				}
				paramValueMap.put(key, varValue);
			} else {
				String replacedStr = StringSubstitutor.replace((String) pv, paramValueMap);
				List<String> vars = varParser.findVariables(replacedStr);
				if (vars.isEmpty()) {
					paramValueMap.put(key, replacedStr);
				} else {
					throw new InvalidParameterValueException("Value should not be null for variable " + vars.get(0));
				}
			}
		}
	}

	protected void validateParameterValues() throws MissingParameterValueException, InvalidParameterValueException {
		validateParameterValues(this.parameterValues);
	}

	protected void validateParameterValues(Map<String, Object> paramValues)
			throws MissingParameterValueException, InvalidParameterValueException {
		for (ResourceParameter<?> bp : getResourceParameters()) {
			if (bp.isRequired()) {
				Object pv = null;
				if (paramValues != null) {
					pv = paramValues.get(bp.getName());
				}
				if (pv == null) {
					throw new MissingParameterValueException("Cannot find value for parameter " + bp.getName());
				}
			}
		}
	}

	public Set<String> findUserParameters() {
		if (this.parameterValues == null) {
			return null;
		}
		Set<String> params = new TreeSet<>();
		for (Object value : this.parameterValues.values()) {
			if (value instanceof String) {

				try {
					List<String> vars = varParser.findVariables((String) value);
					params.addAll(vars);
				} catch (InvalidVariableException e) {
					LOG.error(e.getMessage());
				}
			}
		}

		return params;
	}

	public boolean hasBoundedParameter(ResourceParameter<?> param) {
		ResourceParameter<?> p = findBoundedParameter(param.getName());
		if (p == null) {
			return false;
		}
		return p.getType().equals(param.getType());
	}

	public ResourceParameter<?> findBoundedParameter(String parameterName) {
		ResourceParameter<?>[] boundParams = getResourceParameters();
		if (boundParams == null) {
			return null;
		}
		for (ResourceParameter<?> p : boundParams) {
			if (p.getName().equals(parameterName)) {
				return p;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void configureParameters(JsonNode parametersNode) {
		try {
			if (parametersNode != null) {
				Iterator<String> pnames = parametersNode.fieldNames();
				while (pnames.hasNext()) {
					String pname = pnames.next();
					Object value = ObjectValueExtractor.getPropertyValue(parametersNode, pname);
					setParameterValue(pname, value);
				}
			}
		} catch (Exception e) {
			throw new InvalidParameterValueException(e.getMessage(), e);
		}
	}
}
