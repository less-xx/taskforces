/**
 * 
 */
package org.teapotech.taskforce;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.executor.docker.DockerBlockExecutionContext;
import org.teapotech.block.model.Workspace;
import org.teapotech.block.util.WorkspaceExecutor;
import org.teapotech.block.util.WorkspaceUtils;
import org.teapotech.taskforce.provider.FileStorageProvider;
import org.teapotech.taskforce.provider.KeyValueStorageProvider;
import org.teapotech.taskforce.task.TaskExecutionUtil;

/**
 * @author jiangl
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableAutoConfiguration
@ComponentScan("org.teapotech.taskforce")
@SpringJUnitConfig(SpringBootContextLoader.class)
public class TestTaskforceExecutor {

	@Autowired
	KeyValueStorageProvider kvStorageProvider;

	@Autowired
	FileStorageProvider fileStorageProvider;

	@Autowired
	BlockExecutorFactory factory;

	@BeforeAll
	static void init() {
		System.setProperty(TaskExecutionUtil.ENV_TASKFORICE_ID, "test-taskforce-id");
	}

	@Test
	public void testRunResourceFetcher() throws Exception {
		String taskforceId = "test-taskforce-id";
		DockerBlockExecutionContext context = new DockerBlockExecutionContext(taskforceId, factory, kvStorageProvider,
				fileStorageProvider);

		try (InputStream in = getClass().getClassLoader().getResourceAsStream("workspaces/resource_fetcher_01.xml");) {
			Workspace w = WorkspaceUtils.loadWorkspace(in);
			WorkspaceExecutor wExecutor = new WorkspaceExecutor(context);
			wExecutor.execute(w);

			Object result = context.getVariable("result");
			assertNotNull(result);
			System.out.println(result);

			wExecutor.destroy();
		}
	}
}