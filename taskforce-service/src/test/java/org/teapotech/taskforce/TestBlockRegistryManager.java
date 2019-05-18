/**
 * 
 */
package org.teapotech.taskforce;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.teapotech.block.model.Workspace;
import org.teapotech.block.util.BlockXmlUtils;
import org.teapotech.taskforce.service.BlockRegistryService;

/**
 * @author jiangl
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableAutoConfiguration
@ComponentScan("org.teapotech.taskforce")
@SpringJUnitConfig(SpringBootContextLoader.class)
public class TestBlockRegistryManager {

	@Autowired
	BlockRegistryService blockRegService;

	@Test
	public void testBlockRegistryManager() throws Exception {
		Workspace toolbox = blockRegService.getToolboxConfiguration();

		String xml = BlockXmlUtils.toXml(toolbox);
		System.out.println(xml);
	}
}
