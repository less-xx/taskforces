/**
 * 
 */
package org.teapotech.taskforce.block;

import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.teapotech.taskforce.block.executor.BlockExecutionContext;
import org.teapotech.taskforce.block.executor.DefaultBlockExecutionContext;
import org.teapotech.taskforce.block.model.Workspace;
import org.teapotech.taskforce.block.util.WorkspaceExecutor;
import org.teapotech.taskforce.block.util.WorkspaceUtils;

/**
 * @author jiangl
 *
 */
public class TestWorkspaceRunner {

	@Test
	public void testRunWorkspace_01() throws Exception {
		try (InputStream in = getClass().getClassLoader().getResourceAsStream("workspaces/workspace_01.xml");) {
			Workspace w = WorkspaceUtils.loadWorkspace(in);
			BlockExecutionContext context = new DefaultBlockExecutionContext();
			BlockExecutorFactory factory = BlockExecutorFactory.build();
			WorkspaceExecutor wExecutor = new WorkspaceExecutor(context, factory);
			wExecutor.execute(w);
		}
	}

	@Test
	public void testRunWorkspace_02() throws Exception {
		try (InputStream in = getClass().getClassLoader().getResourceAsStream("workspaces/workspace_02.xml");) {
			Workspace w = WorkspaceUtils.loadWorkspace(in);
			BlockExecutionContext context = new DefaultBlockExecutionContext();
			BlockExecutorFactory factory = BlockExecutorFactory.build();
			WorkspaceExecutor wExecutor = new WorkspaceExecutor(context, factory);
			wExecutor.execute(w);
		}
	}

}
