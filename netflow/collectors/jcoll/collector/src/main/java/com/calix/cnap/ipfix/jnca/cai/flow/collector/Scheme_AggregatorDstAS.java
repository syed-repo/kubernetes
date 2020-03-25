package com.calix.cnap.ipfix.jnca.cai.flow.collector;

import java.util.Enumeration;
import java.util.Vector;

import com.calix.cnap.ipfix.jnca.cai.flow.packets.FlowPacket;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataDstAS;
import com.calix.cnap.ipfix.jnca.cai.sql.SQL;

public class Scheme_AggregatorDstAS extends Scheme_Aggregator {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3465886405990564504L;

	public Scheme_AggregatorDstAS(SQL sql, long interval) {
		super(sql, "DstAS", interval);
	}

	public void add(FlowPacket packet) {
		Vector v = packet.getDstASVector();

		if (v == null)
			return;

		for (Enumeration f = v.elements(); f.hasMoreElements();)
			add(new Scheme_ItemDstAS((Scheme_DataDstAS) f.nextElement()));
	}

}
