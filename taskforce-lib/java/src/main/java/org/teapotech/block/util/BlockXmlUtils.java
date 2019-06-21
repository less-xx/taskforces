/**
 * 
 */
package org.teapotech.block.util;

import java.beans.Statement;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.teapotech.block.model.Block;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.model.Category;
import org.teapotech.block.model.Shadow;
import org.teapotech.block.model.Variable;
import org.teapotech.block.model.Workspace;

/**
 * @author jiangl
 *
 */
public class BlockXmlUtils {

	private static Class<?>[] BLOCK_CLASSES = { Workspace.class, Category.class, Block.class, BlockValue.class,
			Field.class, Shadow.class, Variable.class, Statement.class };
	private static JAXBContext BLOCK_CONTEXT = null;
	private static Unmarshaller BLOCK_UNMARSHALLER = null;
	private static Marshaller BLOCK_MARSHALLER = null;

	static {
		try {
			BLOCK_CONTEXT = JAXBContext.newInstance(BLOCK_CLASSES);
			BLOCK_UNMARSHALLER = BLOCK_CONTEXT.createUnmarshaller();
			BLOCK_MARSHALLER = BLOCK_CONTEXT.createMarshaller();
			BLOCK_MARSHALLER.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		} catch (Exception e) {
			throw new Error(e.getMessage(), e);
		}
	}

	public static Workspace loadWorkspace(String configuration) throws JAXBException {
		StringReader reader = new StringReader(configuration);
		StreamSource source = new StreamSource(reader);
		JAXBElement<Workspace> e = BLOCK_UNMARSHALLER.unmarshal(source, Workspace.class);
		return e.getValue();
	}

	public static Workspace loadWorkspace(Source source) throws JAXBException {
		JAXBElement<Workspace> e = BLOCK_UNMARSHALLER.unmarshal(source, Workspace.class);
		return e.getValue();
	}

	public static Workspace loadWorkspace(InputStream in) throws JAXBException {
		Object o = BLOCK_UNMARSHALLER.unmarshal(in);
		return (Workspace) o;
	}

	public static Block loadToolboxBlock(InputStream in) throws JAXBException {
		Workspace tbw = (Workspace) BLOCK_UNMARSHALLER.unmarshal(in);
		if (tbw == null) {
			return null;
		}
		return tbw.getBlocks().get(0);
	}

	public static String toXml(Object o) throws JAXBException {
		StringWriter writer = new StringWriter();
		BLOCK_MARSHALLER.marshal(o, writer);
		return writer.toString();
	}
}
