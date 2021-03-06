package com.calix.cnap.ipfix.jnca.cai.flow.struct;

import com.calix.cnap.ipfix.jnca.cai.flow.collector.interpretator.IpSegmentManager;
import com.calix.cnap.ipfix.jnca.cai.utils.Util;

public class Prefix extends Address {
	public byte mask;

	public long dmask;//����

	public Prefix(long address, byte mask) {
		super(address);

		dmask = ~((1 << (32 - (mask & 0xff))) - 1);
		/**
		 * ǰ׺��ַ
		 */
		this.address = mask <= 0 || mask >= 32 ? address : address & dmask;
		this.mask = mask;
	}

	public String toString() {
		return IpSegmentManager.getInstance().convertIP(address) + "/" + mask;
	}

	public boolean equals(Prefix o) {
		return mask == o.mask && address == o.address;
	}

	public boolean consists(Address addr) {
		return (mask <= 0 || mask >= 32 ? addr.address : addr.address & dmask) == address;
	}

	public boolean includes(Prefix value) {
		if (value.mask < mask)
			return false;

		if (equals(value))
			return true;

		return consists(value);
	}
}
