/**
 * 
 */
package org.teapotech.resource.db.sql;

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

	public final static ResourceParameter<?>[] resourceParameters = { PARAM_SQL, PARAM_LIMIT, PARAM_OFFSET };

	@Override
	public ResourceParameter<?>[] getResourceParameters() {
		return resourceParameters;
	}

	@Override
	public List<Map<String, Object>> call(Map<String, Object> parameterValues) throws ResourceExecutionException {
		List<ResourceParameter<?>> sqlParameters = getSQLNamedParameters();
		String sql = getSQLStatement();
		Integer offset = 0;
		Integer limit = 100;
		if (parameterValues != null) {
			sql = StringSubstitutor.replace(sql, parameterValues);
			if (parameterValues.containsKey(PARAM_OFFSET.getName())) {
				offset = (Integer) parameterValues.get(PARAM_OFFSET.getName());
			}
			if (parameterValues.containsKey(PARAM_LIMIT.getName())) {
				limit = (Integer) parameterValues.get(PARAM_LIMIT.getName());
			}
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

			query.setReadOnly(true);
			query.setFirstResult(offset);
			query.setMaxResults(limit);
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

}
