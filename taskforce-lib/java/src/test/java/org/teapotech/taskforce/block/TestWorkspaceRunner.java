/**
 * 
 */
package org.teapotech.taskforce.block;

import java.io.InputStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.executor.DefaultBlockExecutionContext;
import org.teapotech.block.model.Workspace;
import org.teapotech.block.util.WorkspaceExecutor;
import org.teapotech.block.util.WorkspaceUtils;

/**
 * @author jiangl
 *
 */
public class TestWorkspaceRunner {

	private static BlockExecutorFactory factory;

	@BeforeAll
	static void init() {
		factory = BlockExecutorFactory.build();
	}

	@Test
	public void testRunWorkspace_01() throws Exception {
		try (InputStream in = getClass().getClassLoader().getResourceAsStream("workspaces/workspace_01.xml");) {
			Workspace w = WorkspaceUtils.loadWorkspace(in);
			BlockExecutionContext context = new DefaultBlockExecutionContext(factory);
			WorkspaceExecutor wExecutor = new WorkspaceExecutor(context);
			wExecutor.execute(w);
		}
	}

	@Test
	public void testRunWorkspace_02() throws Exception {
		try (InputStream in = getClass().getClassLoader().getResourceAsStream("workspaces/workspace_02.xml");) {
			Workspace w = WorkspaceUtils.loadWorkspace(in);
			BlockExecutionContext context = new DefaultBlockExecutionContext(factory);
			WorkspaceExecutor wExecutor = new WorkspaceExecutor(context);
			wExecutor.execute(w);
		}
	}

	@Test
	public void testRunWorkspace_04() throws Exception {
		try (InputStream in = getClass().getClassLoader().getResourceAsStream("workspaces/workspace_04.xml");) {
			Workspace w = WorkspaceUtils.loadWorkspace(in);
			BlockExecutionContext context = new DefaultBlockExecutionContext(factory);
			WorkspaceExecutor wExecutor = new WorkspaceExecutor(context);
			wExecutor.execute(w);

			for (String name : context.getAllVariableNames()) {
				Object value = context.getVariable(name);
				System.out.println(name + "=" + value);
			}
		}
	}

}
