package org.teapotech.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.teapotech.resource.db.DatabaseConnectionConfig;
import org.teapotech.resource.db.SessionFactoryBuilder;
import org.teapotech.resource.db.sql.SQLQueryResource;
import org.teapotech.resource.db.sql.SQLResource;
import org.teapotech.resource.util.SQLQueryResourceDeserializer;
import org.teapotech.resource.util.SQLResourceSerializer;
import org.teapotech.util.JsonHelper;

public class TestSQLResource {

	private static JsonHelper jsonHelper = JsonHelper.newInstance().build();

	private static SQLQueryResource queryResource;
	private static SessionFactory sessionFactory = null;

	@BeforeAll
	public static void init() throws Exception {

		InputStream in = TestSQLResource.class.getClassLoader().getResourceAsStream("test_h2_conn_settings.json");
		DatabaseConnectionConfig connConfig = jsonHelper.getObject(in, DatabaseConnectionConfig.class);
		sessionFactory = SessionFactoryBuilder.getSessionFactory(connConfig);
		queryResource = new SQLQueryResource();
		queryResource.setSessionFactory(sessionFactory);
	}

	@Test
	public void test01() throws Exception {
		queryResource.setSQLStatement("select 1");
		List<Map<String, Object>> results = queryResource.getResource();
		printResult(results);
		queryResource.setSQLStatement("SELECT * FROM information_schema.tables where table_schema like :schemas");
		queryResource.updateUserParameter(new ResourceParameter<String>("schemas", String.class, "%"));
		queryResource.updateBoundParameterValue(SQLQueryResource.PARAM_LIMIT.getName(), 5);
		queryResource.updateBoundParameterValue(SQLQueryResource.PARAM_OFFSET.getName(), 2);
		results = queryResource.getResource();
		printResult(results);
	}

	@Test
	public void testSerializeSQLResource() throws Exception {
		queryResource.setSQLStatement("SELECT * FROM information_schema.tables where table_schema like :schemas");
		queryResource.updateUserParameter(new ResourceParameter<String>("schemas", String.class, "%"));
		queryResource.updateBoundParameterValue(SQLQueryResource.PARAM_LIMIT.getName(), 5);
		queryResource.updateBoundParameterValue(SQLQueryResource.PARAM_OFFSET.getName(), 2);

		JsonHelper jsonHelper = JsonHelper.newInstance().addSerializer(SQLResource.class, new SQLResourceSerializer())
				.addDeserializer(SQLQueryResource.class, new SQLQueryResourceDeserializer()).build();
		String json = jsonHelper.getJSON(queryResource);
		System.out.println(json);
		SQLQueryResource qr = jsonHelper.getObject(json, SQLQueryResource.class);
		assertNotNull(qr);
		assertEquals(queryResource.findBoundParamter(SQLResource.PARAM_SQL).get().getValue(),
				qr.findBoundParamter(SQLResource.PARAM_SQL).get().getValue());
		assertEquals(queryResource.findBoundParamter(SQLQueryResource.PARAM_LIMIT).get().getValue(),
				qr.findBoundParamter(SQLQueryResource.PARAM_LIMIT).get().getValue());
		assertEquals(queryResource.findBoundParamter(SQLQueryResource.PARAM_OFFSET).get().getValue(),
				qr.findBoundParamter(SQLQueryResource.PARAM_OFFSET).get().getValue());
		qr.setSessionFactory(sessionFactory);
		List<Map<String, Object>> results = qr.getResource();
		printResult(results);
	}

	private void printResult(List<Map<String, Object>> results) {
		int num = 0;
		for (Map<String, Object> obj : results) {
			System.out.println("Row: " + num);
			for (Entry<String, Object> entry : obj.entrySet()) {
				System.out.println("\t" + entry.getKey() + "=" + entry.getValue());
			}
			num = num + 1;
		}
	}

}
