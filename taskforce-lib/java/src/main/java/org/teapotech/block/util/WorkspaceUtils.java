/**
 * 
 */
package org.teapotech.block.util;

import java.io.InputStream;
import java.lang.reflect.Field;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;

import org.springframework.cglib.core.Block;
import org.teapotech.block.model.BlockValue;
import org.teapotech.block.model.Shadow;
import org.teapotech.block.model.Variable;
import org.teapotech.block.model.Workspace;

/**
 * @author jiangl
 *
 */
public class WorkspaceUtils {

	private static Class<?>[] BLOCK_CLASSES = { Workspace.class, Block.class, BlockValue.class, Field.class,
			Shadow.class, Variable.class };

	public static Workspace loadWorkspace(Source source) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Workspace.class);
		Unmarshaller um = context.createUnmarshaller();
		JAXBElement<Workspace> e = um.unmarshal(source, Workspace.class);
		return e.getValue();
	}

	public static Workspace loadWorkspace(InputStream in) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(BLOCK_CLASSES);
		Unmarshaller um = context.createUnmarshaller();
		Object o = um.unmarshal(in);
		return (Workspace) o;
	}

}
