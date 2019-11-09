/**
 * 
 */
package org.teapotech.resource.exec;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.teapotech.resource.sql.SQLResource;

/**
 * @author lessdev
 *
 */
public abstract class SQLResourceExecutor<R, T extends SQLResource<R>> extends ResourceExecutor<R, T> {

	protected SessionFactory sessionFactory;

	public SQLResourceExecutor(T resource) {
		super(resource);
	}

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

}
