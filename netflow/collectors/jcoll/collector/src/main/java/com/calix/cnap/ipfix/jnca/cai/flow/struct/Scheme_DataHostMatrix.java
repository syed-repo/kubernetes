package com.calix.cnap.ipfix.jnca.cai.flow.struct;

public class Scheme_DataHostMatrix extends Scheme_Data {
	public String src, dst;

	public Scheme_DataHostMatrix(String RouterIP, long Flows, long Missed,
			long dPkts, long dOctets, String src, String dst) {
		super(RouterIP, Flows, Missed, dPkts, dOctets);
		this.src = src;
		this.dst = dst;
	}

}
