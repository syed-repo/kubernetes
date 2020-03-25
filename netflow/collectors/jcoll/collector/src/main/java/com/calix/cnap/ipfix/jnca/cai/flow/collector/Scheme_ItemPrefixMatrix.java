package com.calix.cnap.ipfix.jnca.cai.flow.collector;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_Data;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataPrefixMatrix;
import com.calix.cnap.ipfix.jnca.cai.utils.Params;

public class Scheme_ItemPrefixMatrix implements Scheme_Item {
	Scheme_DataPrefixMatrix data;

	public Scheme_ItemPrefixMatrix(Scheme_DataPrefixMatrix data) {
		this.data = data;
	}

	public Scheme_Data getData() {
		return (Scheme_Data) data;
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (!data.equals(((Scheme_ItemPrefixMatrix) o).data))
			return false;

		if (data.src_mask != ((Scheme_ItemPrefixMatrix) o).data.src_mask)
			return false;

		if (data.src_as != ((Scheme_ItemPrefixMatrix) o).data.src_as)
			return false;

		if (data.src_intf != ((Scheme_ItemPrefixMatrix) o).data.src_intf)
			return false;

		if (data.dst_mask != ((Scheme_ItemPrefixMatrix) o).data.dst_mask)
			return false;

		if (data.dst_as != ((Scheme_ItemPrefixMatrix) o).data.dst_as)
			return false;

		if (data.dst_intf != ((Scheme_ItemPrefixMatrix) o).data.dst_intf)
			return false;

		if (!data.dst_prefix
				.equals(((Scheme_ItemPrefixMatrix) o).data.dst_prefix))
			return false;

		return data.src_prefix
				.equals(((Scheme_ItemPrefixMatrix) o).data.src_prefix);
	}

	public String toString() {
		return data.RouterIP + " @ " + data.src_prefix + "/" + data.src_mask
				+ " (" + data.src_as + ", " + data.src_intf + ") " + " -> "
				+ data.dst_prefix + "/" + data.dst_mask + " (" + data.dst_as
				+ ", " + data.dst_intf + ") " + data.dPkts + " pkts, "
				+ data.dOctets + " octets, " + data.Flows + " flows";
	}

	public int hashCode() {
		return new String(data.RouterIP + data.src_prefix + data.dst_prefix)
				.hashCode()
				+ (int) data.src_mask
				+ (int) data.src_as
				+ (int) data.src_intf
				+ (int) data.dst_mask + (int) data.dst_as + (int) data.dst_intf;
	}

	public void add(Object o) {
		data.add(((Scheme_ItemPrefixMatrix) o).data);
	}

	public int fill(PreparedStatement stm, int numi) throws SQLException {
		int num = data.fill(stm, numi);

		stm.setString(num++, data.src_prefix);
		stm.setInt(num++, (int) data.src_mask);
		stm.setInt(num++, (int) data.src_as);
		stm.setInt(num++, (int) data.src_intf);

		stm.setString(num++, data.dst_prefix);
		stm.setInt(num++, (int) data.dst_mask);
		stm.setInt(num++, (int) data.dst_as);
		stm.setInt(num++, (int) data.dst_intf);
		stm.setString(num++, Params.getCurrentTime());

		return num;
	}

}
