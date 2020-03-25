package com.calix.cnap.ipfix.jnca.cai.flow.struct;

import com.calix.cnap.ipfix.jnca.cai.flow.collector.interpretator.IpSegmentManager;
import com.calix.cnap.ipfix.jnca.cai.utils.Util;

public class Address {
	public long address;

	public Address(long address) {
		this.address = address;
	}
	/**
	 * 转化成string形式的地址格式
	 */
	public String toString() {
//		return Util.str_addr(address);
		return IpSegmentManager.getInstance().convertIP(address);
	}

	public boolean equals(Address o) {
		return address == o.address;
	}
}
