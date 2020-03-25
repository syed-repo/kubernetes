package com.calix.cnap.ipfix.jnca.cai.flow.collector;

import java.util.Enumeration;
import java.util.Vector;

import com.calix.cnap.ipfix.jnca.cai.flow.packets.FlowPacket;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataInterface;
import com.calix.cnap.ipfix.jnca.cai.sql.SQL;

public class Scheme_AggregatorSrcInterface extends Scheme_Aggregator {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4339571449430563322L;

	public Scheme_AggregatorSrcInterface(SQL sql, long interval) {
		super(sql, "SrcInterface", interval);
	}

	public void add(FlowPacket packet) {
		Vector v = packet.getSrcInterfaceVector();

		if (v == null)
			return;

		for (Enumeration f = v.elements(); f.hasMoreElements();)
			add(new Scheme_ItemInterface((Scheme_DataInterface) f.nextElement()));
	}

}
