package com.calix.cnap.ipfix.jnca.cai.flow.struct;

import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * 一种归并模式下的流量数据
 * @author CaiMao
 *
 */
public class Scheme_Data {
	public String RouterIP;

	public long Flows, Missed, dPkts, dOctets;

	public Scheme_Data(String RouterIP, long Flows, long Missed, long dPkts,
			long dOctets) {
		if (RouterIP == null)
			throw new RuntimeException("RouterIp==null in " + this.toString());

		this.RouterIP = RouterIP;
		this.Flows = Flows;
		this.Missed = Missed;
		this.dPkts = dPkts;
		this.dOctets = dOctets;
	}

	// may be overloaded

	public boolean equals(Scheme_Data o) {
		if (o == this)
			return true;

		return RouterIP.equals(o);
	}

	public void add(Scheme_Data o) {
		dPkts += o.dPkts;
		dOctets += o.dOctets;
		Flows += o.Flows;
		Missed += o.Missed;
	}
	/**
	 * 填充固定的流量数据字段
	 * @param stm
	 * @param num
	 * @return
	 * @throws SQLException
	 */
	public int fill(PreparedStatement stm, int num) throws SQLException {
		stm.setString(num++, RouterIP);
		stm.setLong(num++, Flows);
		stm.setLong(num++, Missed);
		stm.setLong(num++, dPkts);
		stm.setLong(num++, dOctets);

		return num;
	}

}
