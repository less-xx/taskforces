package org.teapotech.taskforce.block;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.teapotech.block.model.Block;
import org.teapotech.block.model.Category;
import org.teapotech.block.model.Workspace;
import org.teapotech.block.util.BlockXmlUtils;

public class TestBlockXmlUtil {

	@Test
	public void testBlockXmlUtil_01() throws Exception {

		InputStream in = getClass().getClassLoader().getResourceAsStream("toolbox-config/file_path.xml");
		Block tBlock = BlockXmlUtils.loadToolboxBlock(in);
		assertNotNull(tBlock.getValues());
		assertTrue(tBlock.getValues().size() > 0);
	}

	@Test
	public void testBlockXmlUtil_02() throws Exception {

		Workspace toolbox = new Workspace();
		Category c = toolbox.getCategoryByName("Core/Control", true);
		c = toolbox.getCategoryByName("Core/Logic", true);
		c = toolbox.getCategoryByName("Custom/Resource", true);
		c.setBlocks(new ArrayList<>());

		InputStream in = getClass().getClassLoader().getResourceAsStream("toolbox-config/file_path.xml");
		Block tBlock = BlockXmlUtils.loadToolboxBlock(in);
		c.getBlocks().add(tBlock);

		String xml = BlockXmlUtils.toXml(toolbox);
		System.out.println(xml);

	}

}
