package com.calix.cnap.ipfix.jnca.cai.flow.collector;

import java.io.UnsupportedEncodingException;

import com.calix.cnap.ipfix.jnca.cai.flow.packets.v9.TemplateManager;
import com.calix.cnap.ipfix.jnca.cai.sql.SQL;
import com.calix.cnap.ipfix.jnca.cai.utils.DoneException;
import com.calix.cnap.ipfix.jnca.cai.utils.Params;
import com.calix.cnap.ipfix.jnca.cai.utils.Syslog;

public class Run {
    static {
        try {
            Class.forName("com.calix.cnap.ipfix.jnca.cai.flow.collector.Collector");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void go(String args[]) {
        boolean run_collector = true;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("create_db")) {
                new SQL().create_DB();
                run_collector = false;
            } else if (args[i].equals("remove_db")) {
                new SQL().delete_DB();
                run_collector = false;
            } else if (args[i].startsWith("encoding=")) {
                Params.encoding = args[i].substring(9);

                try {
                    String test = "eeeeeee";
                    test.getBytes(Params.encoding);
                } catch (UnsupportedEncodingException e) {
                    System.err.println("RUN: Unsupported encoding: "
                            + Params.encoding);
                    System.exit(0);
                }
            } else {
                System.err.println("RUN: Unknown argument -- " + args[i]);
                run_collector = false;
            }
        }

        if (run_collector) {
            TemplateManager.getTemplateManager();
            new Collector().go();
        }
    }
    public static Thread runInThread()
    {
        class Runner extends Thread {
            public void run()
            {
                TemplateManager.getTemplateManager();
                new Collector().go();
            }
        }
        Thread r = new Runner();
        r.start();
        return r;
    }

    public static void main(String args[]) throws Throwable {
        try {
            go(args);
        } catch (DoneException e) {
            if (Syslog.log != null) {
                Syslog.log.print_exception(e);
            } else {
                System.err.println("Run error - " + e.toString());
            }
        } catch (Throwable e) {
            if (Syslog.log != null) {
                Syslog.log.print_exception(e);
            } else {
                throw e;
            }
        }

    }
}


