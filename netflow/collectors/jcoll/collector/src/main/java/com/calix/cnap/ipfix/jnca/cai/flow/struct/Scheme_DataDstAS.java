package com.calix.cnap.ipfix.jnca.cai.flow.struct;

public class Scheme_DataDstAS extends Scheme_Data {
	public String Dst_As;

	public Scheme_DataDstAS(String RouterIP, long Flows, long Missed,
			long dPkts, long dOctets, long Dst_As) {
		super(RouterIP, Flows, Missed, dPkts, dOctets);
		this.Dst_As = DataAS.aggregate(Dst_As, DataAS.AS_Destination);
	}

}
