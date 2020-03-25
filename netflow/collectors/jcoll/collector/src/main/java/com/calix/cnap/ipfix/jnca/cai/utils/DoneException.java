package com.calix.cnap.ipfix.jnca.cai.utils;

public class DoneException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2257630368096030109L;

	String message;

	public DoneException(String message) {
		this.message = message;
	}

	public String toString() {
		return message;
	}
}
