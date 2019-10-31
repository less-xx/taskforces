/**
 * 
 */
package org.teapotech.resource.db.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.teapotech.resource.ParameterizedResource;
import org.teapotech.resource.ResourceParameter;
import org.teapotech.util.VariableParser;

/**
 * @author jiangl
 *
 */
public abstract class SQLResource extends ParameterizedResource {

	public final static ResourceParameter<String> PARAM_SQL = new ResourceParameter<String>("sql", String.class, true);

	protected final static VariableParser varParser = new VariableParser();
	private SessionFactory sessionFactory;
	private String NAMED_PARAMETER_SIGN = ":";
	private String PUNCTUATIONS = "\n .,:;'/()";
	protected List<ResourceParameter<?>> sqlNamedParameters;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Transaction beginTransaction() {
		return this.sessionFactory.getCurrentSession().beginTransaction();
	}

	protected void commitTransaction() {
		this.sessionFactory.getCurrentSession().getTransaction().commit();
	}

	protected void rollbackTransaction() {
		this.sessionFactory.getCurrentSession().getTransaction().rollback();
	}

	protected NativeQuery<?> createQuery(String sql) {
		Session session = sessionFactory.getCurrentSession();
		return session.createNativeQuery(sql);
	}

	public String getSQLStatement() {
		return (String) this.getParameterValues().get(PARAM_SQL.getName());
	}

	public void setSQLStatement(String sql) {
		setResourceParameterValue(PARAM_SQL, sql);
	}

	public List<String> getSQLNamedParameters(String sql) {
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<ResourceParameter<?>> getSQLNamedParameters() {
		if (this.sqlNamedParameters != null) {
			return this.sqlNamedParameters;
		}

		String sql = getSQLStatement();
		return getSQLNamedParameters(sql).stream().map(pname -> new ResourceParameter(pname, String.class))
				.collect(Collectors.toList());
	}

}