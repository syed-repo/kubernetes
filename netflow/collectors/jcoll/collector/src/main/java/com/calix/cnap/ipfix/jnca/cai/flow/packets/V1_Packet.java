package com.calix.cnap.ipfix.jnca.cai.flow.packets;

import java.util.Enumeration;
import java.util.Vector;

import com.calix.cnap.ipfix.jnca.cai.sql.SQL;
import com.calix.cnap.ipfix.jnca.cai.utils.DoneException;
import com.calix.cnap.ipfix.jnca.cai.utils.Syslog;
import com.calix.cnap.ipfix.jnca.cai.utils.Util;

/*

 V1 Flow Packet

 *-------*------------*-------------------------------------------------------------*
 | Bytes | Contents   | Description                                                 |
 *-------*------------*-------------------------------------------------------------*
 | 0-1   | version    | NetFlow export format version number                        |
 *-------*------------*-------------------------------------------------------------*
 | 2-3   | count      | Number of flows exported in this packet (1-24)              |
 *-------*------------*-------------------------------------------------------------*
 | 4-7   | SysUptime  | Current time in milliseconds since the export device booted |
 *-------*------------*-------------------------------------------------------------*
 | 8-11  | unix_secs  | Current count of seconds since 0000 UTC 1970                |
 *-------*------------*-------------------------------------------------------------*
 | 12-16 | unix_nsecs | Residual nanoseconds since 0000 UTC 1970                    |
 *-------*------------*-------------------------------------------------------------*

 */

public class V1_Packet implements FlowPacket {
	long count;

	String RouterIP;

	long SysUptime, unix_secs, unix_nsecs;

	Vector flows;

	public final int V1_Header_Size = 16;

	public final int V1_Flow_Size = 48;

	public V1_Packet(String RouterIP, byte[] buf, int len) throws DoneException {
		if (len < V1_Header_Size)
			throw new DoneException("    * incomplete header *");

		this.RouterIP = RouterIP;
		count = Util.to_number(buf, 2, 2);

		if (count <= 0 || len != V1_Header_Size + count * V1_Flow_Size)
			throw new DoneException("    * corrupted packet " + len + "/"
					+ count + "/" + (V1_Header_Size + count * V1_Flow_Size)
					+ " *");

		SysUptime = Util.to_number(buf, 4, 4);
		unix_secs = Util.to_number(buf, 8, 4);
		unix_nsecs = Util.to_number(buf, 12, 4);

		if (Syslog.log.need(Syslog.LOG_INFO))
			Syslog.log.syslog(Syslog.LOG_INFO, "    count: " + count
					+ ", uptime: " + Util.uptime(SysUptime / 1000) + ", date: "
					+ unix_secs + "." + unix_nsecs);

		flows = new Vector((int) count);

		for (int i = 0, p = V1_Header_Size; i < count; i++, p += V1_Flow_Size)
			flows.add(new V1_Flow(RouterIP, buf, p));
	}

	protected static String add_raw_sql = null;

	@Override
	public Vector getFlows() {
		return flows;
	}

	@Override


	public void process_raw(SQL sql) {
		if (add_raw_sql == null) {
			add_raw_sql = SQL.resources.getAndTrim("SQL.Add.RawV1");
		}

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();)
			((V1_Flow) flowenum.nextElement()).save_raw(SysUptime, unix_secs,
					unix_nsecs, sql.prepareStatement(
							"Prepare INSERT to V1 raw table", add_raw_sql));
	}

	public Vector getSrcASVector() {
		return null;
	}

	public Vector getDstASVector() {
		return null;
	}

	public Vector getASMatrixVector() {
		return null;
	}

	public Vector getSrcNodeVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();)
			v.add(((V1_Flow) flowenum.nextElement()).getDataSrcNode());

		return v;
	}

	public Vector getDstNodeVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();)
			v.add(((V1_Flow) flowenum.nextElement()).getDataDstNode());

		return v;
	}

	public Vector getHostMatrixVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();)
			v.add(((V1_Flow) flowenum.nextElement()).getDataHostMatrix());

		return v;
	}

	public Vector getSrcInterfaceVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();)
			v.add(((V1_Flow) flowenum.nextElement()).getDataSrcInterface());

		return v;
	}

	public Vector getDstInterfaceVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();)
			v.add(((V1_Flow) flowenum.nextElement()).getDataDstInterface());

		return v;
	}

	public Vector getInterfaceMatrixVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();)
			v.add(((V1_Flow) flowenum.nextElement()).getDataInterfaceMatrix());

		return v;
	}

	public Vector getSrcPrefixVector() {
		return null;
	}

	public Vector getDstPrefixVector() {
		return null;
	}

	public Vector getPrefixMatrixVector() {
		return null;
	}

	public Vector getProtocolVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();)
			v.add(((V1_Flow) flowenum.nextElement()).getDataProtocol());

		return v;
	}
}
