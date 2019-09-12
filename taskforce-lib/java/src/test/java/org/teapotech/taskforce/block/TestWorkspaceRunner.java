/**
 * 
 */
package org.teapotech.taskforce.block;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.block.BlockRegistryManager;
import org.teapotech.block.executor.DefaultBlockExecutionContext;
import org.teapotech.block.model.Workspace;
import org.teapotech.block.support.CustomResourcePathLoader;
import org.teapotech.block.util.BlockXmlUtils;
import org.teapotech.block.util.WorkspaceExecutor;
import org.teapotech.taskforce.entity.FileSystemPath;
import org.teapotech.taskforce.event.BlockEventListenerFactory;
import org.teapotech.taskforce.event.EventDispatcher;
import org.teapotech.taskforce.event.SimpleBlockEventListenerFactory;
import org.teapotech.taskforce.event.SimpleEventDispatcher;
import org.teapotech.taskforce.event.SimpleEventExchange;
import org.teapotech.taskforce.provider.DiskFileStorageProvider;
import org.teapotech.taskforce.provider.FileStorageProvider;
import org.teapotech.taskforce.provider.InMemoryKeyValueStorageProvider;
import org.teapotech.taskforce.provider.KeyValueStorageProvider;
import org.teapotech.taskforce.task.config.TaskforceExecutionProperties;

/**
 * @author jiangl
 *
 */
public class TestWorkspaceRunner {

	private static BlockExecutorFactory factory;
	private static BlockRegistryManager registryManager;
	private static KeyValueStorageProvider kvStorageProvider = new InMemoryKeyValueStorageProvider();
	private static FileStorageProvider fileStorageProvider = new DiskFileStorageProvider("/tmp/taskforce/test");
	private static SimpleEventExchange eventExchange = new SimpleEventExchange();
	private static EventDispatcher eventDispatcher = new SimpleEventDispatcher(eventExchange);
	private static BlockEventListenerFactory blockEventListenerFac = new SimpleBlockEventListenerFactory(eventExchange);

	private static String testWorkspaceId = "test-workspace-id_0";

	@BeforeAll
	static void init() throws Exception {
		registryManager = new BlockRegistryManager();
		final Map<String, FileSystemPath> filePaths = new HashMap<>();
		filePaths.put("id0", new FileSystemPath("id0", "Test file system path 0"));
		filePaths.put("id1", new FileSystemPath("id1", "Test file system path 1"));
		filePaths.put("id2", new FileSystemPath("id2", "Test file system path 2"));

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
		factory = BlockExecutorFactory.build(registryManager, null, blockEventListenerFac);
	}

	@Test
	public void testRunWorkspace_01() throws Exception {
		try (InputStream in = getClass().getClassLoader().getResourceAsStream("workspaces/workspace_01.xml");) {
			Workspace w = BlockXmlUtils.loadWorkspace(in);

			TaskforceExecutionProperties execProperties = new TaskforceExecutionProperties();

			DefaultBlockExecutionContext context = new DefaultBlockExecutionContext(testWorkspaceId, factory,
					execProperties);
			context.setKeyValueStorageProvider(kvStorageProvider);
			context.setFileStorageProvider(fileStorageProvider);
			context.setEventDispatcher(eventDispatcher);
			WorkspaceExecutor wExecutor = new WorkspaceExecutor(w, context);
			wExecutor.execute();
		}
	}

	@Test
	public void testRunWorkspace_02() throws Exception {
		try (InputStream in = getClass().getClassLoader().getResourceAsStream("workspaces/workspace_02.xml");) {
			Workspace w = BlockXmlUtils.loadWorkspace(in);
			TaskforceExecutionProperties execProperties = new TaskforceExecutionProperties();
			DefaultBlockExecutionContext context = new DefaultBlockExecutionContext(testWorkspaceId, factory,
					execProperties);
			context.setKeyValueStorageProvider(kvStorageProvider);
			context.setFileStorageProvider(fileStorageProvider);
			context.setEventDispatcher(eventDispatcher);
			WorkspaceExecutor wExecutor = new WorkspaceExecutor(w, context);
			wExecutor.execute();
		}
	}

	@Test
	public void testRunWorkspace_04() throws Exception {
		try (InputStream in = getClass().getClassLoader().getResourceAsStream("workspaces/workspace_04.xml");) {
			Workspace w = BlockXmlUtils.loadWorkspace(in);
			TaskforceExecutionProperties execProperties = new TaskforceExecutionProperties();
			DefaultBlockExecutionContext context = new DefaultBlockExecutionContext(testWorkspaceId, factory,
					execProperties);
			context.setKeyValueStorageProvider(kvStorageProvider);
			context.setFileStorageProvider(fileStorageProvider);
			context.setEventDispatcher(eventDispatcher);
			WorkspaceExecutor wExecutor = new WorkspaceExecutor(w, context);
			wExecutor.execute();

			for (String name : context.getAllVariableNames()) {
				Object value = context.getVariable(name);
				System.out.println(name + "=" + value);
			}
		}
	}

	@Test
	public void testLoopFile_01() throws Exception {

		FileSystemPath p1 = registryManager.getCustomResourcePathLoader().getFileSystemPathById("id0");
		File pf1 = new File(p1.getPath());
		File f1 = new File(pf1, "test_file_01.txt");
		f1.createNewFile();

		try (InputStream in = getClass().getClassLoader().getResourceAsStream("workspaces/loop_file_01.xml");) {
			Workspace w = BlockXmlUtils.loadWorkspace(in);
			TaskforceExecutionProperties execProperties = new TaskforceExecutionProperties();
			DefaultBlockExecutionContext context = new DefaultBlockExecutionContext(testWorkspaceId, factory,
					execProperties);
			context.setEventDispatcher(eventDispatcher);
			context.setKeyValueStorageProvider(kvStorageProvider);
			context.setFileStorageProvider(fileStorageProvider);
			WorkspaceExecutor wExecutor = new WorkspaceExecutor(w, context);
			wExecutor.execute();
		}

		f1.delete();
	}

	@Test
	public void testLoopFile_02() throws Exception {

		FileSystemPath p1 = registryManager.getCustomResourcePathLoader().getFileSystemPathById("id0");
		File pf1 = new File(p1.getPath());
		File f1 = new File(pf1, "test_file_01.txt");
		f1.createNewFile();
		File f2 = new File(pf1, "test_file_02.txt");
		f2.createNewFile();
		File f3 = new File(pf1, "test_file_03.txt");
		f3.createNewFile();

		try (InputStream in = getClass().getClassLoader().getResourceAsStream("workspaces/loop_file_02.xml");) {
			Workspace w = BlockXmlUtils.loadWorkspace(in);
			TaskforceExecutionProperties execProperties = new TaskforceExecutionProperties();
			DefaultBlockExecutionContext context = new DefaultBlockExecutionContext(testWorkspaceId,
					factory, execProperties);
			context.setEventDispatcher(eventDispatcher);
			context.setKeyValueStorageProvider(kvStorageProvider);
			context.setFileStorageProvider(fileStorageProvider);
			WorkspaceExecutor wExecutor = new WorkspaceExecutor(w, context);
			wExecutor.execute();

			Thread.sleep(2000L);

			wExecutor.stop();

		}
		Thread.sleep(2000L);
		f1.delete();
		f2.delete();
		f3.delete();
	}

}
