package com.calix.cnap.ipfix.jnca.cai.flow.collector;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_Data;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataInterface;
import com.calix.cnap.ipfix.jnca.cai.utils.Params;

public class Scheme_ItemInterface implements Scheme_Item {
	Scheme_DataInterface data;

	public Scheme_ItemInterface(Scheme_DataInterface data) {
		this.data = data;
	}

	public Scheme_Data getData() {
		return (Scheme_Data) data;
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (!data.equals(((Scheme_ItemInterface) o).data))
			return false;

		return data.intf == ((Scheme_ItemInterface) o).data.intf;
	}

	public String toString() {
		return data.RouterIP + " via " + data.intf + ", " + data.dPkts
				+ " pkts, " + data.dOctets + " octets, " + data.Flows
				+ " flows";
	}

	public int hashCode() {
		return data.RouterIP.hashCode() + (int) data.intf;
	}

	public void add(Object o) {
		data.add(((Scheme_ItemInterface) o).data);
	}

	public int fill(PreparedStatement stm, int numi) throws SQLException {
		int num = data.fill(stm, numi);

		stm.setInt(num++, (int) data.intf);
		stm.setString(num++, Params.getCurrentTime());

		return num;
	}

}
