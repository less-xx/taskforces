/**
 * 
 */
package org.teapotech.taskforce.task;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.teapotech.block.exception.BlockExecutionException;
import org.teapotech.block.exception.BlockExecutorNotFoundException;
import org.teapotech.block.exception.InvalidBlockException;
import org.teapotech.block.exception.InvalidBlockExecutorException;
import org.teapotech.block.executor.BlockExecutionContext;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.util.BlockExecutorUtils;

/**
 * @author jiangl
 *
 */
@Service
public class ResourceFetcher implements DockerBlockCallable {

	private static Logger LOG = LoggerFactory.getLogger(ResourceFetcher.class);

	@Autowired
	ResourceLoader resourceLoader;

	@Autowired
	BlockExecutionContext context;

	public String getResourceAsText(String url) throws IOException {

		LOG.info("Try to fetch resource from {}", url);
		Resource res = resourceLoader.getResource(url);
		return IOUtils.toString(res.getInputStream(), Charset.defaultCharset());
	}

	public BlockExecutionContext getContext() {
		return context;
	}

	@Override
	public Object call(Block block) throws InvalidBlockExecutorException, BlockExecutionException,
			BlockExecutorNotFoundException, InvalidBlockException {
		BlockValue pathBv = block.getBlockValueByName("path", block.getValues().get(0));
		Object url = BlockExecutorUtils.execute(pathBv, context);
		try {
			return getResourceAsText((String) url);
		} catch (IOException e) {
			throw new BlockExecutionException(e.getMessage(), e);
		}
	}

}
