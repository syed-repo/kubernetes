package com.calix.cnap.ipfix.jnca.cai.flow.struct;

public class Scheme_DataASMatrix extends Scheme_Data {
	public String Src_As, Dst_As;

	public Scheme_DataASMatrix(String RouterIP, long Flows, long Missed,
			long dPkts, long dOctets, long Src_As, long Dst_As) {
		super(RouterIP, Flows, Missed, dPkts, dOctets);
		this.Src_As = DataAS.aggregate(Src_As, DataAS.AS_Source);
		this.Dst_As = DataAS.aggregate(Dst_As, DataAS.AS_Destination);
	}

}
