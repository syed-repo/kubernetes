package com.calix.cnap.ipfix.jnca.cai.flow.packets.v9;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;
import com.calix.util.packet.IpAddress;
import com.calix.cnap.ipfix.jnca.cai.utils.Params;
import com.calix.cnap.ipfix.jnca.cai.utils.Util;
import com.calix.cnap.ipfix.api.IpfixIeIdService;
import com.calix.cnap.ipfix.jnca.Jnca;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *
 * 
 *
 */

public class TemplateIPFix10 implements Template {
	final static int MAX_TYPE = 93;

	private int samplingRate = 1;// init to 1

	static String templatePath = Params.path + "/etc/templates/";

	private int wholeOffset = 0;

	private final Logger log = LoggerFactory.getLogger(getClass());

	public IpfixIeIdService ipfixIEIdService;
	{
		ipfixIEIdService = Jnca.getIpfixIeIdService();
	}
	
	/**
	 * TODO check for v10 : is persistency needed
	 */
	Properties prop = new Properties();

	// TODO remove below : offsetOftypes
	int[] offsetOftypes = new int[MAX_TYPE];// In the template the location of the field
	// TODO remove below : lenOftypes
	int[] lenOftypes = new int[MAX_TYPE];// In the template the location of the field

	// Arrays to hold fields meta details
	int fieldCount = 0;

	public int getFieldCount() {
		return fieldCount;
	}

	int[] typesArray = null;
	String[] namesArray = null;
	boolean[] isVendorDefArray = null;
	int[] lengthArray = null; // new int[MAX_TYPE];// runtime fetch
	long[] vendorIdsArray = null;

	int templateId = 0;

	String routerIp = null;

	/**
	 * @return Returns the routerIp.
	 */
	public String getRouterIp() {
		return routerIp;
	}

	/**
	 * @param routerIp The routerIp to set.
	 */
	public void setRouterIp(String routerIp) {
		this.routerIp = routerIp;
	}

	/**
	 * 
	 *
	 * @param fileName
	 * @throws Exception
	 */
	public TemplateIPFix10(String fileName) throws Exception {
		int beginIdx = fileName.lastIndexOf("\\");
		if (beginIdx < 0) {
			beginIdx = 0;
		} else {
			beginIdx += 1;
		}
		String routerIp = fileName.trim().substring(beginIdx, fileName.indexOf("_"));
		String templateIdStr = fileName.trim().substring(fileName.indexOf("_") + 1, fileName.lastIndexOf("."));
		int tid = Integer.parseInt(templateIdStr);
		makeTemplate(routerIp, tid);
	}

	/**
	 * 
	 *
	 * @param routerIp
	 * @param tid
	 * @throws Exception
	 */
	public TemplateIPFix10(String routerIp, int tid) throws Exception {
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
		String fullName = null;

		if (routerIp.indexOf(File.separator) == -1) {
			fullName = templatePath + routerIp;
		} else {
			fullName = routerIp;
		}
		File propFile = new File(fullName + "_" + tid + ".properties");
		if (propFile.exists()) {
			InputStream propIn = new FileInputStream(propFile);
			prop.load(propIn);
		} else {
			System.err.println("Propery file does not exist :" + propFile );
		}
		wholeOffset = Integer.parseInt(prop.getProperty("-1"));
		if (prop != null) {
			for (Enumeration theKeys = prop.propertyNames(); theKeys.hasMoreElements();) {
				String key = theKeys.nextElement().toString();
				int typeName = Integer.parseInt(key);
				if (typeName > 0 && typeName < TemplateIPFix9.MAX_TYPE) {
					int offset = Integer.parseInt(prop.getProperty(key));
					this.offsetOftypes[typeName] = offset;
					this.lenOftypes[typeName] = wholeOffset - offset;
				}
			}
			for (Enumeration theKeys = prop.propertyNames(); theKeys.hasMoreElements();) {
				String key = theKeys.nextElement().toString();
				int typeName = Integer.parseInt(key);
				if (typeName > 0 && typeName < TemplateIPFix9.MAX_TYPE) {
					if (typeName == 11) {
						System.out.println("");
					}
					for (int i = 0; i < offsetOftypes.length; i++) {
						if (offsetOftypes[i] >= 0 && (offsetOftypes[i] - offsetOftypes[typeName] > 0)
								&& (offsetOftypes[i] - offsetOftypes[typeName] < lenOftypes[typeName])) {
							lenOftypes[typeName] = offsetOftypes[i] - offsetOftypes[typeName];
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 *
	 * @param routerIp
	 * @param pr
	 * @param tid
	 */

	// private Template(String routerIp, Properties pr, int tid) throws
	// Exception {
	// makeTemplate(routerIp, pr, tid);
	// }
	public void makeTemplate(String routerIp, Properties pr, int tid) throws Exception {
		prop = pr;
		templateId = tid;
		setRouterIp(routerIp);
		if (prop != null) {
			File propFile = new File(templatePath + routerIp + "_" + tid + ".properties");
			if (propFile.exists()) {
				propFile.delete();
			}
			OutputStream propOut = new FileOutputStream(propFile);
			prop.store(propOut, "template of " + tid + " " + routerIp);
			propOut.flush();
			propOut.close();
		} else {
			throw new Exception("Template already exists...");
		}
	}

	/**
	 * 
	 *
	 * @param routerIp
	 * @param flowset
	 * @param templateOffset 
	 * @throws Exception
	 */
	public TemplateIPFix10(String RouterIp, byte[] flowset, int templateOffset) throws Exception {
		int tid = (int) Util.to_number(flowset, templateOffset, 2);
		if (tid >= 0 && tid <= 255) { // 0-255 reserved for flowset IDs
			throw new Exception("templateID is invalid " + tid);
		}

		this.templateId = tid;
		this.routerIp = RouterIp;
		int fieldCnt = (int) Util.to_number(flowset, templateOffset + 2, 2);
		templateOffset += 4;

		fieldCount = fieldCnt;
		typesArray = new int[fieldCnt];
		namesArray = new String[fieldCnt];
		isVendorDefArray = new boolean[fieldCnt];
		lengthArray = new int[fieldCnt];
		vendorIdsArray = new long[fieldCnt];

		Properties prop = new Properties();
		log.debug("Field Count: {}", fieldCnt);
		// int dataFlowSetOffset = 4;// after the flowSetID and length
		int dataFlowSetOffset = 0;
		for (int idx = 0; idx < fieldCnt; idx++) {

			short typeField = (short) Util.to_number(flowset, templateOffset, 2);
			templateOffset += 2;

			boolean isVendorDefined = Integer.numberOfLeadingZeros(typeField) == 0;

			int typeName = 0;

			if (isVendorDefined)
				typeName = typeField & 0x7fff;
			else
				typeName = typeField;

			int typeLen = (int) Util.to_number(flowset, templateOffset, 2);
			templateOffset += 2;

			long vendorId = 0;
			if (isVendorDefined) {
				vendorId = (int) Util.to_number(flowset, templateOffset, 4);
				templateOffset += 4;
			}
			typesArray[idx] = typeName;
			namesArray[idx] = ipfixIEIdService.getName(IpAddress.valueOf(RouterIp), typeName);
			isVendorDefArray[idx] = isVendorDefined;
			lengthArray[idx] = typeLen;
			vendorIdsArray[idx] = vendorId;

			log.debug("Field type = {} Field Len = {} IsVendor Defined = {} VendorId = {}",
					  typeName, typeLen, isVendorDefined, vendorId);

			// dataFlowSetOffset += typeLen; TODO check if needed
		}

		/**
		 * TODO fix the persistence of the template if (prop.size() <= 0) {// if nothing
		 * is inputted throw new Exception("FieldTypeÃƒâ€šÃ‚Â·ÃƒÆ’Ã¢â‚¬Â¡Ãƒâ€šÃ‚Â·Ãƒâ€šÃ‚Â¨"); } prop.setProperty(new
		 * Integer(-1).toString(), new Integer(dataFlowSetOffset).toString());
		 * wholeOffset = dataFlowSetOffset; this.makeTemplate(routerIp, prop, tid);
		 **/

	}

	/**
	 * 
	 *
	 * @param typeName
	 * @return
	 */
	public int getTypeOffset(int typeName) {
		if (typeName > 0 && typeName < MAX_TYPE) {
			if (this.offsetOftypes[typeName] == 0) {
				String value = this.prop.getProperty(new Integer(typeName).toString());
				if (value != null) {
					offsetOftypes[typeName] = Integer.parseInt(value);
				}
			}
			return offsetOftypes[typeName];
		} else if (typeName == -1) {
			return wholeOffset;
		} else {
			return -1;
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
	 * @param templateId The templateId to set.
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
	 * @param samplingRate The samplingRate to set.
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
	 * @param wholeOffset The wholeOffset to set.
	 */
	public void setWholeOffset(int wholeOffset) {
		this.wholeOffset = wholeOffset;
	}

	@Override
	public int getVersion() {
		return 10;
	}

	public boolean isFieldVendorDefined(int idx) {
		return isVendorDefArray[idx];
	}

	public int getFieldType(int idx) {
		return typesArray[idx];
	}
	
	public String getFieldName(int idx) {
		return namesArray[idx];
	}
	public int getFieldLength(int idx) {
		return lengthArray[idx];
	}

	public int getFieldVendorId(int idx) {
		return lengthArray[idx];
	}

	public boolean doesIEDExist(int iEd) {
		for (int i = 0; i < this.fieldCount; i++)
			if (typesArray[i] == iEd)
				return true;
		return false;
	}
}


