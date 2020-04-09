package com.calix.cnap.ipfix.jnca.cai.flow.packets;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.calix.cnap.ipfix.jnca.cai.flow.struct.Prefix;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataASMatrix;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataDstAS;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataInterface;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataInterfaceMatrix;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataPrefix;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataPrefixMatrix;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataProtocol;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataSrcAS;
import com.calix.cnap.ipfix.jnca.cai.sql.SQL;
import com.calix.cnap.ipfix.jnca.cai.utils.DoneException;
import com.calix.cnap.ipfix.jnca.cai.utils.Params;
import com.calix.cnap.ipfix.jnca.cai.utils.Syslog;
import com.calix.cnap.ipfix.jnca.cai.utils.Util;

/*

 V8 Flow SrcPrefix structure

 +-------------------------------------------------------------------------------------+
 | Bytes | Contents   | Description                                                    |
 |-------+------------+----------------------------------------------------------------|
 | 0-3   | flows      | Number of flows                                                |
 |-------+------------+----------------------------------------------------------------|
 | 4-7   | dPkts      | Packets in the flow                                            |
 |-------+------------+----------------------------------------------------------------|
 | 8-11  | dOctets    | Total number of Layer 3 bytes in the packets of the flow       |
 |-------+------------+----------------------------------------------------------------|
 | 12-15 | First      | SysUptime, in seconds, at start of flow                        |
 |-------+------------+----------------------------------------------------------------|
 | 16-19 | Last       | SysUptime, in seconds, at the time the last packet of the flow |
 |       |            | was received                                                   |
 |-------+------------+----------------------------------------------------------------|
 | 20-23 | src_prefix | Source IP address prefix                                       |
 |-------+------------+----------------------------------------------------------------|
 | 24    | src_mask   | Source address prefix mask                                     |
 |-------+------------+----------------------------------------------------------------|
 | 25    | pad        | Unused (zero) bytes                                            |
 |-------+------------+----------------------------------------------------------------|
 | 26-27 | src_as     | Source autonomous system number, either origin or peer         |
 |-------+------------+----------------------------------------------------------------|
 | 28-29 | input      | Interface index (ifindex) of input interface                   |
 |-------+------------+----------------------------------------------------------------|
 | 30-31 | reserved   | Unused (zero) bytes                                            |
 +-------------------------------------------------------------------------------------+

 */

public class V8_FlowSrcPrefix extends V8_Flow {
	long src_prefix;

	byte src_mask;

	long src_as, input;

	Prefix srcprefix;

	public V8_FlowSrcPrefix(String RouterIP, byte[] buf, int off)
			throws DoneException {
		super(RouterIP, buf, off);

		src_prefix = Util.to_number(buf, off + 20, 4);
		src_mask = buf[off + 24];
		src_as = Util.to_number(buf, off + 26, 2);
		input = Util.to_number(buf, off + 28, 2);

		srcprefix = new Prefix(src_prefix, src_mask);

		if (Syslog.log.need(Syslog.LOG_INFO)) {
			Syslog.log.syslog(Syslog.LOG_INFO, "      <- AS " + src_as + " "
					+ srcprefix.toString() + ", " + flows + " flows");
			Syslog.log.syslog(Syslog.LOG_INFO, "        bytes=" + dOctets
					+ ", pkts=" + dPkts + ", inIf=" + input);
		}
	}

	public Long getSrcAS() {
		return new Long(src_as);
	}

	public Long getInIf() {
		return new Long(input);
	}

	public Prefix getSrcPrefix() {
		return srcprefix;
	}

	void fill_specific(PreparedStatement add_raw_stm) throws SQLException {
		add_raw_stm.setString(13, Util.str_addr(src_prefix));
		add_raw_stm.setInt(14, (int) src_mask);
		add_raw_stm.setInt(15, (int) src_as);
		add_raw_stm.setInt(16, (int) input);
		add_raw_stm.setString(17, Params.getCurrentTime());
	}

	String table_name() {
		return new String("SrcPrefix");
	}

	protected static String add_raw_sql = null;

	String get_text_raw_insert(SQL sql) {
		return add_raw_sql == null ? SQL.resources
				.getAndTrim("SQL.Add.RawV8.SrcPrefix") : add_raw_sql;
	}

	PreparedStatement get_sql_raw_insert(SQL sql) throws SQLException {
		return sql.prepareStatement("Prepare INSERT to V8 SrcPrefix raw table",
				get_text_raw_insert(sql));
	}

	public Scheme_DataSrcAS getDataSrcAS() {
		return new Scheme_DataSrcAS(RouterIP, flows, 0, // ???
				dPkts, dOctets, src_as);
	}

	public Scheme_DataDstAS getDataDstAS() {
		return null;
	}

	public Scheme_DataASMatrix getDataASMatrix() {
		return null;
	}

	public Scheme_DataInterface getDataSrcInterface() {
		return new Scheme_DataInterface(RouterIP, flows, 0, // ???
				dPkts, dOctets, input);
	}

	public Scheme_DataInterface getDataDstInterface() {
		return null;
	}

	public Scheme_DataInterfaceMatrix getDataInterfaceMatrix() {
		return null;
	}

	public Scheme_DataPrefix getDataSrcPrefix() {
		return new Scheme_DataPrefix(RouterIP, flows, 0, // ???
				dPkts, dOctets, srcprefix, src_as, input);
	}

	public Scheme_DataPrefix getDataDstPrefix() {
		return null;
	}

	public Scheme_DataPrefixMatrix getDataPrefixMatrix() {
		return null;
	}

	public Scheme_DataProtocol getDataProtocol() {
		return null;
	}
}
