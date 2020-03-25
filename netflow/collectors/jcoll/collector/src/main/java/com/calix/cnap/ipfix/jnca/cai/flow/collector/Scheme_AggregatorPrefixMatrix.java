package com.calix.cnap.ipfix.jnca.cai.flow.collector;

import java.util.Enumeration;
import java.util.Vector;

import com.calix.cnap.ipfix.jnca.cai.flow.packets.FlowPacket;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataPrefixMatrix;
import com.calix.cnap.ipfix.jnca.cai.sql.SQL;

public class Scheme_AggregatorPrefixMatrix extends Scheme_Aggregator {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1252372442487649097L;

	public Scheme_AggregatorPrefixMatrix(SQL sql, long interval) {
		super(sql, "PrefixMatrix", interval);
	}

	public void add(FlowPacket packet) {
		Vector v = packet.getPrefixMatrixVector();

		if (v == null)
			return;

		for (Enumeration f = v.elements(); f.hasMoreElements();)
			add(new Scheme_ItemPrefixMatrix((Scheme_DataPrefixMatrix) f
					.nextElement()));
	}

}
