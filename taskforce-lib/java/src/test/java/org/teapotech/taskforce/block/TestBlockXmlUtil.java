package org.teapotech.taskforce.block;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.teapotech.block.model.Block;
import org.teapotech.block.util.BlockXmlUtils;

public class TestBlockXmlUtil {

	@Test
	public void testBlockXmlUtil_01() throws Exception {

		InputStream in = getClass().getClassLoader().getResourceAsStream("toolbox-config/file_path.xml");
		Block tBlock = BlockXmlUtils.loadToolboxBlock(in);
		assertNotNull(tBlock.getValues());
		assertTrue(tBlock.getValues().size() > 0);
	}
}
