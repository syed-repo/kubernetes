package com.calix.cnap.ipfix.jnca.cai.flow.packets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Vector;

import com.calix.cnap.ipfix.jnca.cai.flow.packets.v9.TemplateManager;
import com.calix.cnap.ipfix.jnca.cai.sql.SQL;
import com.calix.cnap.ipfix.jnca.cai.utils.DoneException;
import com.calix.cnap.ipfix.jnca.cai.utils.Params;
import com.calix.cnap.ipfix.jnca.cai.utils.Syslog;
import com.calix.cnap.ipfix.jnca.cai.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*

 V5 Flow Packet UDP���Ľ������ڲ��������V5_Flow��������

 *-------*---------------*------------------------------------------------------*
 | Bytes | Contents      | Description                                          |
 *-------*---------------*------------------------------------------------------*
 | 0-1   | version       | NetFlow export format version number                 |
 *-------*---------------*------------------------------------------------------*
 | 2-3   | count         | Number of flows exported in this packet (1-30)       |
 *-------*---------------*------------------------------------------------------*
 | 4-7   | SysUptime     | Current time in milliseconds since the export device |
 |       |               | booted                                               |
 *-------*---------------*------------------------------------------------------*
 | 8-11  | unix_secs     | Current count of seconds since 0000 UTC 1970         |
 *-------*---------------*------------------------------------------------------*
 | 12-15 | unix_nsecs    | Residual nanoseconds since 0000 UTC 1970             |
 *-------*---------------*------------------------------------------------------*
 | 16-19 | flow_sequence | Sequence counter of total flows seen                 |
 *-------*---------------*------------------------------------------------------*
 | 20    | engine_type   | Type of flow-switching engine                        |
 *-------*---------------*-------------------------------------------Source ID--*
 | 21    | engine_id     | Slot number of the flow-switching engine             |
 *-------*---------------*------------------------------------------------------*
 | 22-23 | reserved      | Unused (zero) bytes                                  |
 *-------*---------------*------------------------------------------------------*

 */

public class V5_Packet implements FlowPacket {
	long count;
	private final Logger log = LoggerFactory.getLogger(getClass());
	String RouterIP;

	long SysUptime, unix_secs, unix_nsecs, flow_sequence;

	long engine_type, engine_id;

	Vector flows;

	public static final int V5_Header_Size = 24;

	public static final int V5_Flow_Size = 48;

	/**
	 * ����UDP��ͷ�������е�flows�����洢���ڴ�Vector��
	 *
	 * @param RouterIP
	 * @param buf
	 * @param len
	 * @throws DoneException
	 */
	public V5_Packet(String RouterIP, byte[] buf, int len, long time) throws DoneException {
		if (false){//(Params.DEBUG) {
			// ����ʵ��
			File tmpFile = new File(Params.path + File.separator + "cache.tmp");
			if (tmpFile.exists()) {
				try {
					ObjectInputStream fIn = new ObjectInputStream(
							new FileInputStream(tmpFile));
					//System.out.println("ֱ�Ӵ�" + fIn + "������");
					try {
						buf = (byte[]) fIn.readObject();
						len = ((Integer) fIn.readObject()).intValue();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					fIn.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					ObjectOutputStream fOut;
					fOut = new ObjectOutputStream(new FileOutputStream(tmpFile));
					fOut.writeObject(buf);
					fOut.writeObject(new Integer(len));
					fOut.flush();
					fOut.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			// ����ʵ��
		}
		if (len < V5_Header_Size)
			throw new DoneException("    * incomplete header *");

		this.RouterIP = RouterIP;
		count = Util.to_number(buf, 2, 2);

		if (count <= 0 || len != V5_Header_Size + count * V5_Flow_Size)
			throw new DoneException("    * corrupted packet " + len + "/"
					+ count + "/" + (V5_Header_Size + count * V5_Flow_Size)
					+ " *");

		SysUptime = Util.to_number(buf, 4, 4);
		unix_secs = Util.to_number(buf, 8, 4);
		unix_nsecs = Util.to_number(buf, 12, 4);
		flow_sequence = Util.to_number(buf, 16, 4);
		engine_type = buf[20];
		engine_id = buf[21];

		if (Syslog.log.need(Syslog.LOG_INFO)) {
			Syslog.log.syslog(Syslog.LOG_INFO, "    uptime: "
					+ Util.uptime(SysUptime / 1000) + ", date: " + unix_secs
					+ "." + unix_nsecs);
			Syslog.log.syslog(Syslog.LOG_INFO, "    sequence: " + flow_sequence
					+ ", count: " + count + ", engine: " + engine_type + "/"
					+ engine_id);
		}

		flows = new Vector((int) count);
		log.debug("Received date record of version V5");
		for (int i = 0, p = V5_Header_Size; i < count; i++, p += V5_Flow_Size) {
			V5_Flow f;
			try {
				f = new V5_Flow(RouterIP, buf, p);
				if (Params.DEBUG) {
                                    if (!f.equals(new V5_Flow(RouterIP, buf, p, TemplateManager
						.getTemplateManager().getV5Template()))) {
						System.err.println("ERROR:Data inconsistency with different algorithm");
					}
				}
				// ��ַû�б��ų�
				if (f.srcaddr != null && f.dstaddr != null) {
					flows.add(f);

					log.debug("Flow from IP: {}, flow-seq: {}, processed: {}", RouterIP, flow_sequence, f);

					// TODO: fix lookup by router IP
					/*
					FlowService ipfixService = DefaultServiceDirectory.getService(FlowService.class);
					IpfixDataCallback callback = ipfixService.getIpfixDataCallback(ipAddress);
					NetFlowData netFlowData = new NetFlowData(new Timestamp(time * 1000), null,
							f.dOctets, null, null, f.dPkts, f.srcaddr, (int)f.srcport, f.dstaddr, (int)f.dstport,
                            f.flowTraceSeqNo);

					if (callback == null) {
						// get random callback for purposes of PoC
						callback = ipfixService.getIpfixDataCallback();
					}
					if (callback != null) {
						callback.onPmDataReceipt(netFlowData, ipAddress);
					} else {
						log.warn("No callbacks are available (IP address = {}. Skipping packet flow {}", ipAddress, f);
					} */
				} else {
					if (Params.DEBUG) {
						System.err
								.println(f.srcaddr + "��" + f.dstaddr + "������ ");
					}
				}
			} catch (DoneException e) {
				if (Params.DEBUG) {
					e.printStackTrace();
				}
				if (e.getMessage() != null && (!e.getMessage().equals(""))) {
					System.err.println(e.getMessage());
				}
			}
		}
	}

	protected static String add_raw_sql = null;

	@Override
	public Vector getFlows() {
		return flows;
	}

	@Override

	public void process_raw(SQL sql) {
		if (add_raw_sql == null) {
			add_raw_sql = SQL.resources.getAndTrim("SQL.Add.RawV5");
		}

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();)
			// if (((V5_Flow) flowenum.nextElement())�Ϸ����û����
			((V5_Flow) flowenum.nextElement()).save_raw(SysUptime, unix_secs,
					unix_nsecs, flow_sequence, engine_type, engine_id, sql
							.prepareStatement("Prepare INSERT to V5 raw table",
									add_raw_sql));
	}

	public Vector getSrcASVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();)
			v.add(((V5_Flow) flowenum.nextElement()).getDataSrcAS());

		return v;
	}

	public Vector getDstASVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();)
			v.add(((V5_Flow) flowenum.nextElement()).getDataDstAS());

		return v;
	}

	public Vector getASMatrixVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();)
			v.add(((V5_Flow) flowenum.nextElement()).getDataASMatrix());

		return v;
	}

	public Vector getSrcNodeVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();)
			v.add(((V5_Flow) flowenum.nextElement()).getDataSrcNode());

		return v;
	}

	public Vector getDstNodeVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();)
			v.add(((V5_Flow) flowenum.nextElement()).getDataDstNode());

		return v;
	}

	public Vector getHostMatrixVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();)
			v.add(((V5_Flow) flowenum.nextElement()).getDataHostMatrix());

		return v;
	}

	public Vector getSrcInterfaceVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();)
			v.add(((V5_Flow) flowenum.nextElement()).getDataSrcInterface());

		return v;
	}

	public Vector getDstInterfaceVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();)
			v.add(((V5_Flow) flowenum.nextElement()).getDataDstInterface());

		return v;
	}

	public Vector getInterfaceMatrixVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();)
			v.add(((V5_Flow) flowenum.nextElement()).getDataInterfaceMatrix());

		return v;
	}

	public Vector getSrcPrefixVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();)
			v.add(((V5_Flow) flowenum.nextElement()).getDataSrcPrefix());

		return v;
	}

	public Vector getDstPrefixVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();)
			v.add(((V5_Flow) flowenum.nextElement()).getDataDstPrefix());

		return v;
	}

	public Vector getPrefixMatrixVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();)
			v.add(((V5_Flow) flowenum.nextElement()).getDataPrefixMatrix());

		return v;
	}

	public Vector getProtocolVector() {
		Vector v = new Vector((int) count, (int) count);

		for (Enumeration flowenum = flows.elements(); flowenum
				.hasMoreElements();)
			v.add(((V5_Flow) flowenum.nextElement()).getDataProtocol());

		return v;
	}
}
