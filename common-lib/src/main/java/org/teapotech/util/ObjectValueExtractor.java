/**
 * 
 */
package org.teapotech.util;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.FloatNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.LongNode;

/**
 * @author jiangl
 *
 */
public class ObjectValueExtractor {

	private static Logger LOG = LoggerFactory.getLogger(ObjectValueExtractor.class);
	private static Pattern ARRAY_PATTERN = Pattern.compile("([a-zA-Z0-9_]*)\\s*\\[\\s*(\\d+)\\s*\\]");

	public static Object getPropertyValue(Object obj, String propName) throws ObjectValueExtractException {
		if (propName == null || propName.isEmpty()) {
			return obj;
		}
		String attr = propName;
		int i = attr.indexOf(".");
		Object value = obj;
		while (i > 0) {
			String prop = attr.substring(0, i);
			attr = attr.substring(i + 1);
			value = getDirectPropertyValue(value, prop);
			i = attr.indexOf(".");
		}

		return getDirectPropertyValue(value, attr);
	}

	public static Object getPropertyValue(Object obj, String propName, Map<String, Object> defaultValues)
			throws ObjectValueExtractException {
		Object value = getPropertyValue(obj, propName);
		if (value == null) {
			value = getPropertyValue(defaultValues, propName);
		}
		return value;
	}

	private static ArrayVar parseArrayVariable(String propName) {
		Matcher m = ARRAY_PATTERN.matcher(propName);
		if (m.find()) {
			ArrayVar av = new ArrayVar();
			av.name = m.group(1);
			av.index = Integer.parseInt(m.group(2));
			return av;
		}
		return null;
	}

	private static Object getDirectPropertyValue(Object obj, String propName) throws ObjectValueExtractException {
		if (propName == null || propName.isEmpty()) {
			return obj;
		}
		Object value = null;
		if (obj instanceof Map<?, ?>) {
			Map<?, ?> map = (Map<?, ?>) obj;
			value = map.get(propName);
		} else if (obj instanceof DynaBean) {
			DynaBean dbean = (DynaBean) obj;
			value = dbean.get(propName);
		} else if (obj instanceof JsonNode) {
			JsonNode jn = (JsonNode) obj;
			JsonNode valueNode = null;

			ArrayVar arrayVar = parseArrayVariable(propName);
			if (arrayVar == null) {
				valueNode = jn.get(propName);
			} else {
				if (arrayVar.index < 0) {
					LOG.error("Invalid array index for property {}", propName);
					return null;
				}
				if (StringUtils.isNotBlank(arrayVar.name)) {
					valueNode = jn.get(arrayVar.name);
				} else {
					valueNode = jn;
				}
				if (valueNode == null) {
					LOG.error("Cannot find array value for property {}", propName);
					return null;
				}
				if (arrayVar.index >= valueNode.size()) {
					LOG.error("Array index out of bound for property {}", propName);
					return null;
				}
				valueNode = valueNode.get(arrayVar.index);
			}

			if (valueNode == null) {
				LOG.error("Cannot find array value for property {}", propName);
				return null;
			}
			JsonNodeType jnt = valueNode.getNodeType();
			if (jnt == JsonNodeType.MISSING || jnt == JsonNodeType.NULL) {
				value = null;
			} else if (jnt == JsonNodeType.BOOLEAN) {
				value = valueNode.asBoolean();
			} else if (jnt == JsonNodeType.NUMBER) {
				if (valueNode instanceof IntNode) {
					value = valueNode.asInt();
				} else if (valueNode instanceof LongNode) {
					value = valueNode.asLong();
				} else if (valueNode instanceof FloatNode || valueNode instanceof DoubleNode) {
					value = valueNode.asDouble();
				} else {
					value = valueNode.asText();
				}
			} else if (jnt == JsonNodeType.STRING) {
				value = valueNode.asText();
			} else if (jnt == JsonNodeType.ARRAY) {
				List<JsonNode> list = new ArrayList<>();
				for (final JsonNode itemNode : valueNode) {
					list.add(itemNode);
				}
				value = list;
			} else {
				value = valueNode;
			}
		} else if (obj instanceof String) {
			if (StringUtils.isEmpty(propName)) {
				return obj;
			}
			LOG.error("String does not have property {}", propName);
		} else {
			try {
				value = PropertyUtils.getProperty(obj, propName);
			} catch (Exception e) {
				LOG.error(e.getMessage());
				value = null;
			}
		}
		if (value == null) {
			// throw new ObjectValueExtractException("Cannot find value for property '" +
			// propName + "'");
			LOG.error("Cannot find value for property {}", propName);
		}
		return value;
	}

	public static Map<String, Object> toMap(Object obj) {

		if (obj == null) {
			return null;
		}
		if (obj instanceof String) {
			throw new IllegalArgumentException("String is not supported.");
		}
		if (obj instanceof Map) {
			Map<?, ?> m = (Map<?, ?>) obj;
			Map<String, Object> values = new TreeMap<>();
			for (Entry<?, ?> entry : m.entrySet()) {
				values.put(entry.getKey().toString(), entry.getValue());
			}
			return values;
		}
		if (obj instanceof JsonNode) {
			JsonNode n = (JsonNode) obj;
			Iterator<String> it = n.fieldNames();
			Map<String, Object> values = new TreeMap<>();
			while (it.hasNext()) {
				String key = it.next();
				values.put(key, n.get(key));
			}
			return values;
		} else {
			Map<String, Object> values = new TreeMap<>();
			PropertyDescriptor[] propDescs = PropertyUtils.getPropertyDescriptors(obj);
			for (PropertyDescriptor pd : propDescs) {
				if (pd.getReadMethod() != null && !pd.isHidden()) {
					try {
						Object pv = PropertyUtils.getProperty(obj, pd.getName());
						values.put(pd.getName(), pv);
					} catch (Exception e) {
						LOG.error(e.getMessage(), e);
					}
				}
			}
			return values;
		}
	}

	private static class ArrayVar {
		String name;
		int index = -1;
	}
}
