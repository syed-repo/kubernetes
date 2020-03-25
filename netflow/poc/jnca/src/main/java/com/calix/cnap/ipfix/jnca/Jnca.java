/*******************************************************************************
 *                         /  __ \     | (_)                                    *
 *                         | /  \/ __ _| |___  __                               *
 *                         | |    / _` | | \ \/ /                               *
 *                         | \__/\ (_| | | |>  <                                *
 *                          \____/\__,_|_|_/_/\_\ inc.                          *
 *                                                                              *
 ********************************************************************************
 *                                                                              *
 *                        copyright 2018 by Calix, Inc.                        *
 *                              Petaluma, CA                                    *
 *                                                                              *
 *******************************************************************************/
package com.calix.cnap.ipfix.jnca;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.calix.cnap.ipfix.api.IpfixIeIdService;
import com.calix.cnap.ipfix.jnca.cai.flow.collector.Collector;
/**
 * Wrapper for the opensource library.
 *
 * It bootstraps and runs the opensource jnca ipfix collector.
 * It also exposes methods through which the dependencies exposed
 * by other bundles are made available to the rest of the jnca library.
 */
@Component(immediate = true)
public class Jnca {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private Thread theRunner;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    private IpfixIeIdService ipfixIEIdService;

    private static Jnca jncaRef;

    /**
     * Hook for the start of the component.
     */
    @Activate
    protected void activate() {

        // update the static reference
        jncaRef = this;

        log.info("Starting");
        startCollector();
    }

    /**
     * Hook for the stop of the component.
     */
    @Deactivate
    protected void deactivate() {
        log.info("Stopping ");
        Collector.closeCollector();
        theRunner.stop();
        log.info("Stopped");
    }

    /**
     * Returns the IE ID mgr received as reference from other bundles.
     *
     * @return the IpfixIeIdService
     */
    public static IpfixIeIdService getIpfixIeIdService() {
        return jncaRef.ipfixIEIdService;
    }


    /**
     * Waits for the main thread of this componenet to complete.
     */
    protected void waitForThread() {
        try {
            theRunner.join();
        } catch (java.lang.InterruptedException e) {
            log.error("caught exception..InterruptedException ", e);
        }
    }

    /**
     * Starts the collector

     * It forms the argument needed to start the collector Invokes the runInthread
     * method to start the collector in another thread Stores the Thread object for
     * the purpose of joining/stopping.
     */
    protected void startCollector() {
        try {
            Class.forName("com.calix.cnap.ipfix.jnca.cai.flow.collector.Collector");
            String[] ar = new String[1];
            ar[0] = new String("encoding=utf-8");
            log.info("Running collector main.. ");
            this.theRunner = com.calix.cnap.ipfix.jnca.cai.flow.collector.Run.runInThread();
            log.info("Started running in thread..");
        } catch (java.lang.Throwable e) {

            log.error("Ignoring Exception..", e);
        }
        log.debug("out of startCollector..");
    }
}

