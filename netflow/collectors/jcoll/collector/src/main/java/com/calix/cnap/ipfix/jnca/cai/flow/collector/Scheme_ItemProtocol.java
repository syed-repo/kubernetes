package com.calix.cnap.ipfix.jnca.cai.flow.collector;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_Data;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataProtocol;
import com.calix.cnap.ipfix.jnca.cai.utils.Params;

public class Scheme_ItemProtocol implements Scheme_Item {
	Scheme_DataProtocol data;

	public Scheme_ItemProtocol(Scheme_DataProtocol data) {
		this.data = data;
	}

	public Scheme_Data getData() {
		return (Scheme_Data) data;
	}

	public boolean equals(Object oi) {
		if (oi == this)
			return true;

		Scheme_ItemProtocol o = (Scheme_ItemProtocol) oi;

		if (!data.equals(o.data))
			return false;

		if (!data.protocol.equals(o.data.protocol))
			return false;

		return true;
	}

	public String toString() {
		return "DstAS: " + data.RouterIP + " " + data.protocol + " "
				+ data.dPkts + " pkts, " + data.dOctets + " octets, "
				+ data.Flows + " flows";
	}

	public int hashCode() {
		return new String(data.RouterIP + data.protocol).hashCode();
	}

	public void add(Object o) {
		data.add(((Scheme_ItemProtocol) o).data);
	}

	public int fill(PreparedStatement stm, int numi) throws SQLException {
		int num = data.fill(stm, numi);

		stm.setString(num++, data.protocol);
		stm.setString(num++, Params.getCurrentTime());

		return num;
	}

}
