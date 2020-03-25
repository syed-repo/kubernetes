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

 V8 Flow Prefix structure

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
 | 24-27 | dst_prefix | Destination IP address prefix                                  |
 |-------+------------+----------------------------------------------------------------|
 | 28    | src_mask   | Source address prefix mask                                     |
 |-------+------------+----------------------------------------------------------------|
 | 29    | dst_mask   | Destination address prefix mask                                |
 |-------+------------+----------------------------------------------------------------|
 | 30-31 | reserved   | Unused (zero) bytes                                            |
 |-------+------------+----------------------------------------------------------------|
 | 32-33 | src_as     | Source autonomous system number, either origin or peer         |
 |-------+------------+----------------------------------------------------------------|
 | 34-35 | dst_as     | Destination autonomous system number, either origin or peer    |
 |-------+------------+----------------------------------------------------------------|
 | 36-37 | input      | Interface index (ifindex) of input interface                   |
 |-------+------------+----------------------------------------------------------------|
 | 38-39 | output     | Interface index (ifindex) of output interface                  |
 +-------------------------------------------------------------------------------------+
 */

public class V8_FlowPrefix extends V8_Flow {
	long src_prefix, dst_prefix;

	byte src_mask, dst_mask;

	long src_as, dst_as, input, output;

	Prefix srcprefix, dstprefix;

	public V8_FlowPrefix(String RouterIP, byte[] buf, int off)
			throws DoneException {
		super(RouterIP, buf, off);

		src_prefix = Util.to_number(buf, off + 20, 4);
		dst_prefix = Util.to_number(buf, off + 24, 4);
		src_mask = buf[off + 28];
		dst_mask = buf[off + 29];
		src_as = Util.to_number(buf, off + 32, 2);
		dst_as = Util.to_number(buf, off + 34, 2);
		input = Util.to_number(buf, off + 36, 2);
		output = Util.to_number(buf, off + 38, 2);

		srcprefix = new Prefix(src_prefix, src_mask);
		dstprefix = new Prefix(dst_prefix, dst_mask);

		if (Syslog.log.need(Syslog.LOG_INFO)) {
			Syslog.log.syslog(Syslog.LOG_INFO, "      SAS " + src_as + ", "
					+ srcprefix.toString() + " -> DAS " + dst_as + ", "
					+ dstprefix.toString());
			Syslog.log.syslog(Syslog.LOG_INFO, "        bytes=" + dOctets
					+ ", pkts=" + dPkts + ", inIf=" + input + ", outIf="
					+ output + ", " + flows + " flows");
		}

	}

	void fill_specific(PreparedStatement add_raw_stm) throws SQLException {
		add_raw_stm.setString(13, Util.str_addr(src_prefix));
		add_raw_stm.setString(14, Util.str_addr(dst_prefix));
		add_raw_stm.setInt(15, (int) src_mask);
		add_raw_stm.setInt(16, (int) dst_mask);
		add_raw_stm.setInt(17, (int) src_as);
		add_raw_stm.setInt(18, (int) dst_as);
		add_raw_stm.setInt(19, (int) input);
		add_raw_stm.setInt(20, (int) output);
		add_raw_stm.setString(21, Params.getCurrentTime());
	}

	String table_name() {
		return new String("Prefix");
	}

	protected static String add_raw_sql = null;

	String get_text_raw_insert(SQL sql) {
		return add_raw_sql == null ? SQL.resources
				.getAndTrim("SQL.Add.RawV8.Prefix") : add_raw_sql;
	}

	PreparedStatement get_sql_raw_insert(SQL sql) throws SQLException {
		return sql.prepareStatement("Prepare INSERT to V8 Prefix raw table",
				get_text_raw_insert(sql));
	}

	public Scheme_DataSrcAS getDataSrcAS() {
		return null;
	}

	public Scheme_DataDstAS getDataDstAS() {
		return null;
	}

	public Scheme_DataASMatrix getDataASMatrix() {
		return null;
	}

	public Scheme_DataInterface getDataSrcInterface() {
		return null;
	}

	public Scheme_DataInterface getDataDstInterface() {
		return null;
	}

	public Scheme_DataInterfaceMatrix getDataInterfaceMatrix() {
		return new Scheme_DataInterfaceMatrix(RouterIP, flows, 0, // ???
				dPkts, dOctets, input, output);
	}

	public Scheme_DataPrefix getDataSrcPrefix() {
		return null;
	}

	public Scheme_DataPrefix getDataDstPrefix() {
		return null;
	}

	public Scheme_DataPrefixMatrix getDataPrefixMatrix() {
		return new Scheme_DataPrefixMatrix(RouterIP, flows, 0, // ???
				dPkts, dOctets, srcprefix, src_as, input, dstprefix, dst_as,
				output);
	}

	public Scheme_DataProtocol getDataProtocol() {
		return null;
	}
}
