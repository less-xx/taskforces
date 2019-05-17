package org.teapotech.taskforce.block;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.teapotech.block.model.toolbox.Category;
import org.teapotech.block.model.toolbox.Toolbox;
import org.teapotech.block.model.toolbox.ToolboxBlock;
import org.teapotech.block.util.BlockXmlUtils;

public class TestBlockXmlUtil {

	@Test
	public void testBlockXmlUtil_01() throws Exception {

		InputStream in = getClass().getClassLoader().getResourceAsStream("toolbox-config/file_path.xml");
		ToolboxBlock tBlock = BlockXmlUtils.loadToolboxBlock(in);
		assertNotNull(tBlock.getValues());
		assertTrue(tBlock.getValues().size() > 0);
	}

	@Test
	public void testBlockXmlUtil_02() throws Exception {

		Toolbox toolbox = new Toolbox();
		Category c = toolbox.getCategoryByName("Core/Control");
		c = toolbox.getCategoryByName("Core/Logic");
		c = toolbox.getCategoryByName("Custom/Resource");
		c.setBlocks(new ArrayList<>());

		InputStream in = getClass().getClassLoader().getResourceAsStream("toolbox-config/file_path.xml");
		ToolboxBlock tBlock = BlockXmlUtils.loadToolboxBlock(in);
		c.getBlocks().add(tBlock);

		String xml = BlockXmlUtils.toXml(toolbox);
		System.out.println(xml);

	}

}
