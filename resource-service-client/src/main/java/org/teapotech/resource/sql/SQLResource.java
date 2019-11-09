/**
 * 
 */
package org.teapotech.resource.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.teapotech.resource.ResourceConfig;
import org.teapotech.resource.ResourceParameter;
import org.teapotech.util.VariableParser;

/**
 * @author jiangl
 *
 */
public abstract class SQLResource<T> extends ResourceConfig<T> {

	public static final ResourceParameter<String> PARAM_SQL = new ResourceParameter<String>("sql", String.class, true);

	protected final static VariableParser varParser = new VariableParser();
	private String NAMED_PARAMETER_SIGN = ":";
	private String PUNCTUATIONS = "\n .,:;'/()";

	protected SQLResource() {
		this.boundParameters.add(PARAM_SQL);
	}

	public String getSQLStatement() {

		return (String) findBoundParamter(PARAM_SQL).get().getValue();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setSQLStatement(String sql) {
		updateBoundParameterValue(PARAM_SQL.getName(), sql);
		List<ResourceParameter<?>> sqlParams = getSQLNamedParameters(sql);
		Set<ResourceParameter<?>> newParams = sqlParams.stream().collect(Collectors.toSet());
		if (this.userParameters != null) {
			for (ResourceParameter<?> np : newParams) {
				Optional<ResourceParameter<?>> oldParam = this.userParameters.stream()
						.filter(p -> p.getName().equalsIgnoreCase(np.getName())).findFirst();
				if (oldParam.isPresent()) {
					ResourceParameter<?> rp = oldParam.get();
					updateUserParameter(
							new ResourceParameter(rp.getName(), np.getType(), np.isRequired(), rp.getValue()));
				}
			}
		}
		setUserParameters(newParams);
	}

	private List<String> parseSQLNamedParameters(String sql) {
		char[] chars = sql.toCharArray();
		List<String> params = new ArrayList<>();
		StringBuilder paramBuilder = null;
		boolean foundParamStart = false;
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (!foundParamStart) {
				foundParamStart = NAMED_PARAMETER_SIGN.equals(String.valueOf(c));
				if (foundParamStart) {
					paramBuilder = new StringBuilder();
					continue;
				}
			}

			if (foundParamStart) {
				if (PUNCTUATIONS.indexOf(c) >= 0) {
					params.add(paramBuilder.toString());
					foundParamStart = false;
					paramBuilder = null;
				} else {
					paramBuilder.append(c);
				}
			}
		}
		if (paramBuilder != null && paramBuilder.length() > 0) {
			params.add(paramBuilder.toString());

		}
		return params;
	}

	public List<ResourceParameter<?>> getSQLNamedParameters(String sql) {

		return parseSQLNamedParameters(sql).stream().map(pname -> new ResourceParameter<>(pname, String.class))
				.collect(Collectors.toList());
	}

}