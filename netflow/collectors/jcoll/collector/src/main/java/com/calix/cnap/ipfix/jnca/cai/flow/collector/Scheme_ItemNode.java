package com.calix.cnap.ipfix.jnca.cai.flow.collector;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_Data;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataNode;
import com.calix.cnap.ipfix.jnca.cai.utils.Params;

public class Scheme_ItemNode implements Scheme_Item {
	Scheme_DataNode data;

	public Scheme_ItemNode(Scheme_DataNode data) {
		this.data = data;
	}

	public Scheme_Data getData() {
		return (Scheme_Data) data;
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (!data.equals(((Scheme_ItemNode) o).data))
			return false;

		if (!data.ip.equals(((Scheme_ItemNode) o).data.ip))
			return false;

		return true;
	}

	public String toString() {
		return data.RouterIP + " @ " + data.ip + ", " + data.dPkts + " pkts, "
				+ data.dOctets + " octets, " + data.Flows + " flows";
	}

	public int hashCode() {
		return new String(data.RouterIP + data.ip).hashCode();
	}

	public void add(Object o) {
		data.add(((Scheme_ItemNode) o).data);
	}

	public int fill(PreparedStatement stm, int numi) throws SQLException {
		int num = data.fill(stm, numi);

		stm.setString(num++, data.ip);
		stm.setString(num++, Params.getCurrentTime());

		return num;
	}

}
