/**
 * 
 */
package org.teapotech.resource.db.sql;

import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.hibernate.query.NativeQuery;
import org.teapotech.resource.CallableResource;
import org.teapotech.resource.ResourceParameter;
import org.teapotech.resource.exception.ResourceExecutionException;
import org.teapotech.util.ObjectValueExtractor;

/**
 * @author jiangl
 *
 */
public class SQLUpdateResource extends SQLResource implements CallableResource<Integer> {

	public final static ResourceParameter<?>[] resourceParameters = { PARAM_SQL };

	@Override
	public ResourceParameter<?>[] getResourceParameters() {
		return resourceParameters;
	}

	@Override
	public Integer call(Map<String, Object> parameterValues) throws ResourceExecutionException {
		List<ResourceParameter<?>> sqlParameters = getSQLNamedParameters();
		String sql = getSQLStatement();
		if (parameterValues != null) {
			sql = StringSubstitutor.replace(sql, parameterValues);
		}
		try {
			beginTransaction();
			NativeQuery<?> query = null;
			if (!sqlParameters.isEmpty()) {
				query = createQuery(sql);
				for (int i = 0; i < sqlParameters.size(); i++) {
					ResourceParameter<?> var = sqlParameters.get(i);
					Object paramValue = ObjectValueExtractor.getPropertyValue(parameterValues, var.getName());
					if (paramValue == null) {
						throw new ResourceExecutionException("Cannot find value for parameter: " + var);
					}
					query.setParameter(var.getName(), paramValue);
				}
			} else {
				query = createQuery(sql);
			}
			int result = query.executeUpdate();
			commitTransaction();
			return result;
		} catch (Exception e) {
			rollbackTransaction();
			throw new ResourceExecutionException(e.getMessage(), e);
		}
	}

}
