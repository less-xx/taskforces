package org.teapotech.resource;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.teapotech.resource.db.DatabaseConnectionConfig;
import org.teapotech.resource.db.SessionFactoryBuilder;
import org.teapotech.resource.db.sql.SQLQueryResource;
import org.teapotech.util.JsonHelper;

public class TestSQLResource {

	private static JsonHelper jsonHelper = JsonHelper.newInstance().build();

	private static SQLQueryResource queryResource;

	@BeforeAll
	public static void init() throws Exception {

		InputStream in = TestSQLResource.class.getClassLoader().getResourceAsStream("test_h2_conn_settings.json");
		DatabaseConnectionConfig connConfig = jsonHelper.getObject(in, DatabaseConnectionConfig.class);
		SessionFactory sessionFactory = SessionFactoryBuilder.getSessionFactory(connConfig);
		queryResource = new SQLQueryResource();
		queryResource.setSessionFactory(sessionFactory);
		queryResource.setName("Test query resource");
	}

	@Test
	public void test01() throws Exception {
		queryResource.setSQLStatement("select 1");
		List<Map<String, Object>> results = queryResource.call(null);
		printResult(results);
		queryResource.setSQLStatement("SELECT * FROM information_schema.tables where table_schema like :schemas");
		Map<String, Object> params = new HashMap<>();
		params.put("schemas", "%");
		params.put(SQLQueryResource.PARAM_OFFSET.getName(), 2);
		params.put(SQLQueryResource.PARAM_LIMIT.getName(), 10);
		results = queryResource.call(params);
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
