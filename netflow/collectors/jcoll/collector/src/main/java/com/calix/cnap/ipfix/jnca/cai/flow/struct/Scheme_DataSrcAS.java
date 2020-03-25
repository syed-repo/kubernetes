package com.calix.cnap.ipfix.jnca.cai.flow.struct;
/**
 * 按照SrcAS归并下的流量数据
 * @author CaiMao
 *
 */
public class Scheme_DataSrcAS extends Scheme_Data {
	public String Src_As;

	public Scheme_DataSrcAS(String RouterIP, long Flows, long Missed,
			long dPkts, long dOctets, long Src_As) {
		super(RouterIP, Flows, Missed, dPkts, dOctets);
		//用.aggregate文件归并一次
		this.Src_As = DataAS.aggregate(Src_As, DataAS.AS_Source);
	}

}
