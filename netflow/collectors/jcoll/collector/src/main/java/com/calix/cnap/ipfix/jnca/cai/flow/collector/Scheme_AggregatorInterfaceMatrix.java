package com.calix.cnap.ipfix.jnca.cai.flow.collector;

import java.util.Enumeration;
import java.util.Vector;

import com.calix.cnap.ipfix.jnca.cai.flow.packets.FlowPacket;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataInterfaceMatrix;
import com.calix.cnap.ipfix.jnca.cai.sql.SQL;

public class Scheme_AggregatorInterfaceMatrix extends Scheme_Aggregator {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6724203871691399631L;

	public Scheme_AggregatorInterfaceMatrix(SQL sql, long interval) {
		super(sql, "InterfaceMatrix", interval);
	}

	public void add(FlowPacket packet) {
		Vector v = packet.getInterfaceMatrixVector();

		if (v == null)
			return;

		for (Enumeration f = v.elements(); f.hasMoreElements();)
			add(new Scheme_ItemInterfaceMatrix((Scheme_DataInterfaceMatrix) f
					.nextElement()));
	}

}
