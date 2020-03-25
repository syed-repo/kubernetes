package com.calix.cnap.ipfix.jnca.cai.flow.packets;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.calix.cnap.ipfix.jnca.cai.flow.struct.Address;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataASMatrix;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataDstAS;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataHostMatrix;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataInterface;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataInterfaceMatrix;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataNode;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataProtocol;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataSrcAS;
import com.calix.cnap.ipfix.jnca.cai.sql.SQL;
import com.calix.cnap.ipfix.jnca.cai.utils.DoneException;
import com.calix.cnap.ipfix.jnca.cai.utils.Params;
import com.calix.cnap.ipfix.jnca.cai.utils.Syslog;
import com.calix.cnap.ipfix.jnca.cai.utils.Util;

/*

 V7 Flow structure

 +------------------------------------------------------------------------------------+   
 | Bytes | Contents  | Description                                                    |   
 |-------+-----------+----------------------------------------------------------------|   
 | 0-3   | srcaddr   | Source IP address; in case of destination-only flows, set to   |   
 |       |           | zero.                                                          |   
 |-------+-----------+----------------------------------------------------------------|   
 | 4-7   | dstaddr   | Destination IP address.                                        |   
 |-------+-----------+----------------------------------------------------------------|   
 | 8-11  | nexthop   | Next hop router; always set to zero.                           |   
 |-------+-----------+----------------------------------------------------------------|   
 | 12-13 | input     | Interface index (ifindex) of input interface;                  |
 |       |           | always set to zero.                                            |
 |-------+-----------+----------------------------------------------------------------|   
 | 14-15 | output    | Interface index (ifindex) of output interface                  |
 |-------+-----------+----------------------------------------------------------------|   
 | 16-19 | dPkts     | Packets in the flow.                                           |   
 |-------+-----------+----------------------------------------------------------------|   
 | 20-23 | dOctets   | Total number of Layer 3 bytes in the packets of the flow.      |   
 |-------+-----------+----------------------------------------------------------------|   
 | 24-27 | First     | SysUptime, in seconds, at start of flow.                       |   
 |-------+-----------+----------------------------------------------------------------|   
 | 28-31 | Last      | SysUptime, in seconds, at the time the last packet of the flow |
 |       |           | was received.                                                  |   
 |-------+-----------+----------------------------------------------------------------|   
 | 32-33 | srcport   | TCP/UDP source port number; set to zero if flow mask is        |   
 |       |           | destination-only or source-destination.                        |   
 |-------+-----------+----------------------------------------------------------------|   
 | 34-35 | dstport   | TCP/UDP destination port number; set to zero if flow mask is   |   
 |       |           | destination-only or source-destination.                        |   
 |-------+-----------+----------------------------------------------------------------|   
 | 36    | flags     | Flags indicating, among other things, what flow fields are     |   
 |       |           | invalid.                                                       |   
 |-------+-----------+----------------------------------------------------------------|   
 | 37    | tcp_flags | TCP flags; always set to zero.                                 |   
 |-------+-----------+----------------------------------------------------------------|   
 | 38    | prot      | IP protocol type (for example, TCP = 6; UDP = 17); set to zero |   
 |       |           | if flow mask is destination-only or source-destination.        |   
 |-------+-----------+----------------------------------------------------------------|   
 | 39    | tos       | IP type of service; switch sets it to the ToS of the first     |   
 |       |           | packet of the flow.                                            |   
 |-------+-----------+----------------------------------------------------------------|   
 | 40-41 | src_as    | Source autonomous system number, either origin or peer         |   
 |-------+-----------+----------------------------------------------------------------|   
 | 42-43 | dst_as    | Destination autonomous system number, either origin or peer    |   
 |-------+-----------+----------------------------------------------------------------|   
 | 44    | src_mask  | Source address prefix mask; always set to zero.                |   
 |-------+-----------+----------------------------------------------------------------|   
 | 45    | dst_mask  | Destination address prefix mask; always set to zero.           |   
 |-------+-----------+----------------------------------------------------------------|   
 | 46-47 | flags     | Flags indicating, among other things, what flows are invalid.  |   
 |-------+-----------+----------------------------------------------------------------|   
 | 48-51 | router_sc | IP address of the router that is bypassed by the Catalyst 5000 |   
 |       |           | series switch. This is the same address the router uses when   |   
 |       |           | it sends NetFlow export packets. This IP address is propagated |   
 |       |           | to all switches bypassing the router through the FCP protocol. |   
 +------------------------------------------------------------------------------------+ 

 */

public class V7_Flow extends Flow {
	String srcaddr, dstaddr, nexthop;

	long input, output;

	long dPkts, dOctets, First, Last;

	long srcport, dstport;

	byte flags, tcp_flags, prot, tos;

	long src_as, dst_as;

	byte src_mask, dst_mask;

	long flags2, router_sc;

	String new_source;

	String RouterIP;

	long src_addr, dst_addr, next_hop;

	public V7_Flow(boolean replace_source, String RouterIP, byte[] buf, int off)
			throws DoneException {
		this.new_source = replace_source ? Util.str_addr(router_sc) : RouterIP;
		this.RouterIP = RouterIP;

		// long saddr, daddr;

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
		flags = buf[off + 36];
		tcp_flags = buf[off + 37];
		prot = buf[off + 38];
		tos = buf[off + 39];
		src_as = Util.to_number(buf, off + 40, 2);
		dst_as = Util.to_number(buf, off + 42, 2);
		src_mask = buf[off + 44];
		dst_mask = buf[off + 45];
		flags2 = Util.to_number(buf, off + 46, 2);
		router_sc = Util.to_number(buf, off + 48, 4);

		if (Syslog.log.need(Syslog.LOG_INFO)) {
			Syslog.log.syslog(Syslog.LOG_INFO, "      " + srcaddr + ":"
					+ srcport + " -> " + dstaddr + ":" + dstport + " via "
					+ nexthop);
			Syslog.log.syslog(Syslog.LOG_INFO, "        bytes=" + dOctets
					+ ", pkts=" + dPkts + ", proto=" + prot + ", TOS=" + tos
					+ ", TCPF=" + tcp_flags);
			Syslog.log.syslog(Syslog.LOG_INFO, "        inIf=" + input
					+ ", outIf=" + output + ", SAS=" + src_as + ", DAS="
					+ dst_as + ", SM=" + src_mask + ", DM=" + dst_mask);
			Syslog.log.syslog(Syslog.LOG_INFO, "        flags=" + flags
					+ ", flags2=" + flags2 + ", RP=" + Util.str_addr(router_sc)
					+ ", RS=" + new_source);
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

	public Long getSrcAS() {
		return new Long(src_as);
	}

	public Long getDstAS() {
		return new Long(dst_as);
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
			long flow_sequence, PreparedStatement add_raw_stm) {
		try {
			add_raw_stm.setString(1, RouterIP);
			add_raw_stm.setLong(2, SysUptime);
			add_raw_stm.setLong(3, unix_secs);
			add_raw_stm.setLong(4, unix_nsecs);
			add_raw_stm.setLong(5, flow_sequence);
			add_raw_stm.setString(6, srcaddr);
			add_raw_stm.setString(7, dstaddr);
			add_raw_stm.setString(8, nexthop);
			add_raw_stm.setInt(9, (int) input);
			add_raw_stm.setInt(10, (int) output);
			add_raw_stm.setLong(11, dPkts);
			add_raw_stm.setLong(12, dOctets);
			add_raw_stm.setLong(13, First);
			add_raw_stm.setLong(14, Last);
			add_raw_stm.setInt(15, (int) srcport);
			add_raw_stm.setInt(16, (int) dstport);
			add_raw_stm.setInt(17, (int) flags);
			add_raw_stm.setInt(18, (int) tcp_flags);
			add_raw_stm.setInt(19, (int) prot);
			add_raw_stm.setInt(20, (int) tos);
			add_raw_stm.setInt(21, (int) src_as);
			add_raw_stm.setInt(22, (int) dst_as);
			add_raw_stm.setInt(23, (int) src_mask);
			add_raw_stm.setInt(24, (int) dst_mask);
			add_raw_stm.setInt(25, (int) flags2);
			add_raw_stm.setString(26, Util.str_addr(router_sc));
			add_raw_stm.setString(27, Params.getCurrentTime());
			add_raw_stm.executeUpdate();
		} catch (SQLException e) {
			SQL.error_msg("INSERT to V7 raw table", e, null);
		}
	}

	public Scheme_DataSrcAS getDataSrcAS() {
		return new Scheme_DataSrcAS(new_source, 1, 0, // ???
				dPkts, dOctets, src_as);
	}

	public Scheme_DataDstAS getDataDstAS() {
		return new Scheme_DataDstAS(new_source, 1, 0, // ???
				dPkts, dOctets, dst_as);
	}

	public Scheme_DataASMatrix getDataASMatrix() {
		return new Scheme_DataASMatrix(new_source, 1, 0, // ???
				dPkts, dOctets, src_as, dst_as);
	}

	public Scheme_DataNode getDataSrcNode() {
		return new Scheme_DataNode(new_source, 1, 0, // ???
				dPkts, dOctets, srcaddr);
	}

	public Scheme_DataNode getDataDstNode() {
		return new Scheme_DataNode(new_source, 1, 0, // ???
				dPkts, dOctets, dstaddr);
	}

	public Scheme_DataHostMatrix getDataHostMatrix() {
		return new Scheme_DataHostMatrix(new_source, 1, 0, // ???
				dPkts, dOctets, srcaddr, dstaddr);
	}

	public Scheme_DataInterface getDataDstInterface() {
		return new Scheme_DataInterface(new_source, 1, 0, // ???
				dPkts, dOctets, output);
	}

	public Scheme_DataInterfaceMatrix getDataInterfaceMatrix() {
		return new Scheme_DataInterfaceMatrix(new_source, 1, 0, // ???
				dPkts, dOctets, input, output);
	}

	public Scheme_DataProtocol getDataProtocol() {
		return new Scheme_DataProtocol(RouterIP, 1, 0, dPkts, dOctets, this);
	}
}
