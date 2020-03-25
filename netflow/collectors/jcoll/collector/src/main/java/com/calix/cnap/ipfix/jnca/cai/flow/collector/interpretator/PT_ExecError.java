package com.calix.cnap.ipfix.jnca.cai.flow.collector.interpretator;

public class PT_ExecError extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6974035719171467181L;

	String s;

	public PT_ExecError(String s) {
		super(s);
		this.s = s;
	}

	public String toString() {
		return s;
	}
}
