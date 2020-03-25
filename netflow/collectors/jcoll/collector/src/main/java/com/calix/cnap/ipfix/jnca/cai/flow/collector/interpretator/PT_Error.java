package com.calix.cnap.ipfix.jnca.cai.flow.collector.interpretator;

public class PT_Error extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3792507329919419851L;

	String s;

	public PT_Error(String s) {
		super(s);
		this.s = s;
	}

	public String toString() {
		return s;
	}
}
