package com.calix.cnap.ipfix.jnca.cai.flow.packets.v9;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.calix.cnap.ipfix.jnca.cai.sql.SQL;
import com.calix.cnap.ipfix.jnca.cai.utils.DoneException;
import com.calix.cnap.ipfix.jnca.cai.utils.Params;
import com.calix.cnap.ipfix.jnca.cai.utils.Util;

/*-------*-----------*----------------------------------------------------------*
 | Bytes | Contents  | Description                                              |
 *-------*-----------*----------------------------------------------------------*
 | 0-1   |tmeplateid | templateid==flowsetid                                    |
 *-------*-----------*----------------------------------------------------------*
 | 4-7   | length    | TLV                                                      |
 *-------*-----------*----------------------------------------------------------*/

public class OptionFlow {
	String routerIP;

	long[] values = new long[TemplateIPFix9.MAX_TYPE];

	OptionTemplate myTemplate = null;

	public OptionFlow(String RouterIP, final byte[] buf, int off,
			OptionTemplate t) throws DoneException {
		this.routerIP = RouterIP;
		myTemplate = t;
		if (buf.length < t.getTypeOffset(-1)) {// 多做判断，去掉垃圾包
			throw new DoneException("包长度不符合OptionTemplate"
					+ t.getOptionTemplateId() + "要求");
		}
		// 考虑offset和长度为非法值的问题的问题
		int currOffset = 0, currLen = 0;
		for (int idx = 0; idx < TemplateIPFix9.MAX_TYPE; idx++) {
			if (t.getTypeLen(idx) > 0) {// this is in the template
				currOffset = t.getTypeOffset(idx);
				currLen = t.getTypeLen(idx);
				if (currOffset >= 0 && currLen > 0) {
					values[idx] = Util
							.to_number(buf, off + currOffset, currLen);
				}
			}
		}
	}

	public void save_raw(long SysUptime, long unix_secs, long packageSequence,
			long sourceId, PreparedStatement add_raw_stm) {
		try {
			for (int typeName = 0; typeName < TemplateIPFix9.MAX_TYPE; typeName++) {
				if (myTemplate.getTypeLen(typeName) > 0) {
					add_raw_stm.setString(1, routerIP);
					add_raw_stm.setLong(2, SysUptime);
					add_raw_stm.setLong(3, unix_secs);
					add_raw_stm.setLong(4, packageSequence);
					add_raw_stm.setLong(5, sourceId);
					add_raw_stm.setString(6, myTemplate.isScopeType(typeName)?"T":"F");
					add_raw_stm.setInt(7,myTemplate.getOptionTemplateId());
					add_raw_stm.setInt(8,typeName);
					add_raw_stm.setLong(9,values[typeName]);
					add_raw_stm.setString(10, Params.getCurrentTime());
					add_raw_stm.executeUpdate();
				}
			}
		} catch (SQLException e) {
			SQL.error_msg("INSERT to V9 raw table", e, null);
		}
	}
}

