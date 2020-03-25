/**
 *
 */
package com.calix.cnap.ipfix.jnca.cai.flow.packets.v9;

import java.util.Hashtable;

/**
 * @author CaiMao
 *
 */
public class OptionTemplateManager {
	Hashtable optionTemplates = new Hashtable();

	private static OptionTemplateManager mgr = new OptionTemplateManager();

	public static OptionTemplateManager getOptionTemplateManager() {
		return mgr;
	}
	public boolean acceptOptionTemplate(String routerIp, byte[] content, int offset) {
		if (offset > 3) {
			try {
//                                optionTemplates = new Hashtable();
				OptionTemplate t = new OptionTemplate(routerIp, content, offset);
				optionTemplates.put(t.getRouterIp() + t.getOptionTemplateId(), t);
				return true;
			} catch (Exception e) {
//				e.printStackTrace();
			}
		}
		return false;
	}

	public OptionTemplate getOptionTemplate(String routerIp, int optionTemplateId) {
		return (OptionTemplate) optionTemplates.get(routerIp + optionTemplateId);
	}
}
