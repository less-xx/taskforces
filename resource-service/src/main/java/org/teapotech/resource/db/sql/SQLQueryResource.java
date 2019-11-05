/**
 * 
 */
package org.teapotech.resource.db.sql;

import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.teapotech.resource.ResourceParameter;
import org.teapotech.resource.exception.ResourceExecutionException;
import org.teapotech.util.ObjectValueExtractor;

/**
 * @author jiangl
 *
 */
public class SQLQueryResource extends SQLResource<List<Map<String, Object>>> {

	public static final ResourceParameter<Integer> PARAM_LIMIT = new ResourceParameter<Integer>("limit", Integer.class,
			false, 100);
	public static final ResourceParameter<Integer> PARAM_OFFSET = new ResourceParameter<Integer>("offset",
			Integer.class, false, 0);

	public SQLQueryResource() {
		super();
		boundParameters.add(PARAM_LIMIT);
		boundParameters.add(PARAM_OFFSET);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getResource() throws ResourceExecutionException {
		String sql = getSQLStatement();
		List<ResourceParameter<?>> sqlParameters = getSQLNamedParameters(sql);

		Integer limit = PARAM_LIMIT.getValue();
		Integer offset = PARAM_OFFSET.getValue();
		Map<String, Object> userParamValues = getUserParameterValueMap();
		if (userParamValues != null) {
			sql = StringSubstitutor.replace(sql, userParamValues);
		}
		ResourceParameter<?> rp = findBoundParamter(PARAM_LIMIT).get();
		if (rp.getValue() != null) {
			limit = (Integer) rp.getValue();
		}
		rp = findBoundParamter(PARAM_OFFSET).get();
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
