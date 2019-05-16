/**
 * 
 */
package org.teapotech.block.util;

import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;

import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.model.Shadow;
import org.teapotech.block.model.Variable;
import org.teapotech.block.model.Workspace;

/**
 * @author jiangl
 *
 */
public class BlockXmlUtils {

	private static Class<?>[] BLOCK_CLASSES = { Workspace.class, Block.class, BlockValue.class, Field.class,
			Shadow.class, Variable.class };
	private static JAXBContext BLOCK_CONTEXT = null;
	private static Unmarshaller BLOCK_UNMARSHALLER = null;
	private static Marshaller BLOCK_MARSHALLER = null;

	static {
		try {
			BLOCK_CONTEXT = JAXBContext.newInstance(BLOCK_CLASSES);
			BLOCK_UNMARSHALLER = BLOCK_CONTEXT.createUnmarshaller();
			BLOCK_MARSHALLER = BLOCK_CONTEXT.createMarshaller();
		} catch (Exception e) {
			throw new Error(e.getMessage(), e);
		}
	}

	public static Workspace loadWorkspace(Source source) throws JAXBException {
		JAXBElement<Workspace> e = BLOCK_UNMARSHALLER.unmarshal(source, Workspace.class);
		return e.getValue();
	}

	public static Workspace loadWorkspace(InputStream in) throws JAXBException {
		Object o = BLOCK_UNMARSHALLER.unmarshal(in);
		return (Workspace) o;
	}

	public static String blockToXml(Block block) throws JAXBException {
		StringWriter writer = new StringWriter();
		BLOCK_MARSHALLER.marshal(block, writer);
		return writer.toString();
	}

	public static Block xmlToBlock(Source xmlSource) throws JAXBException {
		return (Block) BLOCK_UNMARSHALLER.unmarshal(xmlSource);
	}

	public static Block xmlToBlock(InputStream in) throws JAXBException {
		return (Block) BLOCK_UNMARSHALLER.unmarshal(in);
	}

}