package com.calix.cnap.ipfix.jnca.cai.flow.collector;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_Data;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataHostMatrix;
import com.calix.cnap.ipfix.jnca.cai.utils.Params;

public class Scheme_ItemHostMatrix implements Scheme_Item {
	Scheme_DataHostMatrix data;

	public Scheme_ItemHostMatrix(Scheme_DataHostMatrix data) {
		this.data = data;
	}

	public Scheme_Data getData() {
		return (Scheme_Data) data;
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (!data.equals(((Scheme_ItemHostMatrix) o).data))
			return false;

		if (!data.src.equals(((Scheme_ItemHostMatrix) o).data.src))
			return false;

		if (!data.dst.equals(((Scheme_ItemHostMatrix) o).data.dst))
			return false;

		return true;
	}

	public String toString() {
		return data.RouterIP + " @ " + data.src + " -> " + data.dst + ", "
				+ data.dPkts + " pkts, " + data.dOctets + " octets, "
				+ data.Flows + " flows";
	}

	public int hashCode() {
		return new String(data.RouterIP + data.src + data.dst).hashCode();
	}

	public void add(Object o) {
		data.add(((Scheme_ItemHostMatrix) o).data);
	}

	public int fill(PreparedStatement stm, int numi) throws SQLException {
		int num = data.fill(stm, numi);

		stm.setString(num++, data.src);
		stm.setString(num++, data.dst);
		stm.setString(num++, Params.getCurrentTime());

		return num;
	}

}
