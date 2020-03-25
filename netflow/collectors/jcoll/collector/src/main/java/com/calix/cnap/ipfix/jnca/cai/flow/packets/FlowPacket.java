package com.calix.cnap.ipfix.jnca.cai.flow.packets;

import java.util.Vector;

import com.calix.cnap.ipfix.jnca.cai.sql.SQL;

public interface FlowPacket {
	void process_raw(SQL sql);

	Vector getSrcASVector();

	Vector getDstASVector();

	Vector getASMatrixVector();

	Vector getSrcNodeVector();

	Vector getDstNodeVector();

	Vector getHostMatrixVector();

	Vector getSrcInterfaceVector();

	Vector getDstInterfaceVector();

	Vector getInterfaceMatrixVector();

	Vector getSrcPrefixVector();

	Vector getDstPrefixVector();

	Vector getPrefixMatrixVector();

	Vector getProtocolVector();

	Vector getFlows();
}
