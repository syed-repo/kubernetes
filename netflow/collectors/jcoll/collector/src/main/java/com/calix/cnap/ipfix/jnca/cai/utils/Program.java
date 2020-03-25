package com.calix.cnap.ipfix.jnca.cai.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * 类似于Property的结构
 * 
 * @author CaiMao
 * 
 */
public class Program extends LinkedList {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7282648229289878952L;

	private static final String keyValueSeparators = "=: \t\r\n\f";

	private static final String strictKeyValueSeparators = "=:";

	// private static final String specialSaveChars = "=: \t\r\n\f#!";
	private static final String whiteSpaceChars = " \t\r\n\f";

	private String file_name;

	public void error(String str) {
		System.err.println(file_name + ": " + str);
		System.exit(0);
	}

	public Program(String file) throws IOException {
		file_name = Params.path + File.separator + "etc"
				+ File.separator + file + ".aggregate";

		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(file_name), Params.encoding));

		while (true) {
			String line = in.readLine();

			if (line == null)
				return;

			if (line.length() > 0) {
				char firstChar = line.charAt(0);

				if ((firstChar != '#') && (firstChar != '!')) {
					while (continueLine(line)) {
						String nextLine = in.readLine();

						if (nextLine == null)
							nextLine = new String("");

						String loppedLine = line
								.substring(0, line.length() - 1);
						int startIndex = 0;

						for (startIndex = 0; startIndex < nextLine.length(); startIndex++)
							if (whiteSpaceChars.indexOf(nextLine
									.charAt(startIndex)) == -1)
								break;

						nextLine = nextLine.substring(startIndex, nextLine
								.length());
						line = new String(loppedLine + nextLine);
					}

					int len = line.length();
					int keyStart;

					for (keyStart = 0; keyStart < len; keyStart++) {
						if (whiteSpaceChars.indexOf(line.charAt(keyStart)) == -1)
							break;
					}

					int separatorIndex;

					for (separatorIndex = keyStart; separatorIndex < len; separatorIndex++) {
						char currentChar = line.charAt(separatorIndex);

						if (currentChar == '\\')
							separatorIndex++;
						else if (keyValueSeparators.indexOf(currentChar) != -1)
							break;
					}

					int valueIndex;

					for (valueIndex = separatorIndex + 1; valueIndex < len; valueIndex++)
						if (whiteSpaceChars.indexOf(line.charAt(valueIndex)) == -1)
							break;

					if (valueIndex < len)
						if (strictKeyValueSeparators.indexOf(line
								.charAt(valueIndex)) != -1)
							valueIndex++;

					while (valueIndex < len) {
						if (whiteSpaceChars.indexOf(line.charAt(valueIndex)) == -1)
							break;

						valueIndex++;
					}

					String[] obj = new String[2];

					obj[0] = loadConvert(line.substring(keyStart,
							separatorIndex));
					obj[1] = loadConvert((separatorIndex < len) ? line
							.substring(valueIndex, len) : "");
					add(obj);
				}
			}
		}
	}

	private boolean continueLine(String line) {
		int slashCount = 0;
		int index = line.length() - 1;

		while ((index >= 0) && (line.charAt(index--) == '\\'))
			slashCount++;

		return (slashCount % 2 == 1);
	}

	private String loadConvert(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);

		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);

			if (aChar == '\\') {
				aChar = theString.charAt(x++);

				if (aChar == 'u') {
					int value = 0;

					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);

						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed \\uxxxx encoding.");
						}
					}

					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}

		return outBuffer.toString();
	}

	/*
	 * private String saveConvert( String theString ) { char aChar; int len =
	 * theString.length(); StringBuffer outBuffer = new StringBuffer(len*2);
	 * 
	 * for(int x=0; x<len; ) { aChar = theString.charAt(x++);
	 * 
	 * switch( aChar ) { case '\\': outBuffer.append('\\');
	 * outBuffer.append('\\'); continue; case '\t': outBuffer.append('\\');
	 * outBuffer.append('t'); continue; case '\n': outBuffer.append('\\');
	 * outBuffer.append('n'); continue; case '\r': outBuffer.append('\\');
	 * outBuffer.append('r'); continue; case '\f': outBuffer.append('\\');
	 * outBuffer.append('f'); continue; default: if ((aChar < 20) || (aChar >
	 * 127)) { outBuffer.append('\\'); outBuffer.append('u');
	 * outBuffer.append(toHex((aChar >> 12) & 0xF));
	 * outBuffer.append(toHex((aChar >> 8) & 0xF));
	 * outBuffer.append(toHex((aChar >> 4) & 0xF));
	 * outBuffer.append(toHex((aChar >> 0) & 0xF)); } else { if
	 * (specialSaveChars.indexOf(aChar) != -1) outBuffer.append('\\');
	 * 
	 * outBuffer.append(aChar); } } }
	 * 
	 * return outBuffer.toString(); }
	 * 
	 * private static char toHex( int nibble ) { return hexDigit[(nibble &
	 * 0xF)]; }
	 * 
	 * private static final char[] hexDigit = {
	 * '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F' };
	 */
}
