package com.calix.cnap.ipfix.jnca.cai.flow.packets.v9;

import java.io.File;
import java.util.Hashtable;
import com.calix.cnap.ipfix.jnca.cai.utils.Resources;
import com.calix.cnap.ipfix.jnca.cai.utils.Params;
import com.calix.cnap.ipfix.jnca.cai.utils.DoneException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateManager {
    private Template v5Template = null;

    private Resources resources = new Resources("serverSampling");
    
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    private static String v5FileName = "127.0.0.0_32.properties";
    static {
        try {
            Class.forName("com.calix.cnap.ipfix.jnca.cai.flow.collector.Collector");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private TemplateManager() {
/*
        try {
            v5Template = new TemplateIPFix9(v5FileName);
            int samRate = resources.integer(v5Template.getRouterIp());
            if (samRate != 0) {
                v5Template.setSamplingRate(samRate);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
*/
        // ��������ֱ�ӵõ�template���������ȡ
        if (com.calix.cnap.ipfix.jnca.cai.utils.Params.template_refreshFromHD) {
            File tpPath = new File(TemplateIPFix9.templatePath);
            if (tpPath.exists() && tpPath.isDirectory()) {
                String[] fileNames = tpPath.list();
                for (int idx = 0; idx < fileNames.length; idx++) {
                    Template t;
                    try {
                        if (fileNames[idx].indexOf(v5FileName) == -1) {
                            t = new TemplateIPFix9(fileNames[idx]);
                            int samRate = resources.integer(t.getRouterIp());
                            if (samRate != 0) {
                                t.setSamplingRate(samRate);
                            }
                            templates.put(t.getRouterIp() + t.getTemplateId(),
                                          t);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // ��Ӱ����һ��template
                    }
                }
            } /*else {
                System.err.println("ϵͳ�⵽�ƻ�");
            }*/
        }
    }

    private static TemplateManager mgr = new TemplateManager();

    Hashtable templates = new Hashtable();

    public static synchronized TemplateManager getTemplateManager() {
        return mgr;
    }

    public synchronized boolean acceptTemplate(String routerIp, byte[] content,
                                               int offset,int version) throws Exception {
        Exception ex = null;
        if (offset > 3) {
            Template t = null;
            try{
        	if (version == 9)
        	    t = new TemplateIPFix9(routerIp, content, offset);
        	else if (version == 10)
        	    t = new TemplateIPFix10(routerIp, content, offset);
            }catch(Exception ex2){
                ex=ex2;
                log.error("Exception while constructing template "+ ex2  );
            }
            int samRate = resources.integer(t.getRouterIp());
            if (samRate != 0) {
                t.setSamplingRate(samRate);
            }
            String key = t.getRouterIp() + t.getTemplateId();
            log.info("Registering template "+key);
            templates.put(key, t);
            if (ex!=null){
                throw ex;
            }
            return true;
        }
        return false;
    }

    public synchronized Template getTemplate(String routerIp, int templateId) {
        
        String key = routerIp + templateId;
        log.trace("Retreiving template {}", key);

        return (Template) templates.get(routerIp + templateId);
    }

    /**
     * @return Returns the v5Template.
     */
    public Template getV5Template() {
        return v5Template;
    }

    /**
     * @param template
     *            The v5Template to set.
     */
    public void setV5Template(Template template) {
        v5Template = template;
    }
}



