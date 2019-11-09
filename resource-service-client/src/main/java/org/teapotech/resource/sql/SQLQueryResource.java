/**
 * 
 */
package org.teapotech.resource.sql;

import java.util.List;
import java.util.Map;

import org.teapotech.resource.ResourceParameter;

/**
 * @author jiangl
 *
 */
public class SQLQueryResource extends SQLResource<List<Map<String, Object>>> {

	public static final ResourceParameter<Integer> PARAM_LIMIT = new ResourceParameter<Integer>("limit", Integer.class,
			false, 100);
	public static final ResourceParameter<Integer> PARAM_OFFSET = new ResourceParameter<Integer>("offset",
			Integer.class, false, 0);

	public SQLQueryResource() {
		super();
		boundParameters.add(PARAM_LIMIT);
		boundParameters.add(PARAM_OFFSET);
	}
}
