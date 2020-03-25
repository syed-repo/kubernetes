package com.calix.cnap.ipfix.jnca.cai.flow.collector;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_Data;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataSrcAS;
import com.calix.cnap.ipfix.jnca.cai.utils.Params;

public class Scheme_ItemSrcAS implements Scheme_Item {
	Scheme_DataSrcAS data;

	public Scheme_ItemSrcAS(Scheme_DataSrcAS data) {
		this.data = data;
	}

	public Scheme_Data getData() {
		return (Scheme_Data) data;
	}

	public boolean equals(Object oi) {
		if (oi == this)
			return true;

		Scheme_ItemSrcAS o = (Scheme_ItemSrcAS) oi;

		if (!data.equals(o.data))
			return false;

		if (!data.Src_As.equals(o.data.Src_As))
			return false;

		return true;
	}

	public String toString() {
		return "SrcAS: " + data.RouterIP + " " + data.Src_As + " " + data.dPkts
				+ " pkts, " + data.dOctets + " octets, " + data.Flows
				+ " flows";
	}

	public int hashCode() {
		return new String(data.RouterIP + data.Src_As).hashCode();
	}

	public void add(Object o) {
		data.add(((Scheme_ItemSrcAS) o).data);
	}

	public int fill(PreparedStatement stm, int numi) throws SQLException {
		int num = data.fill(stm, numi);

		stm.setString(num++, data.Src_As);
		stm.setString(num++, Params.getCurrentTime());

		return num;
	}

}
