package com.calix.cnap.ipfix.jnca.cai.flow.packets;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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

 V8 Flow AS structure

 +-------------------------------------------------------------------------------------+
 | Bytes | Contents | Description                                                      |
 |-------+----------+------------------------------------------------------------------|
 | 0-3   | flows    | Number of flows                                                  |
 |-------+----------+------------------------------------------------------------------|
 | 4-7   | dPkts    | Packets in the flow                                              |
 |-------+----------+------------------------------------------------------------------|
 | 8-11  | dOctets  | Total number of Layer 3 bytes in the packets of the flow         |
 |-------+----------+------------------------------------------------------------------|
 | 12-15 | First    | SysUptime, in seconds, at start of flow                          |
 |-------+----------+------------------------------------------------------------------|
 | 16-19 | Last     | SysUptime, in seconds, at the time the last packet of the flow   |
 |       |          | was received                                                     |
 |-------+----------+------------------------------------------------------------------|
 | 20-21 | src_as   | Source autonomous system number, either origin or peer           |
 |-------+----------+------------------------------------------------------------------|
 | 22-23 | dst_as   | Destination autonomous system number, either origin or peer      |
 |-------+----------+------------------------------------------------------------------|
 | 24-25 | input    | Interface index (ifindex) of input interface                     |
 |-------+----------+------------------------------------------------------------------|
 | 26-27 | output   | Interface index (ifindex) of output interface                    |
 +-------------------------------------------------------------------------------------+

 */

public class V8_FlowAS extends V8_Flow {
	long src_as, dst_as;

	long input, output;

	public V8_FlowAS(String RouterIP, byte[] buf, int off) throws DoneException {
		super(RouterIP, buf, off);

		src_as = Util.to_number(buf, off + 20, 2);
		dst_as = Util.to_number(buf, off + 22, 2);
		input = Util.to_number(buf, off + 24, 2);
		output = Util.to_number(buf, off + 26, 2);

		if (Syslog.log.need(Syslog.LOG_INFO)) {
			Syslog.log.syslog(Syslog.LOG_INFO, "      AS " + src_as + " -> AS "
					+ dst_as + ", " + flows + " flows");

			Syslog.log.syslog(Syslog.LOG_INFO, "        bytes=" + dOctets
					+ ", pkts=" + dPkts + ", inIf=" + input + ", outIf="
					+ output);
		}
	}

	void fill_specific(PreparedStatement add_raw_stm) throws SQLException {
		add_raw_stm.setInt(13, (int) src_as);
		add_raw_stm.setInt(14, (int) dst_as);
		add_raw_stm.setInt(15, (int) input);
		add_raw_stm.setInt(16, (int) output);
		add_raw_stm.setString(17, Params.getCurrentTime());
	}

	String table_name() {
		return new String("AS");
	}

	protected static String add_raw_sql = null;

	String get_text_raw_insert(SQL sql) {
		return add_raw_sql == null ? SQL.resources
				.getAndTrim("SQL.Add.RawV8.AS") : add_raw_sql;
	}

	PreparedStatement get_sql_raw_insert(SQL sql) throws SQLException {
		return sql.prepareStatement("Prepare INSERT to V8 AS raw table",
				get_text_raw_insert(sql));
	}

	public Scheme_DataSrcAS getDataSrcAS() {
		return null;
	}

	public Scheme_DataDstAS getDataDstAS() {
		return null;
	}

	public Scheme_DataASMatrix getDataASMatrix() {
		return new Scheme_DataASMatrix(RouterIP, flows, 0, // ???
				dPkts, dOctets, src_as, dst_as);
	}

	public Scheme_DataInterface getDataSrcInterface() {
		return null;
	}

	public Scheme_DataInterface getDataDstInterface() {
		return null;
	}

	public Scheme_DataInterfaceMatrix getDataInterfaceMatrix() {
		return null;
	}

	public Scheme_DataPrefix getDataSrcPrefix() {
		return null;
	}

	public Scheme_DataPrefix getDataDstPrefix() {
		return null;
	}

	public Scheme_DataPrefixMatrix getDataPrefixMatrix() {
		return null;
	}

	public Scheme_DataProtocol getDataProtocol() {
		return new Scheme_DataProtocol(RouterIP, 1, 0, dPkts, dOctets, this);
	}
}
