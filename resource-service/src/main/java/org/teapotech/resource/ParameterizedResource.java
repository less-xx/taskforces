package org.teapotech.resource;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teapotech.resource.exception.InvalidParameterException;
import org.teapotech.resource.exception.MissingParameterValueException;
import org.teapotech.util.VariableParser;

abstract public class ParameterizedResource<T> implements Resource<T> {

	private static Comparator<ResourceParameter<?>> comparator = new Comparator<ResourceParameter<?>>() {

		@Override
		public int compare(ResourceParameter<?> p1, ResourceParameter<?> p2) {
			return p1.getName().compareTo(p2.getName());
		}
	};

	protected Logger LOG = LoggerFactory.getLogger(getClass());
	protected static VariableParser varParser = new VariableParser(false);
	protected Set<ResourceParameter<?>> userParameters = new TreeSet<>(comparator);
	protected final Set<ResourceParameter<?>> boundParameters = new TreeSet<>(comparator);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void updateBoundParameterValue(String paramName, Object value) {
		Optional<ResourceParameter<?>> op = this.boundParameters.stream()
				.filter(p -> p.getName().equalsIgnoreCase(paramName)).findFirst();
		if (op.isPresent()) {
			ResourceParameter<?> oldP = op.get();
			this.boundParameters.remove(oldP);
			this.boundParameters.add(new ResourceParameter(oldP.getName(), oldP.getType(), oldP.isRequired(), value));
		} else {
			throw new InvalidParameterException("Invalid bound parameter [" + paramName + "]");
		}
	}

	public Set<ResourceParameter<?>> getBoundParameters() {
		return boundParameters;
	}

	public Set<ResourceParameter<?>> getUserParameters() {
		return userParameters;
	}

	public void updateUserParameter(ResourceParameter<?> userParameter) {
		this.userParameters.remove(userParameter);
		this.userParameters.add(userParameter);
	}

	protected void setUserParameters(Set<ResourceParameter<?>> userParameters) {
		this.userParameters = userParameters;
	}

	protected Optional<ResourceParameter<?>> findBoundParamter(ResourceParameter<?> param) {
		return this.boundParameters.stream().filter(p -> p.getName().equalsIgnoreCase(param.getName())).findAny();
	}

	protected Optional<ResourceParameter<?>> findUserParameterByName(String paramName) {
		return this.userParameters.stream().filter(p -> p.getName().equalsIgnoreCase(paramName)).findFirst();
	}

	protected void validateParameters() throws MissingParameterValueException, InvalidParameterException {
		for (ResourceParameter<?> param : this.boundParameters) {
			validateParamter(param);
		}
		for (ResourceParameter<?> param : this.userParameters) {
			validateParamter(param);
		}
	}

	private void validateParamter(ResourceParameter<?> param)
			throws MissingParameterValueException, InvalidParameterException {
		if (param.isRequired() && param.getValue() == null) {
			throw new MissingParameterValueException("Value of paramter " + param.getName() + " is required.");
		}
	}

	protected Map<String, Object> getUserParameterValueMap() {
		if (userParameters != null) {
			return this.userParameters.stream()
					.collect(Collectors.toMap(ResourceParameter::getName, ResourceParameter::getValue));
		}
		return null;

	}
}
