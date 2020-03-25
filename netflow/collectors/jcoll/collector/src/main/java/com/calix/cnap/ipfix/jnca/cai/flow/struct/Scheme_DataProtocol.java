package com.calix.cnap.ipfix.jnca.cai.flow.struct;

import com.calix.cnap.ipfix.jnca.cai.flow.packets.Flow;

public class Scheme_DataProtocol extends Scheme_Data {
	public String protocol;

	public Scheme_DataProtocol(String RouterIP, long Flows, long Missed,
			long dPkts, long dOctets, Flow flow) {
		super(RouterIP, Flows, Missed, dPkts, dOctets);
		this.protocol = DataProtocol.aggregate(flow);
	}

}
