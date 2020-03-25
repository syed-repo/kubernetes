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

 V8 Flow ProtoPort structure

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
 | 20    | prot     | IP protocol type (for example, TCP = 6; UDP = 17)                |
 |-------+----------+------------------------------------------------------------------|
 | 21    | pad      | Unused (zero) bytes                                              |
 |-------+----------+------------------------------------------------------------------|
 | 22-23 | reserved | Unused (zero) bytes                                              |
 |-------+----------+------------------------------------------------------------------|
 | 24-25 | srcport  | TCP/UDP source port number                                       |
 |-------+----------+------------------------------------------------------------------|
 | 26-27 | dstport  | TCP/UDP destination port number                                  |
 +-------------------------------------------------------------------------------------+
 */

public class V8_FlowProtoPort extends V8_Flow {
	byte prot;

	long srcport, dstport;

	public V8_FlowProtoPort(String RouterIP, byte[] buf, int off)
			throws DoneException {
		super(RouterIP, buf, off);

		prot = buf[off + 20];
		srcport = Util.to_number(buf, off + 24, 2);
		dstport = Util.to_number(buf, off + 26, 2);

		if (Syslog.log.need(Syslog.LOG_INFO)) {
			Syslog.log.syslog(Syslog.LOG_INFO, "      SP " + srcport
					+ " -> DP " + dstport + ", " + flows + " flows");
			Syslog.log.syslog(Syslog.LOG_INFO, "        bytes=" + dOctets
					+ ", pkts=" + dPkts + ", proto=" + prot);
		}
	}

	public Long getSrcPort() {
		return new Long(srcport);
	}

	public Long getDstPort() {
		return new Long(dstport);
	}

	public Long getProto() {
		return new Long(prot);
	}

	void fill_specific(PreparedStatement add_raw_stm) throws SQLException {
		add_raw_stm.setInt(13, (int) prot);
		add_raw_stm.setInt(14, (int) srcport);
		add_raw_stm.setInt(15, (int) dstport);
		add_raw_stm.setString(16, Params.getCurrentTime());
	}

	String table_name() {
		return new String("ProtoPort");
	}

	protected static String add_raw_sql = null;

	String get_text_raw_insert(SQL sql) {
		return add_raw_sql == null ? SQL.resources
				.getAndTrim("SQL.Add.RawV8.ProtoPort") : add_raw_sql;
	}

	PreparedStatement get_sql_raw_insert(SQL sql) throws SQLException {
		return sql.prepareStatement("Prepare INSERT to V8 ProtoPort raw table",
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
