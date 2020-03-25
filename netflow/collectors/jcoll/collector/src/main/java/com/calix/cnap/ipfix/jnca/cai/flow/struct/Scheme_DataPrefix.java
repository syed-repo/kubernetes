package com.calix.cnap.ipfix.jnca.cai.flow.struct;

import com.calix.cnap.ipfix.jnca.cai.flow.collector.interpretator.IpSegmentManager;

public class Scheme_DataPrefix extends Scheme_Data {
	public String prefix="";

	public byte mask;

	public long as, intf;

	public Scheme_DataPrefix(String RouterIP, long Flows, long Missed,
			long dPkts, long dOctets, Prefix prefix, long as, long intf) {
		super(RouterIP, Flows, Missed, dPkts, dOctets);
		// convertIP不会产生中文名称
		if (prefix != null) {
			this.prefix = IpSegmentManager.getInstance().convertIP(
					prefix.address);
		}
		this.mask = prefix.mask;
		this.as = as;
		this.intf = intf;
	}

}
