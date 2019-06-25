/**
 * 
 */
package org.teapotech.taskforce.block;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.BlockRegistryManager;
import org.teapotech.block.executor.DefaultBlockExecutionContext;
import org.teapotech.block.model.Workspace;
import org.teapotech.block.support.CustomResourcePathLoader;
import org.teapotech.block.util.BlockXmlUtils;
import org.teapotech.block.util.WorkspaceExecutor;
import org.teapotech.taskforce.entity.FileSystemPath;
import org.teapotech.taskforce.event.BlockEventDispatcher;
import org.teapotech.taskforce.event.WorkspaceEventDispatcher;
import org.teapotech.taskforce.provider.DiskFileStorageProvider;
import org.teapotech.taskforce.provider.FileStorageProvider;
import org.teapotech.taskforce.provider.InMemoryKeyValueStorageProvider;
import org.teapotech.taskforce.provider.KeyValueStorageProvider;

/**
 * @author jiangl
 *
 */
@ExtendWith(MockitoExtension.class)
public class TestWorkspaceRunner {

	private static BlockExecutorFactory factory;
	private static BlockRegistryManager registryManager;
	private static KeyValueStorageProvider kvStorageProvider = new InMemoryKeyValueStorageProvider();
	private static FileStorageProvider fileStorageProvider = new DiskFileStorageProvider("/tmp/taskforce/test");

	@Mock
	BlockEventDispatcher blockEventDispatcher;

	@Mock
	WorkspaceEventDispatcher workspaceEventDispatcher;

	@BeforeAll
	static void init() throws Exception {
		registryManager = new BlockRegistryManager();
		final Map<String, FileSystemPath> filePaths = new HashMap<>();
		filePaths.put("id0", new FileSystemPath("id0", "Test file system path 0"));
		filePaths.put("id0", new FileSystemPath("id1", "Test file system path 1"));
		filePaths.put("id0", new FileSystemPath("id2", "Test file system path 2"));

		final CustomResourcePathLoader pathLoader = new CustomResourcePathLoader() {

			@Override
			public FileSystemPath getFileSystemPathById(String id) {
				FileSystemPath filePath = filePaths.get(id);
				File tmpPath = new File("/tmp/" + id);
				if (!tmpPath.exists()) {
					tmpPath.mkdirs();
				}
				filePath.setPath(tmpPath.getAbsolutePath());
				return filePath;
			}

			@Override
			public Collection<FileSystemPath> getAllFileSystemPaths() {
				return filePaths.values();
			}
		};

		registryManager.setCustomResourcePathLoader(pathLoader);
		registryManager.loadBlockRegistries();
		factory = BlockExecutorFactory.build(registryManager);
	}

	@Test
	public void testRunWorkspace_01() throws Exception {
		try (InputStream in = getClass().getClassLoader().getResourceAsStream("workspaces/workspace_01.xml");) {
			Workspace w = BlockXmlUtils.loadWorkspace(in);
			DefaultBlockExecutionContext context = new DefaultBlockExecutionContext("test-workspace-id", factory);
			context.setKeyValueStorageProvider(kvStorageProvider);
			context.setFileStorageProvider(fileStorageProvider);
			WorkspaceExecutor wExecutor = new WorkspaceExecutor(w, context);
			wExecutor.execute();
		}
	}

	@Test
	public void testRunWorkspace_02() throws Exception {
		try (InputStream in = getClass().getClassLoader().getResourceAsStream("workspaces/workspace_02.xml");) {
			Workspace w = BlockXmlUtils.loadWorkspace(in);
			DefaultBlockExecutionContext context = new DefaultBlockExecutionContext("test-workspace-id", factory);
			context.setKeyValueStorageProvider(kvStorageProvider);
			context.setFileStorageProvider(fileStorageProvider);
			WorkspaceExecutor wExecutor = new WorkspaceExecutor(w, context);
			wExecutor.execute();
		}
	}

	@Test
	public void testRunWorkspace_04() throws Exception {
		try (InputStream in = getClass().getClassLoader().getResourceAsStream("workspaces/workspace_04.xml");) {
			Workspace w = BlockXmlUtils.loadWorkspace(in);
			DefaultBlockExecutionContext context = new DefaultBlockExecutionContext("test-workspace-id", factory);
			context.setKeyValueStorageProvider(kvStorageProvider);
			context.setFileStorageProvider(fileStorageProvider);
			WorkspaceExecutor wExecutor = new WorkspaceExecutor(w, context);
			wExecutor.execute();

			for (String name : context.getAllVariableNames()) {
				Object value = context.getVariable(name);
				System.out.println(name + "=" + value);
			}
		}
	}

	@Test
	public void testRunWorkspace_05() throws Exception {

		FileSystemPath p1 = registryManager.getCustomResourcePathLoader().getFileSystemPathById("id1");
		File pf1 = new File(p1.getPath());
		File f1 = new File(pf1, "test_file_01.txt");
		f1.createNewFile();

		try (InputStream in = getClass().getClassLoader().getResourceAsStream("workspaces/workspace_05.xml");) {
			Workspace w = BlockXmlUtils.loadWorkspace(in);
			DefaultBlockExecutionContext context = new DefaultBlockExecutionContext("test-workspace-id", factory);
			context.setKeyValueStorageProvider(kvStorageProvider);
			context.setFileStorageProvider(fileStorageProvider);
			WorkspaceExecutor wExecutor = new WorkspaceExecutor(w, context);
			wExecutor.execute();

		}
		FileSystemPath p0 = registryManager.getCustomResourcePathLoader().getFileSystemPathById("id0");
		File pf0 = new File(p0.getPath());
		File[] files = pf0.listFiles();
		assertNotNull(files);
		assertTrue(files.length > 0);
		System.out.println(files[0]);
		f1.delete();
	}

	@Test
	public void testLoopFile() throws Exception {

		FileSystemPath p1 = registryManager.getCustomResourcePathLoader().getFileSystemPathById("id0");
		File pf1 = new File(p1.getPath());
		File f1 = new File(pf1, "test_file_01.txt");
		f1.createNewFile();

		try (InputStream in = getClass().getClassLoader().getResourceAsStream("workspaces/loop_file_01.xml");) {
			Workspace w = BlockXmlUtils.loadWorkspace(in);
			DefaultBlockExecutionContext context = new DefaultBlockExecutionContext("test-workspace-id", factory);
			context.setBlockEventDispatcher(blockEventDispatcher);
			context.setWorkspaceEventDispatcher(workspaceEventDispatcher);
			context.setKeyValueStorageProvider(kvStorageProvider);
			context.setFileStorageProvider(fileStorageProvider);
			WorkspaceExecutor wExecutor = new WorkspaceExecutor(w, context);
			wExecutor.executeAndWait();

		}

		f1.delete();
	}

}
