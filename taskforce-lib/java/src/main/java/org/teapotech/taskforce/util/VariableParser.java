/**
 * 
 */
package org.teapotech.taskforce.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jiangl
 *
 */
public class VariableParser {

	private String delimiterPrefix = "${";
	private String delimiterSuffix = "}";
	private boolean validateVariableName = true;

	public VariableParser() {
	}

	public VariableParser(boolean validateVariableName) {
		this.validateVariableName = validateVariableName;
	}

	public VariableParser(String delimiterPrefix, String delimiterSuffix, boolean validateVariableName) {
		this(validateVariableName);
		this.delimiterPrefix = delimiterPrefix;
		this.delimiterSuffix = delimiterSuffix;
	}

	public String getDelimiterPrefix() {
		return delimiterPrefix;
	}

	public void setDelimiterPrefix(String delimiterPrefix) {
		this.delimiterPrefix = delimiterPrefix;
	}

	public String getDelimiterSuffix() {
		return delimiterSuffix;
	}

	public void setDelimiterSuffix(String delimiterSuffix) {
		this.delimiterSuffix = delimiterSuffix;
	}

	public boolean isValidateVariableName() {
		return validateVariableName;
	}

	public void setValidateVariableName(boolean validateVariableName) {
		this.validateVariableName = validateVariableName;
	}

	public String formatVariable(String var) {
		return this.delimiterPrefix + var + this.delimiterSuffix;
	}

	public boolean isJustVariable(String input) {
		if (input == null) {
			return false;
		}

		String s = input.trim();
		if (!s.startsWith(delimiterPrefix)) {
			return false;
		}
		if (!s.endsWith(delimiterSuffix)) {
			return false;
		}
		s = s.substring(delimiterPrefix.length());
		s = s.substring(0, s.length() - delimiterSuffix.length()).trim();
		try {
			validateVariableName(s);
			return true;
		} catch (InvalidVariableException e) {
			return false;
		}
	}

	public String formatVariablesInText(String text) throws Exception {
		List<String> vars = findVariables(text);
		for (String var : vars) {
			text = text.replaceAll("\\s*" + var + "\\s*", var);
		}
		return text;
	}

	public List<String> findVariables(String input) throws InvalidVariableException {

		final List<String> variableNames = new ArrayList<>();
		parse(input, new VariableHandler() {

			@Override
			public void handleVariable(String var) {
				variableNames.add(var);
			}

			@Override
			public void handleNoneVariableChar(char c) {
			}
		});

		return variableNames;
	}

	private void parse(String input, VariableHandler varHandler) throws InvalidVariableException {
		char[] chars = input.toCharArray();
		char[] prefixChars = delimiterPrefix.toCharArray();
		char[] suffixChars = delimiterSuffix.toCharArray();
		boolean foundPrefix = false, foundSuffix = false;
		StringBuilder sbVarName = null;
		for (int i = 0; i < chars.length; i++) {
			if (!foundPrefix) {
				foundPrefix = isSame(chars, i, prefixChars);
				if (foundPrefix) {
					i = i + prefixChars.length - 1;
					sbVarName = new StringBuilder();
				} else if (varHandler != null) {
					varHandler.handleNoneVariableChar(chars[i]);
				}
				continue;
			}

			if (!foundSuffix) {
				foundSuffix = isSame(chars, i, suffixChars);
				if (foundSuffix) {
					String vn = sbVarName.toString().trim();
					if (this.validateVariableName) {
						validateVariableName(vn);
					}
					if (varHandler != null) {
						varHandler.handleVariable(vn);
					}
					foundPrefix = foundSuffix = false;
					sbVarName = null;

					i = i + suffixChars.length - 1;

				} else {
					if (sbVarName != null) {
						sbVarName.append(chars[i]);
					}
				}

			}
		}
	}

	private boolean isSame(char[] cc, int start, char[] target) {
		if (start > cc.length) {
			return false;
		}
		for (int i = 0; i < target.length; i++) {
			if (cc[start + i] != target[i]) {
				return false;
			}
		}
		return true;
	}

	public String replaceVariable(String input, String replacement) throws InvalidVariableException {
		StringBuilder output = new StringBuilder();
		parse(input, new VariableHandler() {

			@Override
			public void handleVariable(String var) {
				output.append(replacement);
			}

			@Override
			public void handleNoneVariableChar(char c) {
				output.append(c);
			}
		});

		return output.toString();
	}

	public void validateVariableName(String varName) throws InvalidVariableException {

		if (varName.isEmpty()) {
			throw new InvalidVariableException("Found empty variable name.");
		}
		char[] chars = varName.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (i == 0 && ((c >= '0' && c <= '9') || c == '.')) {
				throw new InvalidVariableException(". or digit must not be the first char in " + varName);
			}

			if (!(c >= 'A' && c <= 'Z' || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '_' || c == '.')) {
				throw new InvalidVariableException("Not a invalid variable name " + varName);
			}
			if (i == chars.length - 1 && c == '.') {
				throw new InvalidVariableException(". must not be the last char in " + varName);
			}
		}
	}

	private interface VariableHandler {
		void handleVariable(String var);

		void handleNoneVariableChar(char c);
	}
}
