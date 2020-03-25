/**
 *
 */
package com.calix.cnap.ipfix.jnca.cai.flow.packets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;


import com.calix.cnap.ipfix.jnca.cai.flow.packets.v9.OptionFlow;
import com.calix.cnap.ipfix.jnca.cai.flow.packets.v9.TemplateIPFix10;
import com.calix.cnap.ipfix.jnca.cai.flow.packets.v9.TemplateManager;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Scheme_DataPrefix;
import com.calix.cnap.ipfix.jnca.cai.sql.SQL;
import com.calix.cnap.ipfix.jnca.cai.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  
 * 
 * <p>
 * -------*---------------*------------------------------------------------------*
 * | Bytes | Contents | Description |
 * -------*---------------*------------------------------------------------------*
 * | 0-1 | version | The version of NetFlow records exported 009 |
 * -------*---------------*------------------------------------------------------*
 * | 2-3 | len | Length of the packet |
 * -------*---------------*------------------------------------------------------*
 * | 4-7 | SysUptime | Current time in milliseconds since the export
 * device | | | | booted |
 * -------*---------------*------------------------------------------------------*
 * | 8-11 | PackageSequence| pkt id of all flows * |
 * -------*---------------*------------------------------------------------------*
 * | 12-15 | Observation Domain id
 * -------*---------------*-----***-------------------------------------------------*
 */
public class V10_Packet implements FlowPacket {
    long length;
    private final Logger log = LoggerFactory.getLogger(getClass());
    String routerIP;

    long SysUptime, packageSequence;
    long exportTime;
    long unix_secs;
    int count;
    long sourceId;
    
    Vector flows = null;

    Vector optionFlows = null;

    public static final int V10_Header_Size = 16;

    public class IEds {
        public static final int HOSTNAME = 1;
        public static final int SHELF = 2;
        public static final int SLOT = 3;
        public static final int PORT = 4;
        public static final int OLT_PON_UTIL_AID = 103;
        public static final int SAMPLE_TIME = 10;
        public static final int ADMITTED_GUARANTEED_UPSTREAM_BW = 210;
        public static final int AVAILABLE_GUARANTEED_UPSTREAM_BW = 211;
        public static final int ADMITTED_BEST_EFFORT_UPSTREAM_BW = 212;
        public static final int AVAILABLE_BEST_EFFORT_UPSTREAM_BW = 213;
        public static final int UPSTREAM_OCTETS = 208;
        public static final int DOWNSTREAM_OCTETS = 209;
    }

    ;

    /**
     * 
     *
     * @param RouterIP
     * @param buf
     * @param len
     * @throws DoneException
     */
    public V10_Packet(String RouterIP, byte[] buf, int len) throws DoneException {
        if (Params.DEBUG) {
//            File tmpFile = new File("D:\\Dev\\netflow\\jnca\\savePacketT_211.98.0.147_256.cache.tmp");
            File tmpFile = new File("D:\\Dev\\netflow\\jnca\\savePacketT_211.98.0.147_256.cache.tmp");
            if (tmpFile.exists()) {
                try {
                    ObjectInputStream fIn = new ObjectInputStream(new FileInputStream(tmpFile));
                    System.out.println("Directly read from " + fIn);
                    try {
                        buf = (byte[]) fIn.readObject();
                        len = ((Integer) fIn.readObject()).intValue();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    fIn.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    ObjectOutputStream fOut;
                    fOut = new ObjectOutputStream(new FileOutputStream(tmpFile));
                    fOut.writeObject(buf);
                    fOut.writeObject(new Integer(len));
                    fOut.flush();
                    fOut.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            
        }
        if (len < V10_Header_Size) {
            throw new DoneException("    * incomplete header *");
        }

        this.routerIP = RouterIP;
        length = Util.to_number(buf, 2, 2);

        /**
         * In the V10 (ipfix) header has the exportTime at the offset 4.
         * And the System uptime sent at the location has been removed.
         * TODO Retaining the SysUptime so that it can be cleanedup during cleanup task
         */
        exportTime = Util.to_number(buf, 4, 4);
        SysUptime = exportTime;
        Variation vrat = Variation.getInstance();
        vrat.setVary(Util.convertIPS2Long(RouterIP), SysUptime);
        packageSequence = Util.to_number(buf, 8, 4);
        sourceId = Util.to_number(buf, 12, 4);

        flows = new Vector(); // Let's first make some space
        optionFlows = new Vector();
        
        long flowsetLength = 0l;
        log.trace("Need to parse the packet..");

        int packetOffset = V10_Header_Size;

        while (true) {
            if (length <= packetOffset)
                break;

            int flowsetId = (int) Util.to_number(buf, packetOffset, 2);
            flowsetLength = Util.to_number(buf, packetOffset + 2, 2);

            log.trace("Need to parse flowsetId = {}  flowset Length {} ", flowsetId, flowsetLength);
            // now flowset is with me
            if (flowsetId == 2) { // ipfix id = 2 is the template
                int thisTemplateOffset = packetOffset + 4;
                do {
                    //TODO : check if this loop is really needed.. each flowset will
                    // have only one template.
                    long templateId = Util.to_number(buf, thisTemplateOffset, 2);
                    long fieldCount = Util.to_number(buf, thisTemplateOffset + 2, 2);
                    if (TemplateManager.getTemplateManager().getTemplate(this.routerIP, (int) templateId) == null
                            || Params.v9TemplateOverwrite) {
                        try {
                            TemplateManager.getTemplateManager().acceptTemplate(this.routerIP, buf, thisTemplateOffset, 10);
                        } catch (Exception e) {
                            if (Params.DEBUG) {
                                e.printStackTrace();
                            }
                        }
                        log.debug("Added template to the manager..{}", templateId);
                    }
                    //TODO check if there is a possible
                    // Each field is defined by 4 bytes and 2 bytes each for flow id and field_count
                    thisTemplateOffset += fieldCount * 8 + 4;
                } while (thisTemplateOffset - packetOffset < flowsetLength);
            } else if (flowsetId > 255) {
                try {
                    this.parseDataFlowSet(RouterIP, buf, packetOffset, flowsetId);
                } catch (Exception e) {
                    log.error("Failed to parse data flow set {}", flowsetId);
                    e.printStackTrace();
                }
            }
            // below is for buffer maintenance
            packetOffset += flowsetLength;
        }

    }
    
    protected void parseDataFlowSet(String RouterIP, byte[] buf, int offset, int tid) throws Exception {

        long flowsetId = Util.to_number(buf, offset, 2);
        long flowsetLength = Util.to_number(buf, offset + 2, 2);

        long end = offset + flowsetLength;

        //TODO get/check the version before typecasting to version 10
        TemplateIPFix10 t = (TemplateIPFix10) TemplateManager.getTemplateManager().getTemplate(RouterIP, (int) flowsetId);

        if (t == null) {
            throw new Exception("Failed to get flowset template " + Long.toString(flowsetId));
        }

        int loc = offset + 4;
        String key = "";
        while (loc < end) {
            log.trace("\nFlowset Start ---->");
            long val = 0;
            String str = "";
            for (int i = 0; i < t.getFieldCount(); i++) {
                long l = t.getFieldLength(i);
                if (l == 65535) {
                    l = Util.to_number(buf, loc, 1);
                    loc += 1;
                    log.trace("Reading a Variable sized field.. len={} field type={}", l, t.getFieldType(i));
                    byte[] subArray = Arrays.copyOfRange(buf, (int) loc, (int) loc + (int) l);
                    str = new String(subArray);
                    loc += l;

                    log.trace("Field : {} Value= {}", t.getFieldType(i), str);
                    
                } else {
                    val = Util.to_number(buf, loc, (int) l);

                    log.trace("Reading a Fixed sized field.. len={} field type={}", l, t.getFieldType(i));

                    loc += l;
                    log.trace("Field : {}, Value = {}",t.getFieldType(i), val);
                }
            }

            log.trace("Flowset End <----\n");
        }

    }

    protected static String add_raw_sql = null;

    @Override
    public Vector getFlows() {
        return flows;
    }

    @Override


    public void process_raw(SQL sql) {
        if (add_raw_sql == null) {
            add_raw_sql = SQL.resources.getAndTrim("SQL.Add.RawV9");
        }

        for (Enumeration flowenum = flows.elements(); flowenum.hasMoreElements(); ) {
            ((V5_Flow) flowenum.nextElement()).save_raw4v9(SysUptime, unix_secs, packageSequence, sourceId,
                    sql.prepareStatement("Prepare INSERT to V9 raw table", add_raw_sql));
        }
        for (Enumeration oflowenum = optionFlows.elements(); oflowenum.hasMoreElements(); ) {
            ((OptionFlow) oflowenum.nextElement()).save_raw(SysUptime, unix_secs, packageSequence, sourceId,
                    sql.prepareStatement("Prepare INSERT to Option table",
                            SQL.resources.getAndTrim("SQL.Add.OptionsTable")));
        }
    }

    public Vector getSrcASVector() {
        Vector v = new Vector((int) count, (int) count);

        for (Enumeration flowenum = flows.elements(); flowenum.hasMoreElements(); ) {
            v.add(((V5_Flow) flowenum.nextElement()).getDataSrcAS());
        }

        return v;
    }

    public Vector getDstASVector() {
        Vector v = new Vector((int) count, (int) count);

        for (Enumeration flowenum = flows.elements(); flowenum.hasMoreElements(); ) {
            v.add(((V5_Flow) flowenum.nextElement()).getDataDstAS());
        }

        return v;
    }

    public Vector getASMatrixVector() {
        Vector v = new Vector((int) count, (int) count);

        for (Enumeration flowenum = flows.elements(); flowenum.hasMoreElements(); ) {
            v.add(((V5_Flow) flowenum.nextElement()).getDataASMatrix());
        }

        return v;
    }

    public Vector getSrcNodeVector() {
        Vector v = new Vector((int) count, (int) count);

        for (Enumeration flowenum = flows.elements(); flowenum.hasMoreElements(); ) {
            v.add(((V5_Flow) flowenum.nextElement()).getDataSrcNode());
        }

        return v;
    }

    public Vector getDstNodeVector() {
        Vector v = new Vector((int) count, (int) count);

        for (Enumeration flowenum = flows.elements(); flowenum.hasMoreElements(); ) {
            v.add(((V5_Flow) flowenum.nextElement()).getDataDstNode());
        }

        return v;
    }

    public Vector getHostMatrixVector() {
        Vector v = new Vector((int) count, (int) count);

        for (Enumeration flowenum = flows.elements(); flowenum.hasMoreElements(); ) {
            v.add(((V5_Flow) flowenum.nextElement()).getDataHostMatrix());
        }

        return v;
    }

    public Vector getSrcInterfaceVector() {
        Vector v = new Vector((int) count, (int) count);

        for (Enumeration flowenum = flows.elements(); flowenum.hasMoreElements(); ) {
            v.add(((V5_Flow) flowenum.nextElement()).getDataSrcInterface());
        }

        return v;
    }

    public Vector getDstInterfaceVector() {
        Vector v = new Vector((int) count, (int) count);

        for (Enumeration flowenum = flows.elements(); flowenum.hasMoreElements(); ) {
            v.add(((V5_Flow) flowenum.nextElement()).getDataDstInterface());
        }

        return v;
    }

    public Vector getInterfaceMatrixVector() {
        Vector v = new Vector((int) count, (int) count);

        for (Enumeration flowenum = flows.elements(); flowenum.hasMoreElements(); ) {
            v.add(((V5_Flow) flowenum.nextElement()).getDataInterfaceMatrix());
        }

        return v;
    }

    public Vector getSrcPrefixVector() {
        Vector v = new Vector((int) count, (int) count);

        for (Enumeration flowenum = flows.elements(); flowenum.hasMoreElements(); ) {
            Scheme_DataPrefix pfx = ((V5_Flow) flowenum.nextElement()).getDataSrcPrefix();
            if (pfx != null) {
                v.add(pfx);
            }
        }

        return v;
    }

    public Vector getDstPrefixVector() {
        Vector v = new Vector((int) count, (int) count);

        for (Enumeration flowenum = flows.elements(); flowenum.hasMoreElements(); ) {
            Scheme_DataPrefix dpfx = ((V5_Flow) flowenum.nextElement()).getDataDstPrefix();
            if (dpfx != null) {
                v.add(dpfx);
            }
        }
        return v;
    }

    public Vector getPrefixMatrixVector() {
        Vector v = new Vector((int) count, (int) count);

        for (Enumeration flowenum = flows.elements(); flowenum.hasMoreElements(); ) {
            v.add(((V5_Flow) flowenum.nextElement()).getDataPrefixMatrix());
        }

        return v;
    }

    public Vector getProtocolVector() {
        Vector v = new Vector((int) count, (int) count);

        for (Enumeration flowenum = flows.elements(); flowenum.hasMoreElements(); ) {
            v.add(((V5_Flow) flowenum.nextElement()).getDataProtocol());
        }

        return v;
    }
}




