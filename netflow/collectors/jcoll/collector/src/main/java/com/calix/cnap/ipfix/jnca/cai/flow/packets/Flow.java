package com.calix.cnap.ipfix.jnca.cai.flow.packets;

import com.calix.cnap.ipfix.jnca.cai.flow.struct.Address;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Prefix;

import java.util.concurrent.atomic.AtomicLong;

abstract public class Flow {
	protected static AtomicLong flowCounter = new AtomicLong();

	/**
	 * Used for tracing the processing of the packet flow.
	 */
	protected Long flowTraceSeqNo = 0L;

	protected Flow() {
		flowTraceSeqNo = flowCounter.incrementAndGet();
	}

	public Long getSrcPort() {
		return null;
	}

	public Long getDstPort() {
		return null;
	}

	public Long getProto() {
		return null;
	}

	public Long getTOS() {
		return null;
	}

	public Long getSrcAS() {
		return null;
	}

	public Long getDstAS() {
		return null;
	}

	public Address getSrcAddr() {
		return null;
	}

	public Address getDstAddr() {
		return null;
	}

	public Address getNextHop() {
		return null;
	}

	public Long getInIf() {
		return null;
	}

	public Long getOutIf() {
		return null;
	}

	public Prefix getSrcPrefix() {
		return null;
	}

	public Prefix getDstPrefix() {
		return null;
	}
}
