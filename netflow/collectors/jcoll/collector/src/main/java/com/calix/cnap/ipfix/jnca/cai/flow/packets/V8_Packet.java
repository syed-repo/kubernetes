package com.calix.cnap.ipfix.jnca.cai.flow.packets;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

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
import com.calix.cnap.ipfix.jnca.cai.utils.Syslog;
import com.calix.cnap.ipfix.jnca.cai.utils.Util;

/*

 V8 Flow Packet

 +-------------------------------------------------------------------------------------+   
 | Bytes | Contents      | Description                                                 |   
 |-------+---------------+-------------------------------------------------------------|   
 | 0-1   | version       | NetFlow export format version number                        |   
 |-------+---------------+-------------------------------------------------------------|   
 | 2-3   | count         | Number of flows exported in this flow frame (protocol data  |  
 |       |               | unit, or PDU)                                               |   
 |-------+---------------+-------------------------------------------------------------|   
 | 4-7   | SysUptime     | Current time in milliseconds since the export device booted |   
 |-------+---------------+-------------------------------------------------------------|   
 | 8-11  | unix_secs     | Current seconds since 0000 UTC 1970                         |   
 |-------+---------------+-------------------------------------------------------------|   
 | 12-15 | unix_nsecs    | Residual nanoseconds since 0000 UTC 1970                    |   
 |-------+---------------+-------------------------------------------------------------|   
 | 16-19 | flow_sequence | Sequence counter of total flows seen                        |   
 |-------+---------------+-------------------------------------------------------------|   
 | 20    | engine_type   | Type of flow switching engine                               |   
 |-------+---------------+-------------------------------------------------------------|   
 | 21    | engine_id     | ID number of the flow switching engine                      |   
 |-------+---------------+-------------------------------------------------------------|   
 | 22    | aggregation   | Aggregation method being used                               |   
 |-------+---------------+-------------------------------------------------------------|   
 | 23    | agg_version   | Version of the aggregation export                           |   
 |-------+---------------+-------------------------------------------------------------|   
 | 24-27 | reserved      | Unused (zero) bytes                                         |   
 +-------------------------------------------------------------------------------------+

 */

public class V8_Packet implements FlowPacket {
	long count;

	String RouterIP;

	long SysUptime, unix_secs, unix_nsecs, flow_sequence;

	long engine_type, engine_id;

	long aggregation, agg_version;

	Vector flows;

	public final int V8_Flow_RouterAS = 1;

	public final int V8_Flow_RouterProtoPort = 2;

	public final int V8_Flow_RouterSrcPrefix = 3;

	public final int V8_Flow_RouterDstPrefix = 4;

	public final int V8_Flow_RouterPrefix = 5;

	public final int V8_Header_Size = 28;

	public final int V8_Flow_RouterAS_Size = 28;

	public final int V8_Flow_RouterProtoPort_Size = 28;

	public final int V8_Flow_RouterDstPrefix_Size = 32;

	public final int V8_Flow_RouterSrcPrefix_Size = 32;

	public final int V8_Flow_RouterPrefix_Size = 40;

	public V8_Packet(String RouterIP, byte[] buf, int len) throws DoneException {
		if (len < V8_Header_Size)
			throw new DoneException("    * incomplete header *");

		this.RouterIP = RouterIP;
		count = Util.to_number(buf, 2, 2);

		SysUptime = Util.to_number(buf, 4, 4);
		unix_secs = Util.to_number(buf, 8, 4);
		unix_nsecs = Util.to_number(buf, 12, 4);
		flow_sequence = Util.to_number(buf, 16, 4);
		engine_type = buf[20];
		engine_id = buf[21];
		aggregation = buf[22];
		agg_version = buf[23];

		int size = get_v8_flow_size(aggregation);

		if (Syslog.log.need(Syslog.LOG_INFO)) {
			Syslog.log.syslog(Syslog.LOG_INFO, "    uptime: "
					+ Util.uptime(SysUptime / 1000) + ", date: " + unix_secs
					+ "." + unix_nsecs);
			Syslog.log.syslog(Syslog.LOG_INFO, "    sequence: " + flow_sequence
					+ ", count: " + count + ", engine: " + engine_type + "/"
					+ engine_id);
			Syslog.log.syslog(Syslog.LOG_INFO, "    aggregation: "
					+ get_v8_flow_name(aggregation) + "(" + aggregation
					+ "), version: " + agg_version);
		}

		if (agg_version != 2)
			throw new DoneException("      * unknown aggregation version *");

		if (size == 0)
			throw new DoneException("      * unknown aggregation *");

		if (count <= 0 || len != V8_Header_Size + count * size)
			throw new DoneException("      * corrupted packet " + len + "/"
					+ count + "/" + (V8_Header_Size + count * size) + " *");

		flows = new Vector((int) count);

		for (int i = 0, p = V8_Header_Size; i < count; i++, p += size)
			switch ((int) aggregation) {
			case V8_Flow_RouterAS:
				flows.add(new V8_FlowAS(RouterIP, buf, p));
				break;

			case V8_Flow_RouterProtoPort:
				flows.add(new V8_FlowProtoPort(RouterIP, buf, p));
				break;

			case V8_Flow_RouterDstPrefix:
				flows.add(new V8_FlowDstPrefix(RouterIP, buf, p));
				break;

			case V8_Flow_RouterSrcPrefix:
				flows.add(new V8_FlowSrcPrefix(RouterIP, buf, p));
				break;

			case V8_Flow_RouterPrefix:
				flows.add(new V8_FlowPrefix(RouterIP, buf, p));
				break;

			default:
				throw new DoneException(
						"      * BUG * unsupported aggregation *");
			}
	}

	int get_v8_flow_size(long aggregation) {
		switch ((int) aggregation) {
		case V8_Flow_RouterAS:
			return V8_Flow_RouterAS_Size;
		case V8_Flow_RouterProtoPort:
			return V8_Flow_RouterProtoPort_Size;
		case V8_Flow_RouterDstPrefix:
			return V8_Flow_RouterDstPrefix_Size;
		case V8_Flow_RouterSrcPrefix:
			return V8_Flow_RouterSrcPrefix_Size;
		case V8_Flow_RouterPrefix:
			return V8_Flow_RouterPrefix_Size;
		}

		return 0;
	}

	String get_v8_flow_name(long aggregation) {
		String str;

		switch ((int) aggregation) {
		case V8_Flow_RouterAS:
			str = "AS";
			break;
		case V8_Flow_RouterProtoPort:
			str = "ProtoPort";
			break;
		case V8_Flow_RouterDstPrefix:
			str = "DstPrefix";
			break;
		case V8_Flow_RouterSrcPrefix:
			str = "SrcPrefix";
			break;
		case V8_Flow_RouterPrefix:
			str = "Prefix";
			break;
		default:
			str = "Unknown";
		}

		return str;
	}

	@Override
	public Vector getFlows() {
		return flows;
	}


	public void process_raw(SQL sql) {
		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();) {
			V8_Flow f = (V8_Flow) flowenum.nextElement();

			try {
				f.save_raw(SysUptime, unix_secs, unix_nsecs, flow_sequence,
						engine_type, engine_id, f.get_sql_raw_insert(sql));
			} catch (SQLException e) {
				SQL.error_msg("Prepare INSERT to V8 " + f.table_name()
						+ " raw table", e, f.get_text_raw_insert(sql));
			}
		}
	}

	public Vector getSrcASVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();) {
			Scheme_DataSrcAS f = ((V8_Flow) flowenum.nextElement())
					.getDataSrcAS();

			if (f != null)
				v.add(f);
		}

		return v.size() != 0 ? v : null;
	}

	public Vector getDstASVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();) {
			Scheme_DataDstAS f = ((V8_Flow) flowenum.nextElement())
					.getDataDstAS();

			if (f != null)
				v.add(f);
		}

		return v.size() != 0 ? v : null;
	}

	public Vector getASMatrixVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();) {
			Scheme_DataASMatrix f = ((V8_Flow) flowenum.nextElement())
					.getDataASMatrix();

			if (f != null)
				v.add(f);
		}

		return v.size() != 0 ? v : null;
	}

	public Vector getSrcNodeVector() {
		return null;
	}

	public Vector getDstNodeVector() {
		return null;
	}

	public Vector getHostMatrixVector() {
		return null;
	}

	public Vector getSrcInterfaceVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();) {
			Scheme_DataInterface f = ((V8_Flow) flowenum.nextElement())
					.getDataSrcInterface();

			if (f != null)
				v.add(f);
		}

		return v.size() != 0 ? v : null;
	}

	public Vector getDstInterfaceVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();) {
			Scheme_DataInterface f = ((V8_Flow) flowenum.nextElement())
					.getDataDstInterface();

			if (f != null)
				v.add(f);
		}

		return v.size() != 0 ? v : null;
	}

	public Vector getInterfaceMatrixVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();) {
			Scheme_DataInterfaceMatrix f = ((V8_Flow) flowenum.nextElement())
					.getDataInterfaceMatrix();

			if (f != null)
				v.add(f);
		}

		return v.size() != 0 ? v : null;
	}

	public Vector getSrcPrefixVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();) {
			Scheme_DataPrefix f = ((V8_Flow) flowenum.nextElement())
					.getDataSrcPrefix();

			if (f != null)
				v.add(f);
		}

		return v.size() != 0 ? v : null;
	}

	public Vector getDstPrefixVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();) {
			Scheme_DataPrefix f = ((V8_Flow) flowenum.nextElement())
					.getDataDstPrefix();

			if (f != null)
				v.add(f);
		}

		return v.size() != 0 ? v : null;
	}

	public Vector getPrefixMatrixVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();) {
			Scheme_DataPrefixMatrix f = ((V8_Flow) flowenum.nextElement())
					.getDataPrefixMatrix();

			if (f != null)
				v.add(f);
		}

		return v.size() != 0 ? v : null;
	}

	public Vector getProtocolVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();) {
			Scheme_DataProtocol f = ((V8_Flow) flowenum.nextElement())
					.getDataProtocol();

			if (f != null)
				v.add(f);
		}

		return v.size() != 0 ? v : null;
	}
}
