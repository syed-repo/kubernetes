package com.calix.cnap.ipfix.jnca.cai.flow.collector.interpretator;

import com.calix.cnap.ipfix.jnca.cai.sql.SQL;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.calix.cnap.ipfix.jnca.cai.utils.Util;

public class IpSegmentManager {
	public static int MAXSEG = 1000;

	long[] segments = new long[MAXSEG];

	String[] segNames = new String[MAXSEG];

	int segmentsLen = 0;
        int dummy=0;
	private static IpSegmentManager inst = new IpSegmentManager();

	public static IpSegmentManager getInstance() {
		return inst;
	}

	private IpSegmentManager() {
                if(dummy == 0 )
                    return;
		ResultSet rs = new SQL().execToken("fetching IP segments",
				"SQL.selectSegments");
		int idx = 0;
		try {
			while (rs.next()) {
				String ips = rs.getString("IpSeg").trim();
				String ipn = rs.getString("SegName").trim();
				long ipsl = Util.convertIPS2Long(ips);
				if (ipsl != 0l && idx < MAXSEG) {
					segments[idx] = ipsl;
					segNames[idx] = ipn;
					idx++;
				}
			}
			// rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		segmentsLen = idx;
		// 然后排序
		sort(segments, segNames, 0, idx - 1);
	}

	/**
	 * 大的IP放在前面，先匹配,使用快速排序
	 * 
	 * @param ips
	 * @param ipsn
	 * @param lastIdx
	 */
	void sort(long[] ips, String[] ipsn, int beginIdx, int lastIdx) {
		while (beginIdx < lastIdx) {
			if (ips[beginIdx] < ips[lastIdx]) {
				// 交换
				long tmpl = ips[beginIdx];
				ips[beginIdx] = ips[lastIdx];
				ips[lastIdx] = tmpl;
				String tmps = ipsn[beginIdx];
				ipsn[beginIdx] = ipsn[lastIdx];
				ipsn[lastIdx] = tmps;
			}
			beginIdx++;
			lastIdx--;
		}
		int half = (lastIdx - beginIdx) / 2;
		if (half >= 2) {
			sort(ips, ipsn, beginIdx, beginIdx + half);
			sort(ips, ipsn, beginIdx + half, lastIdx);
		} else {
			return;
		}
	}

	private int getIPIdx(int IP) {
		for (int idx = 0; idx < this.segmentsLen; idx++) {
			if ((this.segments[idx] & IP) == this.segments[idx]) {
				if ((this.segments[idx] | IP) == IP) {
					return idx;
				}
			}
		}
		return -1;
	}

	public String convertIP(long addr) {
		return convertIP((int) (addr & 0xffffffff));
	}

	public String convertIP(int addr) {
		StringBuffer buf = new StringBuffer();
		buf.append(((addr >>> 24) & 0xff)).append('.').append(
				((addr >>> 16) & 0xff)).append('.').append(
				((addr >>> 8) & 0xff)).append('.').append(addr & 0xff);
		return buf.toString();
	}

	public String getSegNameByIP(int addr) {
		int idx = getIPIdx(addr);
		if (idx == -1) {
			return convertIP(addr);
		} else {
			return this.segNames[idx];
		}
	}

	public long getSegByIP(int IP) {
		int idx = getIPIdx(IP);
		if (idx == -1) {
			return IP;// not found
		} else {
			return this.segments[idx];
		}
	}

	public long getSegByIP(long IP) {
		return getSegByIP((int) (IP & 0xffffffff));
	}

	public long getSegByIP(String IP) {
		return getSegByIP(Util.convertIPS2Long(IP));
	}

	public String getSegNameByIP(long IP) {
		return getSegNameByIP((int) (IP & 0xffffffff));
	}

	public String getSegNameByIP(String IP) {
		return getSegNameByIP(Util.convertIPS2Long(IP));
	}

	/**
	 * @return Returns the segmentsLen.
	 */
	public int getSegmentsLen() {
		return segmentsLen;
	}

	/**
	 * @param segmentsLen
	 *            The segmentsLen to set.
	 */
	public void setSegmentsLen(int segmentsLen) {
		this.segmentsLen = segmentsLen;
	}
}
