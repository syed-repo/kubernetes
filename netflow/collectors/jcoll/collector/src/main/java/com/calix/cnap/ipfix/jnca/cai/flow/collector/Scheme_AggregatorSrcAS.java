package com.calix.cnap.ipfix.jnca.cai.flow.collector;

import java.util.Enumeration;
import java.util.Vector;

import com.calix.cnap.ipfix.jnca.cai.flow.packets.FlowPacket;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataSrcAS;
import com.calix.cnap.ipfix.jnca.cai.sql.SQL;
/**
 * 一种归并模式
 * @author CaiMao
 *
 */
public class Scheme_AggregatorSrcAS extends Scheme_Aggregator {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7313307991038642118L;

	public Scheme_AggregatorSrcAS(SQL sql, long interval) {
		super(sql, "SrcAS", interval);//入库线程已经创建
	}

	public void add(FlowPacket packet) {
		Vector v = packet.getSrcASVector();

		if (v == null)
			return;

		for (Enumeration f = v.elements(); f.hasMoreElements();)
			add(new Scheme_ItemSrcAS((Scheme_DataSrcAS) f.nextElement()));
	}

}
