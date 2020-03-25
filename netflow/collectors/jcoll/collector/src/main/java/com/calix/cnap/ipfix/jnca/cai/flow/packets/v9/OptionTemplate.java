/**
 *
 */
package com.calix.cnap.ipfix.jnca.cai.flow.packets.v9;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

import com.calix.cnap.ipfix.jnca.cai.utils.Params;
import com.calix.cnap.ipfix.jnca.cai.utils.Util;

/**
 * @author CaiMao
 *
 */
public class OptionTemplate {
	final static int MAX_TYPE = 93;// ����һ�����õ���92

	int wholeOffset = 0;

	static String optionTemplatePath = Params.path + "/etc/optionTemplates/";

	/**
	 * key��offset��value��typename
	 */
	Properties prop = new Properties();// persistent store���ڲ���

	int[] offsetOftypes = new int[MAX_TYPE];// runtime fetch

	int[] lenOftypes = new int[MAX_TYPE];// runtime fetch

	boolean[] scopeType = new boolean[MAX_TYPE];// runtime fetch

	int optionTemplateId = 0;

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
	 * Ҫ��֤�ļ���ֻ��һ��_��.
	 *
	 * @param fileName
	 * @throws Exception
	 */
	public OptionTemplate(String fileName) throws Exception {
		int beginIdx = fileName.lastIndexOf("\\");
		if (beginIdx < 0) {
			beginIdx = 0;
		} else {
			beginIdx += 1;
		}
		String routerIp = fileName.trim().substring(beginIdx,
				fileName.indexOf("_"));
		String optionTemplateIdStr = fileName.trim().substring(
				fileName.indexOf("_") + 1, fileName.lastIndexOf("."));
		int tid = Integer.parseInt(optionTemplateIdStr);
		makeOptionTemplate(routerIp, tid);
	}

	/**
	 * ����routerIp��tid���������һ��optionTemplate
	 *
	 * @param routerIp
	 * @param tid
	 * @throws Exception
	 */
	public OptionTemplate(String routerIp, int tid) throws Exception {
		makeOptionTemplate(routerIp, tid);
	}

	/**
	 * �������룬�ָ���������
	 *
	 * @param routerIp
	 * @param tid
	 * @throws Exception
	 */
	public void makeOptionTemplate(String routerIp, int tid) throws Exception {
		this.routerIp = routerIp;
		this.optionTemplateId = tid;
		String fullName = null;
		// ���routerip�Ѿ���һ��Ŀ¼����ʽ��Ҫע�⣬Ŀ¼���治�����»���
		if (routerIp.indexOf(File.separator) == -1) {
			fullName = optionTemplatePath + routerIp;
		} else {
			fullName = routerIp;
		}
		File propFile = new File(fullName + "_" + tid + ".properties");
		if (propFile.exists()) {
			InputStream propIn = new FileInputStream(propFile);
			prop.load(propIn);
		} /*else {
			System.err.println(propFile + "������");
		}*/
		// �ָ����������
		wholeOffset = Integer.parseInt(prop.getProperty("-1"));
		if (prop != null) {
			for (Enumeration theKeys = prop.propertyNames(); theKeys
					.hasMoreElements();) {
				String key = theKeys.nextElement().toString();
				int typeName = Integer.parseInt(key);
				if (typeName > 0 && typeName < OptionTemplate.MAX_TYPE) {
					int offset = Integer.parseInt(prop.getProperty(key));
					this.offsetOftypes[typeName] = offset;
					this.lenOftypes[typeName] = wholeOffset - offset;// ���ﲻ��+1����ǰ��offset+length����
				}
			}
			for (Enumeration theKeys = prop.propertyNames(); theKeys
					.hasMoreElements();) {
				String key = theKeys.nextElement().toString();
				int typeName = Integer.parseInt(key);
				if (typeName > 0 && typeName < OptionTemplate.MAX_TYPE) {
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
	 * ����һ��optionTemplate������д�����
	 *
	 * @param routerIp
	 * @param pr
	 * @param tid
	 */

	// private OptionTemplate(String routerIp, Properties pr, int tid) throws
	// Exception {
	// makeOptionTemplate(routerIp, pr, tid);
	// }
	public void makeOptionTemplate(String routerIp, Properties pr, int tid)
			throws Exception {
		prop = pr;
		optionTemplateId = tid;
		setRouterIp(routerIp);
		if (prop != null) {
			File propFile = new File(optionTemplatePath + routerIp + "_" + tid
					+ ".properties");
			if (propFile.exists()) {
				propFile.delete();
			}
			OutputStream propOut = new FileOutputStream(propFile);
			prop.store(propOut, "optionTemplate of " + tid + " " + routerIp);
		} else {
			throw new Exception("OptionTemplate����Ϊ��");
		}
	}

	/**
	 * �ö�����������һ��optionTemplate
	 *
	 * @param routerIp
	 * @param flowset
	 * @param optionTemplateOffset
	 *            ��optionTemplateId ��ʼ
	 * @throws Exception
	 */
	public OptionTemplate(String routerIp, byte[] flowset,
			int optionTemplateOffset) throws Exception {
		int tid = Util.BytesToInt(flowset, optionTemplateOffset);
		if (tid < 0 || tid > 255) {// 0-255 reserved for flowset IDs
			int scopeLength = Util
					.BytesToInt(flowset, optionTemplateOffset + 2);
			int optionLength = Util.BytesToInt(flowset,
					optionTemplateOffset + 4);
			Properties prop = new Properties();
			optionTemplateOffset += 6;
			int offsetInOptionRecord = 0;// after the option templateID and
			// length
			for (; optionTemplateOffset < scopeLength + optionLength;) {
				int typeName = Util.BytesToInt(flowset, optionTemplateOffset);
				if (optionTemplateOffset < scopeLength) {
					this.scopeType[typeName] = true;// ��ʾ����һ��scope����
				} else {
					this.scopeType[typeName] = false;
				}
				optionTemplateOffset += 2;
				int typeLen = Util.BytesToInt(flowset, optionTemplateOffset);
				optionTemplateOffset += 2;
				if (typeName < MAX_TYPE && typeName > 0) {
					prop.setProperty(new Integer(typeName).toString(),
							new Integer(offsetInOptionRecord).toString());
					this.offsetOftypes[typeName] = offsetInOptionRecord;
					lenOftypes[typeName] = typeLen;
				}
				offsetInOptionRecord += typeLen;
			}
			if (prop.size() <= 0) {// if nothing is inputted
				throw new Exception("FieldType�Ƿ�");
			}
			// ��-1��Ϊkey��ǽ�����Ҳ���Ǳ���ܳ���offset
			prop.setProperty(new Integer(-1).toString(), new Integer(
					offsetInOptionRecord).toString());
			wholeOffset=offsetInOptionRecord;
			this.makeOptionTemplate(routerIp, prop, tid);
		} else {
//			throw new Exception("optionTemplateID�Ƿ�");
		}
	}

	/**
	 * ����-1��ʾ����
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
		} else if (typeName==-1){
			return wholeOffset;
		}else{
			return -1;// ���ﷵ��0���ܻ��������
		}
	}

	public boolean isScopeType(int typeName) {
		return scopeType[typeName];
	}

	public int getTypeLen(int typeName) {
		if (typeName > 0 && typeName < MAX_TYPE) {
			return lenOftypes[typeName];
		}
		return 0;
	}

	/**
	 * @return Returns the optionTemplateId.
	 */
	public int getOptionTemplateId() {
		return optionTemplateId;
	}

	/**
	 * @param optionTemplateId
	 *            The optionTemplateId to set.
	 */
	public void setOptionTemplateId(int optionTemplateId) {
		this.optionTemplateId = optionTemplateId;
	}

	/**
	 * @return Returns the wholeOffset.
	 */
	public int getWholeOffset() {
		return wholeOffset;
	}

	/**
	 * @param wholeOffset The wholeOffset to set.
	 */
	public void setWholeOffset(int wholeOffset) {
		this.wholeOffset = wholeOffset;
	}

}
