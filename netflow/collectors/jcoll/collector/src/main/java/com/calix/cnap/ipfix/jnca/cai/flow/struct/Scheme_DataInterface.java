package com.calix.cnap.ipfix.jnca.cai.flow.struct;

public class Scheme_DataInterface extends Scheme_Data {
	public long intf;

	public Scheme_DataInterface(String RouterIP, long Flows, long Missed,
			long dPkts, long dOctets, long intf) {
		super(RouterIP, Flows, Missed, dPkts, dOctets);
		this.intf = intf;
	}

}
