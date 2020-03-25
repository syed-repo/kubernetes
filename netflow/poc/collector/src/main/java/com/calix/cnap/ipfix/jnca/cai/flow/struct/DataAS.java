package com.calix.cnap.ipfix.jnca.cai.flow.struct;

import java.io.IOException;
import java.util.Hashtable;
import java.util.ListIterator;
import java.util.StringTokenizer;

import com.calix.cnap.ipfix.jnca.cai.utils.Program;
import com.calix.cnap.ipfix.jnca.cai.utils.Syslog;

abstract public class DataAS {
	public static final int AS_Destination = 0;

	public static final int AS_Source = 1;

	public static String aggregate(long as, int table) {
		switch (table) {
		case AS_Source:
			return aggregate_as(src_as_list, as);
		case AS_Destination:
			return aggregate_as(dst_as_list, as);
		}

		throw new IllegalArgumentException("Invalid AS type: " + table);
	}

	static private Hashtable dst_as_list = init_as_list("AS_Destination");

	static private Hashtable src_as_list = init_as_list("AS_Source");

	static private Hashtable init_as_list(String name) {
		Hashtable ret = new Hashtable();

		try {
			Program program = new Program(name);//¶Á³ö.aggregateÎÄ¼þ
			ListIterator e = program.listIterator();

			while (e.hasNext()) {
				String[] obj = (String[]) e.next();
				String as_name = obj[0];

				for (StringTokenizer st = new StringTokenizer(obj[1]); st
						.hasMoreElements();) {
					String as_number = st.nextToken();
					int minus = as_number.indexOf('-');

					if (minus == -1)
						ret.put(as_number, as_name);
					else if (as_number.indexOf('-', minus + 1) != -1)
						program.error("value `" + as_number + "' of `"
								+ as_name + "' is invalid");
					else {
						int start = 0, stop = 0;
						String s_start = as_number.substring(0, minus);
						String s_stop = as_number.substring(minus + 1);

						try {
							start = Integer.parseInt(s_start);
						} catch (NumberFormatException e1) {
							program.error("value `" + s_start + "' of `"
									+ as_name + "' isn't integer");
						}

						try {
							stop = Integer.parseInt(s_stop);
						} catch (NumberFormatException e2) {
							program.error("value `" + s_stop + "' of `"
									+ as_name + "' isn't integer");
						}

						for (int i = start; i <= stop; i++)
							ret.put("" + i, as_name);
					}
				}
			}
		} catch (IOException exc) {
			Syslog.log.syslog(Syslog.LOG_ERR, "I/O error while reading " + name
					+ " definition");
			exc.printStackTrace();
		}

		return ret;
	}

	private static String aggregate_as(Hashtable as_list, long as) {
		String ret = (String) as_list.get("" + as);

		return ret == null ? "Other" : ret;
	}

}
