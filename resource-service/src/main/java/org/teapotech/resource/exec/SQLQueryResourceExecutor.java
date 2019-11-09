/**
 * 
 */
package org.teapotech.resource.exec;

import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.teapotech.resource.ResourceParameter;
import org.teapotech.resource.exception.ResourceExecutionException;
import org.teapotech.resource.sql.SQLQueryResource;
import org.teapotech.util.ObjectValueExtractor;

/**
 * @author lessdev
 *
 */
public class SQLQueryResourceExecutor extends SQLResourceExecutor<List<Map<String, Object>>, SQLQueryResource> {

	public SQLQueryResourceExecutor(SQLQueryResource resource) {
		super(resource);
	}

	@Override
	public List<Map<String, Object>> getResource() throws ResourceExecutionException {
		String sql = resource.getSQLStatement();
		List<ResourceParameter<?>> sqlParameters = resource.getSQLNamedParameters(sql);

		Integer limit = SQLQueryResource.PARAM_LIMIT.getValue();
		Integer offset = SQLQueryResource.PARAM_OFFSET.getValue();
		Map<String, Object> userParamValues = resource.getUserParameterValueMap();
		if (userParamValues != null) {
			sql = StringSubstitutor.replace(sql, userParamValues);
		}
		ResourceParameter<?> rp = resource.findBoundParamter(SQLQueryResource.PARAM_LIMIT).get();
		if (rp.getValue() != null) {
			limit = (Integer) rp.getValue();
		}
		rp = resource.findBoundParamter(SQLQueryResource.PARAM_OFFSET).get();
		if (rp.getValue() != null) {
			offset = (Integer) rp.getValue();
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
