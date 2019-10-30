/**
 * 
 */
package org.teapotech.resource.sql;

import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.teapotech.resource.CallableResource;
import org.teapotech.resource.ResourceParameter;
import org.teapotech.resource.exception.ResourceExecutionException;
import org.teapotech.util.ObjectValueExtractor;

/**
 * @author jiangl
 *
 */
public class SQLQueryResource extends SQLResource implements CallableResource<List<Map<String, Object>>> {

	public final static ResourceParameter<Integer> PARAM_LIMIT = new ResourceParameter<Integer>("limit", Integer.class,
			false);
	public final static ResourceParameter<Integer> PARAM_OFFSET = new ResourceParameter<Integer>("offset",
			Integer.class, false);

	public final static ResourceParameter<?>[] boundedParameters = { PARAM_SQL, PARAM_LIMIT, PARAM_OFFSET };

	@Override
	public ResourceParameter<?>[] getBoundedParameters() {
		return boundedParameters;
	}

	@Override
	public List<Map<String, Object>> call(Map<String, Object> parameterValues) throws ResourceExecutionException {
		List<String> sqlParameters = null;
		String sql = (String) parameterValues.get(PARAM_SQL.getName());
		try {
			if (sql == null) {
				sql = getSQLStatement();
				sqlParameters = getSQLNamedParameters();
			} else {
				sqlParameters = getSQLNamedParameters(sql);
			}
		} catch (Exception e) {
			throw new ResourceExecutionException(e.getMessage(), e);
		}

		sql = StringSubstitutor.replace(sql, parameterValues);

		try {
			beginTransaction();
			NativeQuery<?> query = null;
			if (!sqlParameters.isEmpty()) {
				query = createQuery(sql);
				for (int i = 0; i < sqlParameters.size(); i++) {
					String var = sqlParameters.get(i);
					Object paramValue = ObjectValueExtractor.getPropertyValue(parameterValues, var);
					if (paramValue == null) {
						throw new ResourceExecutionException("Cannot find value for parameter: " + var);
					}
					query.setParameter(var, paramValue);
				}
			} else {
				query = createQuery(sql);
			}

			query.setReadOnly(true);
			Integer offset = (Integer) parameterValues.get(PARAM_OFFSET.getName());
			if (offset != null) {
				query.setFirstResult(offset);
			}
			Integer limit = (Integer) parameterValues.get(PARAM_LIMIT.getName());
			if (limit != null && limit > 0) {
				query.setMaxResults(limit);
			}
			query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
			// query.setResultTransformer(new DynaBeanResultTransformer(this));

			List<?> result = query.list();
			commitTransaction();
			return (List<Map<String, Object>>) result;

		} catch (Exception e) {
			rollbackTransaction();
			throw new ResourceExecutionException(e.getMessage(), e);
		}
	}

	@Override
	public List<ResourceParameter<?>> getParameters() {
		// TODO Auto-generated method stub
		return null;
	}
}
