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
import com.calix.cnap.ipfix.jnca.cai.utils.DoneException;

/**
 *
 *
 * @author CaiMao
 *
 */
public interface Template {
    	public int getVersion();
	public String getRouterIp();
	public void setRouterIp(String routerIp);
	public void makeTemplate(String routerIp, int tid) throws Exception;
	public void makeTemplate(String routerIp, Properties pr, int tid) throws Exception ;
	public int getTypeOffset(int typeName) ;
	public int getTypeLen(int typeName) ;
	public int getTemplateId() ;
	public void setTemplateId(int templateId);
	public int getSamplingRate() ;
	public void setSamplingRate(int samplingRate);
	public int getWholeOffset() ;
	public void setWholeOffset(int wholeOffset);
}

