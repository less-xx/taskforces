/**
 * 
 */
package org.teapotech.taskforce.expert;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

/**
 * @author jiangl
 *
 */
@Service
public class ResourceFetcher {

	private static Logger LOG = LoggerFactory.getLogger(ResourceFetcher.class);

	@Autowired
	ResourceLoader resourceLoader;

	public String getResourceAsText(String url) throws IOException {

		LOG.info("Try to fetch resource from {}", url);
		Resource res = resourceLoader.getResource(url);
		return IOUtils.toString(res.getInputStream(), Charset.defaultCharset());
	}
}
