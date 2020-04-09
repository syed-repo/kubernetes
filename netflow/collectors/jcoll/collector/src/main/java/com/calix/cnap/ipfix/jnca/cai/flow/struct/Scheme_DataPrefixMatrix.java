package com.calix.cnap.ipfix.jnca.cai.flow.struct;

import com.calix.cnap.ipfix.jnca.cai.flow.collector.interpretator.IpSegmentManager;
import com.calix.cnap.ipfix.jnca.cai.utils.Util;

public class Scheme_DataPrefixMatrix extends Scheme_Data {
	public String src_prefix, dst_prefix;

	public byte src_mask, dst_mask;

	public long src_as, src_intf, dst_as, dst_intf;

	public Scheme_DataPrefixMatrix(String RouterIP, long Flows, long Missed,
			long dPkts, long dOctets, Prefix srcprefix, long src_as,
			long src_intf, Prefix dstprefix, long dst_as, long dst_intf) {
		super(check(RouterIP), Flows, Missed, dPkts, dOctets);

		this.src_prefix = IpSegmentManager.getInstance().convertIP(srcprefix.address);
		this.src_mask = srcprefix.mask;
		this.src_as = src_as;
		this.src_intf = src_intf;

		this.dst_prefix = Util.str_addr(dstprefix.address);
		this.dst_mask = dstprefix.mask;
		this.dst_as = dst_as;
		this.dst_intf = dst_intf;
	}

	private static String check(String check) {
		if (check == null)
			throw new RuntimeException(
					"check==null while create Scheme_DataPrefixMatrix");

		return check;
	}

}
