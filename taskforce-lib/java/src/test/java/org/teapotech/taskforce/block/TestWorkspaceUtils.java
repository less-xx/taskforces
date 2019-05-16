/**
 * 
 */
package org.teapotech.taskforce.block;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.jupiter.api.Test;
import org.teapotech.block.model.Workspace;
import org.teapotech.block.util.BlockXmlUtils;

/**
 * @author jiangl
 *
 */
public class TestWorkspaceUtils {

	@Test
	public void testWorkspaceUtils_01() throws Exception {

		try (InputStream in = getClass().getClassLoader().getResourceAsStream("workspaces/workspace_01.xml");) {
			Workspace w = BlockXmlUtils.loadWorkspace(in);
			assertNotNull(w);
			assertNotNull(w.getVariables());
			assertNotNull(w.getVariables().get(0));
			assertEquals("b", w.getVariables().get(0).getValue());
			assertNotNull(w.getBlock());
			assertNotNull(w.getBlock().getFields());
			assertEquals("b", w.getBlock().getFields().get(0).getValue());
			assertNotNull(w.getBlock().getValues());
			assertNotNull(w.getBlock().getValues().get(0));
			assertNotNull(w.getBlock().getNext());
			assertNotNull(w.getBlock().getNext().getBlock());
			assertNotNull(w.getBlock().getNext().getBlock().getValues().get(0));
			assertNotNull(w.getBlock().getNext().getBlock().getValues().get(0).getBlock());
			assertNotNull(w.getBlock().getNext().getBlock().getValues().get(0).getBlock().getFields());
			assertNotNull(
					w.getBlock().getNext().getBlock().getValues().get(0).getBlock().getFields().get(0).getValue());
			assertNotNull(w.getBlock().getNext().getBlock().getNext().getBlock().getStatements());
			assertEquals(4, w.getBlock().getNext().getBlock().getNext().getBlock().getStatements().size());
		}
	}

	@Test
	public void testWorkspaceUtils_02() throws Exception {

		try (InputStream in = getClass().getClassLoader().getResourceAsStream("workspaces/workspace_02.xml");) {
			Source source = new StreamSource(in);
			Workspace w = BlockXmlUtils.loadWorkspace(source);
			assertNotNull(w);
			assertNotNull(w.getBlock());
			assertNotNull(w.getBlock().getValues());
			assertNotNull(w.getBlock().getValues().get(0).getShadow());
			assertNotNull(w.getBlock().getValues().get(0).getBlock());
			assertNotNull(w.getBlock().getNext());
			assertNotNull(w.getBlock().getNext().getBlock());
			assertNotNull(w.getBlock().getNext().getBlock().getValues());
			assertNotNull(w.getBlock().getNext().getBlock().getValues().get(0).getShadow());
			assertNotNull(w.getBlock().getNext().getBlock().getValues().get(0).getBlock());
			assertNotNull(
					w.getBlock().getNext().getBlock().getNext().getBlock().getValues().get(0).getBlock().getMutation());
			assertNotNull(
					w.getBlock().getNext().getBlock().getNext().getBlock().getValues().get(0).getBlock().getValues());
			assertNotNull(
					w.getBlock().getNext().getBlock().getNext().getBlock().getValues().get(0).getBlock().getValues()
							.get(1));
		}
	}
}
