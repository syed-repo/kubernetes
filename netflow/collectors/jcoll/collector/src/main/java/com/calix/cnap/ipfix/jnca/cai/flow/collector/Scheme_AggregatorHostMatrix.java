package com.calix.cnap.ipfix.jnca.cai.flow.collector;

import java.util.Enumeration;
import java.util.Vector;

import com.calix.cnap.ipfix.jnca.cai.flow.packets.FlowPacket;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataHostMatrix;
import com.calix.cnap.ipfix.jnca.cai.sql.SQL;

public class Scheme_AggregatorHostMatrix extends Scheme_Aggregator {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8018164031734057598L;

	public Scheme_AggregatorHostMatrix(SQL sql, long interval) {
		super(sql, "HostMatrix", interval);
	}

	public void add(FlowPacket packet) {
		Vector v = packet.getHostMatrixVector();

		if (v == null)
			return;

		for (Enumeration f = v.elements(); f.hasMoreElements();)
			add(new Scheme_ItemHostMatrix((Scheme_DataHostMatrix) f
					.nextElement()));
	}

}
