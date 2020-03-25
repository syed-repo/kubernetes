package com.calix.cnap.ipfix.jnca.cai.flow.collector;

import java.util.Enumeration;
import java.util.Vector;

import com.calix.cnap.ipfix.jnca.cai.flow.packets.FlowPacket;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataPrefix;
import com.calix.cnap.ipfix.jnca.cai.sql.SQL;

public class Scheme_AggregatorSrcPrefix extends Scheme_Aggregator {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1614415610177104575L;

	public Scheme_AggregatorSrcPrefix(SQL sql, long interval) {
		super(sql, "SrcPrefix", interval);
	}

	public void add(FlowPacket packet) {
		Vector v = packet.getSrcPrefixVector();

		if (v == null)
			return;

		for (Enumeration f = v.elements(); f.hasMoreElements();)
			add(new Scheme_ItemPrefix((Scheme_DataPrefix) f.nextElement()));
	}

}
