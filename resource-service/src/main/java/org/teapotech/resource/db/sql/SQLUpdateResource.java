/**
 * 
 */
package org.teapotech.resource.db.sql;

import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.hibernate.query.NativeQuery;
import org.teapotech.resource.ResourceParameter;
import org.teapotech.resource.exception.ResourceExecutionException;
import org.teapotech.util.ObjectValueExtractor;

/**
 * @author jiangl
 *
 */
public class SQLUpdateResource extends SQLResource<Integer> {

	@Override
	public Integer getResource() throws ResourceExecutionException {
		String sql = getSQLStatement();
		List<ResourceParameter<?>> sqlParameters = getSQLNamedParameters(sql);
		Map<String, Object> userParamValues = getUserParameterValueMap();
		if (userParamValues != null) {
			sql = StringSubstitutor.replace(sql, userParamValues);
		}
		try {
			beginTransaction();
			NativeQuery<?> query = null;
			if (!sqlParameters.isEmpty()) {
				query = createQuery(sql);
				for (int i = 0; i < sqlParameters.size(); i++) {
					ResourceParameter<?> var = sqlParameters.get(i);
					Object paramValue = ObjectValueExtractor.getPropertyValue(userParamValues, var.getName());
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
