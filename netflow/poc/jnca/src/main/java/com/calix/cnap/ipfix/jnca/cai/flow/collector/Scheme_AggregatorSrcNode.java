package com.calix.cnap.ipfix.jnca.cai.flow.collector;

import java.util.Enumeration;
import java.util.Vector;

import com.calix.cnap.ipfix.jnca.cai.flow.packets.FlowPacket;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataNode;
import com.calix.cnap.ipfix.jnca.cai.sql.SQL;

public class Scheme_AggregatorSrcNode extends Scheme_Aggregator {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4756080165986376995L;

	public Scheme_AggregatorSrcNode(SQL sql, long interval) {
		super(sql, "SrcNode", interval);
	}

	public void add(FlowPacket packet) {
		Vector v = packet.getSrcNodeVector();

		if (v == null)
			return;

		for (Enumeration f = v.elements(); f.hasMoreElements();)
			add(new Scheme_ItemNode((Scheme_DataNode) f.nextElement()));
	}

}
