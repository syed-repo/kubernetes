package com.calix.cnap.ipfix.jnca.cai.flow.packets;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.calix.cnap.ipfix.jnca.cai.flow.struct.Address;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataHostMatrix;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataInterface;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataInterfaceMatrix;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataNode;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataProtocol;
import com.calix.cnap.ipfix.jnca.cai.sql.SQL;
import com.calix.cnap.ipfix.jnca.cai.utils.DoneException;
import com.calix.cnap.ipfix.jnca.cai.utils.Params;
import com.calix.cnap.ipfix.jnca.cai.utils.Syslog;
import com.calix.cnap.ipfix.jnca.cai.utils.Util;

/*

 V1 Flow structure

 *-------*------------------*-------------------------------------------------------*
 | Bytes | Contents         | Description                                           |
 *-------*------------------*-------------------------------------------------------*
 | 0-3   | srcaddr          | Source IP address                                     |
 *-------*------------------*-------------------------------------------------------*
 | 4-7   | dstaddr          | Destination IP address                                |
 *-------*------------------*-------------------------------------------------------*
 | 8-11  | nexthop          | IP address of next hop router                         |
 *-------*------------------*-------------------------------------------------------*
 | 12-13 | input            | Interface index (ifindex) of input interface          |
 *-------*------------------*-------------------------------------------------------*
 | 14-15 | output           | Interface index (ifindex) of output interface         |
 *-------*------------------*-------------------------------------------------------*
 | 16-19 | dPkts            | Packets in the flow                                   |
 *-------*------------------*-------------------------------------------------------*
 | 20-23 | dOctets          | Total number of Layer 3 bytes in the packets of the   |
 |       |                  | flow                                                  |
 *-------*------------------*-------------------------------------------------------*
 | 24-27 | First            | SysUptime at start of flow                            |
 *-------*------------------*-------------------------------------------------------*
 | 28-31 | Last             | SysUptime at the time the last packet of the flow was |
 |       |                  | received                                              |
 *-------*------------------*-------------------------------------------------------*
 | 32-33 | srcport          | TCP/UDP source port number or equivalent              |
 *-------*------------------*-------------------------------------------------------*
 | 34-35 | dstport          | TCP/UDP destination port number or equivalent         |
 *-------*------------------*-------------------------------------------------------*
 | 36-37 | pad1             | Unused (zero) bytes                                   |
 *-------*------------------*-------------------------------------------------------*
 | 38    | prot             | IP protocol type (for example, TCP = 6; UDP = 17)     |
 *-------*------------------*-------------------------------------------------------*
 | 39    | tos              | IP type of service (ToS)                              |
 *-------*------------------*-------------------------------------------------------*
 | 40    | flags            | Cumulative OR of TCP flags                            |
 *-------*------------------*-------------------------------------------------------*
 | 41-43 | pad1, pad2, pad3 | Unused (zero) bytes                                   |
 *-------*------------------*-------------------------------------------------------*
 | 44-48 | reserved         | Unused (zero) bytes                                   |     
 *-------*------------------*-------------------------------------------------------*

 */

public class V1_Flow extends Flow {
	String srcaddr, dstaddr, nexthop;

	long input, output;

	long dPkts, dOctets, First, Last;

	long srcport, dstport;

	byte prot, tos, tcp_flags;

	String RouterIP;

	long src_addr, dst_addr, next_hop;

	public V1_Flow(String RouterIP, byte[] buf, int off) throws DoneException {
		this.RouterIP = RouterIP;

		srcaddr = Util.str_addr(src_addr = Util.to_number(buf, off + 0, 4));
		dstaddr = Util.str_addr(dst_addr = Util.to_number(buf, off + 4, 4));
		nexthop = Util.str_addr(next_hop = Util.to_number(buf, off + 8, 4));
		input = Util.to_number(buf, off + 12, 2);
		output = Util.to_number(buf, off + 14, 2);
		dPkts = Util.to_number(buf, off + 16, 4);
		dOctets = Util.to_number(buf, off + 20, 4);
		First = Util.to_number(buf, off + 24, 4);
		Last = Util.to_number(buf, off + 28, 4);
		srcport = Util.to_number(buf, off + 32, 2);
		dstport = Util.to_number(buf, off + 34, 2);
		prot = buf[off + 38];
		tos = buf[off + 39];
		tcp_flags = buf[off + 40];

		if (Syslog.log.need(Syslog.LOG_INFO)) {
			Syslog.log.syslog(Syslog.LOG_INFO, "      " + srcaddr + ":"
					+ srcport + " -> " + dstaddr + ":" + dstport + " via "
					+ nexthop);
			Syslog.log.syslog(Syslog.LOG_INFO, "        bytes=" + dOctets
					+ ", pkts=" + dPkts + ", proto=" + prot + ", TOS=" + tos
					+ ", TCPF=" + tcp_flags);
			Syslog.log.syslog(Syslog.LOG_INFO, "        inIf=" + input
					+ ", outIf=" + output);
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

	public Long getTOS() {
		return new Long(tos);
	}

	public Address getSrcAddr() {
		return new Address(src_addr);
	}

	public Address getDstAddr() {
		return new Address(dst_addr);
	}

	public Address getNextHop() {
		return new Address(next_hop);
	}

	public Long getInIf() {
		return new Long(input);
	}

	public Long getOutIf() {
		return new Long(output);
	}

	public void save_raw(long SysUptime, long unix_secs, long unix_nsecs,
			PreparedStatement add_raw_stm) {
		try {
			add_raw_stm.setString(1, RouterIP);
			add_raw_stm.setLong(2, SysUptime);
			add_raw_stm.setLong(3, unix_secs);
			add_raw_stm.setLong(4, unix_nsecs);
			add_raw_stm.setString(5, srcaddr);
			add_raw_stm.setString(6, dstaddr);
			add_raw_stm.setString(7, nexthop);
			add_raw_stm.setInt(8, (int) input);
			add_raw_stm.setInt(9, (int) output);
			add_raw_stm.setLong(10, dPkts);
			add_raw_stm.setLong(11, dOctets);
			add_raw_stm.setLong(12, First);
			add_raw_stm.setLong(13, Last);
			add_raw_stm.setInt(14, (int) srcport);
			add_raw_stm.setInt(15, (int) dstport);
			add_raw_stm.setInt(16, (int) prot);
			add_raw_stm.setInt(17, (int) tos);
			add_raw_stm.setInt(18, (int) tcp_flags);
			add_raw_stm.setString(19, Params.getCurrentTime());
			add_raw_stm.executeUpdate();
		} catch (SQLException e) {
			SQL.error_msg("INSERT to V1 raw table", e, null);
		}
	}

	public Scheme_DataNode getDataSrcNode() {
		return new Scheme_DataNode(RouterIP, 1, 0, dPkts, dOctets, srcaddr);
	}

	public Scheme_DataNode getDataDstNode() {
		return new Scheme_DataNode(RouterIP, 1, 0, dPkts, dOctets, dstaddr);
	}

	public Scheme_DataHostMatrix getDataHostMatrix() {
		return new Scheme_DataHostMatrix(RouterIP, 1, 0, dPkts, dOctets,
				srcaddr, dstaddr);
	}

	public Scheme_DataInterface getDataSrcInterface() {
		return new Scheme_DataInterface(RouterIP, 1, 0, dPkts, dOctets, input);
	}

	public Scheme_DataInterface getDataDstInterface() {
		return new Scheme_DataInterface(RouterIP, 1, 0, dPkts, dOctets, output);
	}

	public Scheme_DataInterfaceMatrix getDataInterfaceMatrix() {
		return new Scheme_DataInterfaceMatrix(RouterIP, 1, 0, dPkts, dOctets,
				input, output);
	}

	public Scheme_DataProtocol getDataProtocol() {
		return new Scheme_DataProtocol(RouterIP, 1, 0, dPkts, dOctets, this);
	}
}
