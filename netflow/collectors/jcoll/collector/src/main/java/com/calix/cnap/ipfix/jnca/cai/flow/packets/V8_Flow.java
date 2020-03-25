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
import com.calix.cnap.ipfix.jnca.cai.utils.Util;

public abstract class V8_Flow extends Flow {
	protected long flows;

	protected long dPkts, dOctets, First, Last;

	protected String RouterIP;

	public V8_Flow(String RouterIP, byte[] buf, int off) throws DoneException {
		this.RouterIP = RouterIP;

		flows = Util.to_number(buf, off + 0, 4);
		dPkts = Util.to_number(buf, off + 4, 4);
		dOctets = Util.to_number(buf, off + 8, 4);
		First = Util.to_number(buf, off + 12, 4);
		Last = Util.to_number(buf, off + 16, 4);
	}

	public void save_raw(long SysUptime, long unix_secs, long unix_nsecs,
			long flow_sequence, long engine_type, long engine_id,
			PreparedStatement add_raw_stm) {
		try {
			add_raw_stm.setString(1, RouterIP);
			add_raw_stm.setLong(2, SysUptime);
			add_raw_stm.setLong(3, unix_secs);
			add_raw_stm.setLong(4, unix_nsecs);
			add_raw_stm.setLong(5, flow_sequence);
			add_raw_stm.setInt(6, (int) engine_type);
			add_raw_stm.setInt(7, (int) engine_id);
			add_raw_stm.setLong(8, flows);
			add_raw_stm.setLong(9, dPkts);
			add_raw_stm.setLong(10, dOctets);
			add_raw_stm.setLong(11, First);
			add_raw_stm.setLong(12, Last);
			fill_specific(add_raw_stm);
			add_raw_stm.executeUpdate();
		} catch (SQLException e) {
			SQL.error_msg("INSERT to V8 " + table_name() + " raw table", e,
					null);
		}
	}

	abstract String table_name();

	abstract PreparedStatement get_sql_raw_insert(SQL sql) throws SQLException;

	abstract String get_text_raw_insert(SQL sql);

	abstract void fill_specific(PreparedStatement add_raw_stm)
			throws SQLException;

	public abstract Scheme_DataSrcAS getDataSrcAS();

	public abstract Scheme_DataDstAS getDataDstAS();

	public abstract Scheme_DataASMatrix getDataASMatrix();

	public abstract Scheme_DataInterface getDataSrcInterface();

	public abstract Scheme_DataInterface getDataDstInterface();

	public abstract Scheme_DataInterfaceMatrix getDataInterfaceMatrix();

	public abstract Scheme_DataPrefix getDataSrcPrefix();

	public abstract Scheme_DataPrefix getDataDstPrefix();

	public abstract Scheme_DataPrefixMatrix getDataPrefixMatrix();

	public abstract Scheme_DataProtocol getDataProtocol();
}
