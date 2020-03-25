package com.calix.cnap.ipfix.jnca.cai.utils;

import java.util.StringTokenizer;

public class SuperString {
	public String str;

	public String words[];

	public int length;

	public SuperString(String str) {
		this.str = str;

		int i = 0;

		while (i < this.str.length() && this.str.charAt(i) == ' ')
			i++;

		if (i != 0)
			if (i == this.str.length())
				this.str = new String();
			else
				this.str = this.str.substring(i);

		StringTokenizer st = new StringTokenizer(this.str);

		length = st.countTokens();
		words = new String[length];

		for (i = 0; st.hasMoreTokens(); i++)
			words[i] = st.nextToken();
	}

	public String getWord(int index) {
		return index <= length && index > 0 ? words[index - 1] : null;
	}

	public String skipWords(int count, char strip) {
		String ret;

		try {
			String str = new String(this.str);
			int index = 0;

			while (count-- > 0) {
				index = str.indexOf(' ', index);

				if (index == -1)
					throw new StringIndexOutOfBoundsException("" + index);

				while (str.charAt(++index) == ' ')
					;
			}

			ret = str.substring(index);

			if (strip != 0)
				if (ret.charAt(0) == strip)
					ret = ret.substring(1);

		} catch (StringIndexOutOfBoundsException e) {
			ret = new String();
		}

		return ret;
	}

	public String toString() {
		return str;
	}

	public static String exceptionMsg(String str) {
		int index = str.indexOf(": ");

		return index == -1 ? str : str.substring(index + 2);
	}
}
