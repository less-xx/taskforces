/**
 * 
 */
package org.teapotech.block.executor;

import java.io.InputStream;
import java.util.Collection;

import org.teapotech.block.BlockExecutorFactory;
import org.teapotech.taskforce.provider.FileStorageException;

/**
 * @author jiangl
 *
 */
public interface BlockExecutionContext {

	public final static String KEY_CURRENT_BLOCK = "taskforce._currentBlock";

	String getWorkspaceId();

	BlockExecutorFactory getBlockExecutorFactory();

	Object getVariable(String id);

	void setVariable(String id, Object value);

	Collection<String> getAllVariableNames();

	void destroy();

	void storeFile(String key, InputStream in) throws FileStorageException;

	InputStream loadFile(String key) throws FileStorageException;

}
