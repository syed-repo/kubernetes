package com.calix.cnap.ipfix.jnca.cai.flow.packets.v9;

import java.util.Enumeration;
import java.util.Properties;

import com.calix.cnap.ipfix.jnca.cai.utils.Params;
import com.calix.cnap.ipfix.jnca.cai.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
*/
/**
 * ´ú±ítemplate flowsetÖÐµÄÒ»¸ötemplate TODO ÈÝ´íÎÊÌâ
 *
 * @author CaiMao
 *
 */
public class TemplateIPFix9 implements Template {
	final static int MAX_TYPE = 93;

	private int samplingRate = 1;// init to 1

	static String templatePath = Params.path + "/etc/templates/";

	private int wholeOffset = 0;
	private final Logger log = LoggerFactory.getLogger(getClass());
	/**
	 * keyÊÇtypeName£¬valueÊÇoffset(´ÓÁã¿ªÊ¼)
	 */
	Properties prop = new Properties();// persistent storeÏÖÔÚ²»ÓÃ

	int[] offsetOftypes = new int[MAX_TYPE];// In the template the location of the field

	int[] lenOftypes = new int[MAX_TYPE];// runtime fetch

	int templateId = 0;

	String routerIp = null;

	/**
	 * @return Returns the routerIp.
	 */
	public String getRouterIp() {
		return routerIp;
	}

	/**
	 * @param routerIp
	 *            The routerIp to set.
	 */
	public void setRouterIp(String routerIp) {
		this.routerIp = routerIp;
	}

	/**
	 * Òª±£Ö¤ÎÄ¼þÃûÖ»ÓÐÒ»¸ö_ºÍ.
	 *
	 * @param fileName
	 * @throws Exception
	 */
	public TemplateIPFix9(String fileName) throws Exception {
		int beginIdx = fileName.lastIndexOf("\\");
		if (beginIdx < 0) {
			beginIdx = 0;
		} else {
			beginIdx += 1;
		}
		String routerIp = fileName.trim().substring(beginIdx,
				fileName.indexOf("_"));
		String templateIdStr = fileName.trim().substring(
				fileName.indexOf("_") + 1, fileName.lastIndexOf("."));
		int tid = Integer.parseInt(templateIdStr);
		makeTemplate(routerIp, tid);
	}

	/**
	 * ÀûÓÃrouterIp£¬tid´ÓÍâ´æÔØÈëÒ»¸ötemplate
	 *
	 * @param routerIp
	 * @param tid
	 * @throws Exception
	 */
	public TemplateIPFix9(String routerIp, int tid) throws Exception {
		makeTemplate(routerIp, tid);
	}

	/**
	 * 
	 *
	 * @param routerIp
	 * @param tid
	 * @throws Exception
	 */
	public void makeTemplate(String routerIp, int tid) throws Exception {
		this.routerIp = routerIp;
		this.templateId = tid;
		if (prop != null) {
		wholeOffset = Integer.parseInt(prop.getProperty("-1"));
			for (Enumeration theKeys = prop.propertyNames(); theKeys
					.hasMoreElements();) {
				String key = theKeys.nextElement().toString();
				int typeName = Integer.parseInt(key);
				if (typeName > 0 && typeName < TemplateIPFix9.MAX_TYPE) {
					int offset = Integer.parseInt(prop.getProperty(key));
					this.offsetOftypes[typeName] = offset;
					this.lenOftypes[typeName] = wholeOffset - offset;// ÕâÀï²»ÓÃ+1£¬ÒÔÇ°ÊÇoffset+lengthÀ´µÄ
				}
			}
			for (Enumeration theKeys = prop.propertyNames(); theKeys
					.hasMoreElements();) {
				String key = theKeys.nextElement().toString();
				int typeName = Integer.parseInt(key);
				if (typeName > 0 && typeName < TemplateIPFix9.MAX_TYPE) {
					if (typeName == 11) {
						System.out.println("");
					}
					for (int i = 0; i < offsetOftypes.length; i++) {
						if (offsetOftypes[i] >= 0
								&& (offsetOftypes[i] - offsetOftypes[typeName] > 0)
								&& (offsetOftypes[i] - offsetOftypes[typeName] < lenOftypes[typeName])) {
							lenOftypes[typeName] = offsetOftypes[i]
									- offsetOftypes[typeName];
						}
					}
				}
			}
		}
	}

	/**
	 * ´´½¨Ò»¸ötemplate£¬²¢ÇÒÐ´ÈëÍâ´æ
	 *
	 * @param routerIp
	 * @param pr
	 * @param tid
	 */

	// private Template(String routerIp, Properties pr, int tid) throws
	// Exception {
	// makeTemplate(routerIp, pr, tid);
	// }
	public void makeTemplate(String routerIp, Properties pr, int tid)
			throws Exception {
		if (pr == null) {
			throw new Exception("TemplateÄÚÈÝÎª¿Õ");
		}
		prop = pr;
		templateId = tid;
		setRouterIp(routerIp);

	}

	/**
	 * ÓÃ¶þ½øÖÆÁ÷´´½¨Ò»¸ötemplate
	 *
	 * @param routerIp
	 * @param flowset
	 * @param templateOffset
	 *            ÓÉtemplateId ¿ªÊ¼
	 * @throws Exception
	 */
	public TemplateIPFix9(String routerIp, byte[] flowset, int templateOffset)
			throws Exception {
		int tid = (int)Util.to_number(flowset, templateOffset,2);
		if (tid < 0 || tid > 255) {// 0-255 reserved for flowset IDs
			int fieldCnt = (int)Util.to_number(flowset, templateOffset + 2,2);
			Properties prop = new Properties();
			templateOffset += 4;
//			int dataFlowSetOffset = 4;// after the flowSetID and length
			//ÕâÀï½ö½ö¼ÆËãdata recordÄÚ²¿µÄÆ«ÒÆ£¬Ò»¶¨ÊÇ´Ó0¿ªÊ¼
			int dataFlowSetOffset = 0;
			for (int idx = 0; idx < fieldCnt; idx++) {
				int typeName = (int)Util.to_number(flowset, templateOffset,2);
				templateOffset += 2;
				int typeLen = (int)Util.to_number(flowset, templateOffset,2);
				templateOffset += 2;
				if (typeName < MAX_TYPE && typeName > 0) {
					prop.setProperty(new Integer(typeName).toString(),
							new Integer(dataFlowSetOffset).toString());
					this.offsetOftypes[typeName] = dataFlowSetOffset;
					lenOftypes[typeName] = typeLen;
				}
				dataFlowSetOffset += typeLen;
			}
			if (prop.size() <= 0) {// if nothing is inputted
				throw new Exception("FieldType·Ç·¨");
			}
			// ÓÃ-1×÷Îªkey±ê¼Ç½áÊø£¬Ò²¾ÍÊÇ±ê¼Ç×Ü³¤¶Èoffset
			prop.setProperty(new Integer(-1).toString(), new Integer(
					dataFlowSetOffset).toString());
			wholeOffset = dataFlowSetOffset;
			this.makeTemplate(routerIp, prop, tid);
//                        if (tid==256){
//                            throw new DoneException("savePacketT_"+routerIp+"_"+tid);
//                        }
		} else {
			throw new Exception("templateID·Ç·¨");
		}
	}

	/**
	 * ·µ»Ø-1±íÊ¾³ö´í
	 *
	 * @param typeName
	 * @return
	 */
	public int getTypeOffset(int typeName) {
		if (typeName > 0 && typeName < MAX_TYPE) {
			if (this.offsetOftypes[typeName] == 0) {
				String value = this.prop.getProperty(new Integer(typeName)
						.toString());
				if (value != null) {
					offsetOftypes[typeName] = Integer.parseInt(value);
				}
			}
			return offsetOftypes[typeName];
		} else if (typeName == -1) {
			return wholeOffset;
		} else {
			return -1;// ÕâÀï·µ»Ø0¿ÉÄÜ»áÒýÆðÎó½â
		}
	}

	public int getTypeLen(int typeName) {
		if (typeName > 0 && typeName < MAX_TYPE) {
			return lenOftypes[typeName];
		}
		return 0;
	}

	/**
	 * @return Returns the templateId.
	 */
	public int getTemplateId() {
		return templateId;
	}

	/**
	 * @param templateId
	 *            The templateId to set.
	 */
	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	/**
	 * @return Returns the samplingRate.
	 */
	public int getSamplingRate() {
		return samplingRate;
	}

	/**
	 * @param samplingRate
	 *            The samplingRate to set.
	 */
	public void setSamplingRate(int samplingRate) {
		this.samplingRate = samplingRate;
	}

	/**
	 * @return Returns the wholeOffset.
	 */
	public int getWholeOffset() {
		return wholeOffset;
	}

	/**
	 * @param wholeOffset
	 *            The wholeOffset to set.
	 */
	public void setWholeOffset(int wholeOffset) {
		this.wholeOffset = wholeOffset;
	}

	public int getVersion()
	{
	    return 9;
	}
}


