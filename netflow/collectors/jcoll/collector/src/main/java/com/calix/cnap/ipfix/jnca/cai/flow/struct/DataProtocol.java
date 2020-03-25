package com.calix.cnap.ipfix.jnca.cai.flow.struct;

import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;

import com.calix.cnap.ipfix.jnca.cai.flow.collector.interpretator.PT_Error;
import com.calix.cnap.ipfix.jnca.cai.flow.collector.interpretator.PT_ExecError;
import com.calix.cnap.ipfix.jnca.cai.flow.collector.interpretator.RuleSet;
import com.calix.cnap.ipfix.jnca.cai.flow.packets.Flow;
import com.calix.cnap.ipfix.jnca.cai.utils.Program;
import com.calix.cnap.ipfix.jnca.cai.utils.Syslog;

class Rule {
	public String protocol;

	public RuleSet program;

	public Rule(String protocol, RuleSet program) {
		this.protocol = protocol;
		this.program = program;
	}
}

abstract public class DataProtocol {
	static private LinkedList protocol_list = init_protocol_list();

	static private LinkedList init_protocol_list() {
		LinkedList ret = new LinkedList();

		try {
			Program program = new Program("Protocols");
			ListIterator e = program.listIterator();

			while (e.hasNext()) {
				String[] obj = (String[]) e.next();

				try {
					RuleSet rule = RuleSet.create(obj[1], obj[0]);

					ret.add(new Rule(obj[0], rule));
				} catch (PT_Error exc) {
					program.error(exc.toString());
				}
			}
		} catch (IOException exc) {
			Syslog.log.syslog(Syslog.LOG_ERR,
					"I/O error while reading Protocols definition");
			exc.printStackTrace();
		}

		return ret;
	}

	public static String aggregate(Flow flow) {
		ListIterator e = protocol_list.listIterator(0);

		while (e.hasNext()) {
			Rule rule = (Rule) e.next();

			try {
				if (rule.program.exec(flow)) {
					/*
					 * System.out.println( "srcpor="+flow.getSrcPort()+ ",
					 * dstport="+flow.getDstPort()+ ", prot="+flow.getProto()+ "
					 * it is ["+rule.protocol+"]" );
					 */
					return rule.protocol;
				}
			} catch (PT_ExecError exc) {
				Syslog.log.syslog(Syslog.LOG_ERR, "BUG: aggregate protocol: "
						+ exc.toString());
			}
		}

		return "Other";
	}

}
