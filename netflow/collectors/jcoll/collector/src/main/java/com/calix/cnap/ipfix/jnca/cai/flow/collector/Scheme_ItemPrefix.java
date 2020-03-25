package com.calix.cnap.ipfix.jnca.cai.flow.collector;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_Data;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataPrefix;
import com.calix.cnap.ipfix.jnca.cai.utils.Params;

public class Scheme_ItemPrefix implements Scheme_Item {
	Scheme_DataPrefix data;

	public Scheme_ItemPrefix(Scheme_DataPrefix data) {
		this.data = data;
	}

	public Scheme_Data getData() {
		return (Scheme_Data) data;
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (!data.equals(((Scheme_ItemPrefix) o).data))
			return false;

		if (data.mask != ((Scheme_ItemPrefix) o).data.mask)
			return false;

		if (data.as != ((Scheme_ItemPrefix) o).data.as)
			return false;

		if (data.intf != ((Scheme_ItemPrefix) o).data.intf)
			return false;

		return data.prefix.equals(((Scheme_ItemPrefix) o).data.prefix);
	}

	public String toString() {
		return data.RouterIP + " @ " + data.prefix + "/" + data.mask + ", AS="
				+ data.as + ", If=" + data.intf + " " + data.dPkts + " pkts, "
				+ data.dOctets + " octets, " + data.Flows + " flows";
	}

	public int hashCode() {
		return new String(data.RouterIP + data.prefix).hashCode()
				+ (int) data.mask + (int) data.as + (int) data.intf;
	}

	public void add(Object o) {
		data.add(((Scheme_ItemPrefix) o).data);
	}

	public int fill(PreparedStatement stm, int numi) throws SQLException {
		int num = data.fill(stm, numi);

		stm.setString(num++, data.prefix);
		stm.setInt(num++, (int) data.mask);
		stm.setInt(num++, (int) data.as);
		stm.setInt(num++, (int) data.intf);
		stm.setString(num++, Params.getCurrentTime());

		return num;
	}

}
