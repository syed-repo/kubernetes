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

 V8 Flow DstPrefix structure

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
 | 20-23 | dst_prefix | Destination IP address prefix                                  |
 |-------+------------+----------------------------------------------------------------|
 | 24    | dst_mask   | Destination address prefix mask                                |
 |-------+------------+----------------------------------------------------------------|
 | 25    | pad        | Unused (zero) bytes                                            |
 |-------+------------+----------------------------------------------------------------|
 | 26-27 | dst_as     | Destination autonomous system number, either origin or peer    |
 |-------+------------+----------------------------------------------------------------|
 | 28-29 | output     | Interface index (ifindex) of output interface                  |
 |-------+------------+----------------------------------------------------------------|
 | 30-31 | reserved   | Unused (zero) bytes                                            |
 +-------------------------------------------------------------------------------------+
 */

public class V8_FlowDstPrefix extends V8_Flow {
	long dst_prefix;

	byte dst_mask;

	long dst_as, output;

	Prefix dstprefix;

	public V8_FlowDstPrefix(String RouterIP, byte[] buf, int off)
			throws DoneException {
		super(RouterIP, buf, off);

		dst_prefix = Util.to_number(buf, off + 20, 4);
		dst_mask = buf[off + 24];
		dst_as = Util.to_number(buf, off + 26, 2);
		output = Util.to_number(buf, off + 28, 2);

		dstprefix = new Prefix(dst_prefix, dst_mask);

		if (Syslog.log.need(Syslog.LOG_INFO)) {
			Syslog.log.syslog(Syslog.LOG_INFO, "      -> AS " + dst_as + " "
					+ dstprefix.toString() + ", " + flows + " flows");
			Syslog.log.syslog(Syslog.LOG_INFO, "        bytes=" + dOctets
					+ ", pkts=" + dPkts + ", outIf=" + output);
		}
	}

	public Long getDstAS() {
		return new Long(dst_as);
	}

	public Long getOutIf() {
		return new Long(output);
	}

	public Prefix getDstPrefix() {
		return dstprefix;
	}

	void fill_specific(PreparedStatement add_raw_stm) throws SQLException {
		add_raw_stm.setString(13, Util.str_addr(dst_prefix));
		add_raw_stm.setInt(14, (int) dst_mask);
		add_raw_stm.setInt(15, (int) dst_as);
		add_raw_stm.setInt(16, (int) output);
		add_raw_stm.setString(17, Params.getCurrentTime());
	}

	String table_name() {
		return new String("DstPrefix");
	}

	protected static String add_raw_sql = null;

	String get_text_raw_insert(SQL sql) {
		return add_raw_sql == null ? SQL.resources
				.getAndTrim("SQL.Add.RawV8.DstPrefix") : add_raw_sql;
	}

	PreparedStatement get_sql_raw_insert(SQL sql) throws SQLException {
		return sql.prepareStatement("Prepare INSERT to V8 DstPrefix raw table",
				get_text_raw_insert(sql));
	}

	public Scheme_DataSrcAS getDataSrcAS() {
		return null;
	}

	public Scheme_DataDstAS getDataDstAS() {
		return new Scheme_DataDstAS(RouterIP, flows, 0, // ???
				dPkts, dOctets, dst_as);
	}

	public Scheme_DataASMatrix getDataASMatrix() {
		return null;
	}

	public Scheme_DataInterface getDataSrcInterface() {
		return null;
	}

	public Scheme_DataInterface getDataDstInterface() {
		return new Scheme_DataInterface(RouterIP, flows, 0, // ???
				dPkts, dOctets, output);
	}

	public Scheme_DataInterfaceMatrix getDataInterfaceMatrix() {
		return null;
	}

	public Scheme_DataPrefix getDataSrcPrefix() {
		return null;
	}

	public Scheme_DataPrefix getDataDstPrefix() {
		return new Scheme_DataPrefix(RouterIP, flows, 0, // ???
				dPkts, dOctets, dstprefix, dst_as, output);
	}

	public Scheme_DataPrefixMatrix getDataPrefixMatrix() {
		return null;
	}

	public Scheme_DataProtocol getDataProtocol() {
		return null;
	}
}
