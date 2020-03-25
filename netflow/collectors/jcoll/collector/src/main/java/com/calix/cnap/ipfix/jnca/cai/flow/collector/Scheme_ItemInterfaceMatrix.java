package com.calix.cnap.ipfix.jnca.cai.flow.collector;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_Data;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataInterfaceMatrix;
import com.calix.cnap.ipfix.jnca.cai.utils.Params;

public class Scheme_ItemInterfaceMatrix implements Scheme_Item {
	Scheme_DataInterfaceMatrix data;

	public Scheme_ItemInterfaceMatrix(Scheme_DataInterfaceMatrix data) {
		this.data = data;
	}

	public Scheme_Data getData() {
		return (Scheme_Data) data;
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (!data.equals(((Scheme_ItemInterfaceMatrix) o).data))
			return false;

		if (data.input == ((Scheme_ItemInterfaceMatrix) o).data.input)
			return true;

		return data.output == ((Scheme_ItemInterfaceMatrix) o).data.output;
	}

	public String toString() {
		return data.RouterIP + " @ " + data.input + " -> " + data.output
				+ data.dPkts + " pkts, " + data.dOctets + " octets, "
				+ data.Flows + " flows";
	}

	public int hashCode() {
		return data.RouterIP.hashCode() + (int) data.input + (int) data.output;
	}

	public void add(Object o) {
		data.add(((Scheme_ItemInterfaceMatrix) o).data);
	}

	public int fill(PreparedStatement stm, int numi) throws SQLException {
		int num = data.fill(stm, numi);

		stm.setInt(num++, (int) data.input);
		stm.setInt(num++, (int) data.output);
		stm.setString(num++, Params.getCurrentTime());

		return num;
	}

}
