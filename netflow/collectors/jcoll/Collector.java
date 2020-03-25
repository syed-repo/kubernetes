package com.calix.cnap.ipfix.jnca.cai.flow.collector;


import java.io.IOException;

import java.net.*;
import java.util.*;

import com.calix.cnap.ipfix.jnca.cai.flow.collector.interpretator.IpSegmentManager;
import com.calix.cnap.ipfix.jnca.cai.flow.packets.*;
import com.calix.cnap.ipfix.jnca.cai.utils.DoneException;
import com.calix.cnap.ipfix.jnca.cai.utils.Params;
import com.calix.cnap.ipfix.jnca.cai.utils.Resources;
import com.calix.cnap.ipfix.jnca.cai.utils.ServiceThread;
import com.calix.cnap.ipfix.jnca.cai.utils.SuperString;
import com.calix.cnap.ipfix.jnca.cai.utils.Syslog;
import com.calix.cnap.ipfix.jnca.cai.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Instant;


public class Collector
{

	private static final Logger log = LoggerFactory.getLogger(Collector.class);
    static Resources resources;

    int record_count = 0;

    static InetAddress localHost;

    static int localPort;

    static int receiveBufferSize;

    static boolean[] isVersionEnabled;

    static int max_queue_length;

    static int collector_thread;

    static final int MAX_VERION = 10;

    static Hashtable routers;

    static ServiceThread acceptorThread = null;
    static List<ServiceThread> readerThreads = new LinkedList<>();

    static
    {
	resources = new Resources("NetFlow");
	IpSegmentManager.getInstance();
	receiveBufferSize = resources.integer("net.receive.buffer.size");
	localPort = resources.integer("net.bind.port");

	String local = resources.get("net.bind.host");
	Params.v9TemplateOverwrite = resources.isTrue("flow.collector.V9.template.overwrite");

	Params.template_refreshFromHD = resources.isTrue("flow.collector.template.refreshFromHD");
	Params.ip2ipsConvert = resources.isTrue("flow.ip2ipsConvert");
	String ipSrcEx = resources.getAndTrim("ip.source.excludes");
	String ipSrcIn = resources.getAndTrim("ip.source.includes");
	StringTokenizer tknz = new StringTokenizer(ipSrcEx, ",");
	Params.ipSrcExcludes = new long[tknz.countTokens()];
	int idxOfEx = 0;
	while (tknz.hasMoreElements())
	{
	    long tmpl = Util.convertIPS2Long(tknz.nextToken());
	    Params.ipSrcExcludes[idxOfEx++] = tmpl;// ע��,�Ƿ���ַ�Ὣ���е�ַ��--0.0.0.0
	}
	tknz = new StringTokenizer(ipSrcIn, ",");
	Params.ipSrcIncludes = new long[tknz.countTokens()];
	int idxOfIn = 0;
	while (tknz.hasMoreElements())
	{
	    long tmpl = Util.convertIPS2Long(tknz.nextToken());
	    Params.ipSrcIncludes[idxOfIn++] = tmpl;// ע��,�Ƿ���ַ�Ὣ���е�ַ��--0.0.0.0
	}

	String ipDstEx = resources.getAndTrim("ip.dst.excludes");
	String ipDstIn = resources.getAndTrim("ip.dst.includes");
	tknz = new StringTokenizer(ipDstEx, ",");
	Params.ipDstExcludes = new long[tknz.countTokens()];
	idxOfEx = 0;
	while (tknz.hasMoreElements())
	{
	    long tmpl = Util.convertIPS2Long(tknz.nextToken());
	    Params.ipDstExcludes[idxOfEx++] = tmpl;// ע��,�Ƿ���ַ�Ὣ���е�ַ��--0.0.0.0
	}
	tknz = new StringTokenizer(ipDstIn, ",");
	Params.ipDstIncludes = new long[tknz.countTokens()];
	idxOfIn = 0;
	while (tknz.hasMoreElements())
	{
	    long tmpl = Util.convertIPS2Long(tknz.nextToken());
	    Params.ipDstIncludes[idxOfIn++] = tmpl;// ע��,�Ƿ���ַ�Ὣ���е�ַ��--0.0.0.0
	}

	if (local.equals("any"))
	    localHost = null;
	else
	{
	    try
	    {
		localHost = InetAddress.getByName(local);
	    } catch (UnknownHostException e)
	    {
		localHost = null;
	    }

	    if (localHost == null)
		resources.error("unknown host `" + local + "'");
	}

	isVersionEnabled = new boolean[MAX_VERION];
	isVersionEnabled[0] = resources.isTrue("flow.collector.V1.enabled");
	isVersionEnabled[1] = false;
	isVersionEnabled[2] = false;
	isVersionEnabled[3] = false;
	isVersionEnabled[4] = resources.isTrue("flow.collector.V5.enabled");
	isVersionEnabled[5] = false;
	isVersionEnabled[6] = resources.isTrue("flow.collector.V7.enabled");
	isVersionEnabled[7] = resources.isTrue("flow.collector.V8.enabled");
	isVersionEnabled[8] = resources.isTrue("flow.collector.V9.enabled");
	isVersionEnabled[9] = resources.isTrue("flow.collector.V10.enabled");

	max_queue_length = resources.integer("flow.collector.max_queue_length");
	collector_thread = resources.integer("flow.collector.collector.thread");

	if (collector_thread < 1)
	    resources.error("key `" + collector_thread + "' bust be great one");

	routers = new Hashtable();

	ResourceBundle bundle = resources.getResourceBundle();
	String prefix = "flow.collector.router.group.";
	int prefix_len = prefix.length();

	for (Enumeration e = bundle.getKeys(); e.hasMoreElements();)
	{
	    String entry = (String) e.nextElement();

	    if (!entry.startsWith(prefix))
		continue;

	    InetAddress router_group = null;
	    boolean putted = false;

	    try
	    {
		router_group = InetAddress.getByName(entry.substring(prefix_len));
	    } catch (UnknownHostException e1)
	    {
		resources.error("unknown host `" + entry.substring(prefix_len) + "' in `" + entry + "'");
	    }

	    String the_routers = (String) bundle.getString(entry);

	    for (StringTokenizer st = new StringTokenizer(the_routers); st.hasMoreElements();)
	    {
		String router_name = st.nextToken();
		InetAddress router = null;

		try
		{
		    router = InetAddress.getByName(router_name);
		} catch (UnknownHostException e2)
		{
		    resources.error("unknown host `" + router_name + "' in `" + entry + "'");
		}

		routers.put(router, router_group);
		putted = true;
	    }

	    if (!putted)
		resources.error("key `" + the_routers + "' -- no routers in group");
	}
    }

    Syslog syslog;

    LinkedList data_queue;

    Aggregate aggregator;

    long queued = 0, processed = 0;

    int sampleRate = 1;

    int stat_interval;
    Instant instant = Instant.now();
    long sttime = instant.getEpochSecond();

    public Collector()
    {
	sampleRate = resources.integer("sample.rate");
	if (sampleRate == 0)
	{
	    sampleRate = 1;
	}
	byte logLevel = Syslog.translatePriority(resources.get("flow.collector.syslog.level"));
	byte logOptions = Syslog.translateOptions(resources.get("flow.collector.syslog.options"));
	short logFacility = Syslog.translateFacility(resources.get("flow.collector.syslog.facility"));

	stat_interval = resources.getInterval("flow.collector.statistics.interval");

	if (logLevel == Syslog.LOG_ILLEGAL_P)
	    resources.error("illegal flow.collector.syslog.level value");

	if (logOptions == Syslog.LOG_ILLEGAL_O)
	    resources.error("illegal flow.collector.syslog.options value");

	if (logFacility == Syslog.LOG_ILLEGAL_F)
	    resources.error("illegal flow.collector.syslog.facility value");

	syslog = new Syslog("NetFlow", logOptions, logFacility);
	syslog.setlogmask(Syslog.LOG_UPTO(logLevel));
	syslog.syslog(Syslog.LOG_DEBUG, "Syslog created: " + syslog.toString());

	aggregator = new Aggregate(resources);// ���еĹ鲢�̺߳�SQL
	data_queue = new LinkedList();
    }

    public static void closeAcceptor() {

    	if(acceptorThread == null) {
    		return;
		}
		log.info("closing acceptor..");
    	acceptorThread.stopService();
    	acceptorThread = null;
	}
	public static void closeReaders() {
    	log.info("closing readers..");
		for(ServiceThread t : readerThreads) {
			t.stopService();
		}
		readerThreads.clear();
		log.info("closed readers..");
	}

	public static void closeCollector() {
    	closeAcceptor();
    	closeReaders();
	}

    /**
     * 
     *
     */
    void go()
    {
    	closeAcceptor();
	/**
	 *
	 */
	System.out.println("collector go function" + localHost + localPort);
	ServiceThread rdr = new ServiceThread(this, syslog,
		"acceptor at " + (localHost == null ? "any" : "" + localHost) + ":" + localPort, "Reader") {
		DatagramSocket acceptorSocket;
		public void exec() throws Throwable
	    {
	    	//acceptorSocket = new DatagramSocket(localPort);
		((Collector) o).reader_loop();
	    }

		public void stopService() {

			if (this.acceptorSocket == null) {
				return;
			}
			try {
				this.acceptorSocket.close();
			} catch (Exception e) {
				log.error("Exception closing acceptor.. ",e);
			}
			this.acceptorSocket = null;
			this.stop();
		}
	};
	acceptorThread = rdr;
	rdr.setPriority(Thread.MAX_PRIORITY);
	rdr.setDaemon(true);
	rdr.start();

	ServiceThread statistics;
	/**
	 * 
	 */
	if (stat_interval != 0)
	{
	    statistics = new ServiceThread(this, syslog, "Statistics over " + Util.toInterval(stat_interval),
		    "Statistics") {
		public void exec() throws Throwable
		{
		    ((Collector) o).statistics_loop();
		}
	    };

	    statistics.setDaemon(true);
	    statistics.start();
	}

	ServiceThread[] cols = new ServiceThread[collector_thread];

	for (int i = 0; i < collector_thread; i++)
	{
	    String title = new String("Collector #" + (i + 1));
	    ServiceThread col = new ServiceThread(this, syslog, title, title) {
		public void exec()
		{
		    ((Collector) o).collector_loop();
		}
	    };

	    cols[i] = col;
	    col.start();
	}

	try
	{
	    for (int i = 0; i < collector_thread; i++)
		cols[i].join();
	} catch (InterruptedException e)
	{
	    syslog.syslog(Syslog.LOG_CRIT, "Collector - InterruptedException in main thread, exit");
	}
    }

    /**
     * 
     *
     * @throws Throwable
     */
    public void statistics_loop() throws Throwable
    {
	long start = System.currentTimeMillis();

	while (true)
	{
	    try
	    {
		Thread.sleep(stat_interval * 1000);
	    } catch (InterruptedException e)
	    {
	    }

	    long u = System.currentTimeMillis() - start;
	    String s = "" + ((float) queued * 1000 / u);
	    int i = s.indexOf('.') + 3;

	    if (i < s.length())
		s = s.substring(0, i);

	    /*syslog.syslog(Syslog.LOG_DEBUG,
		    "Pkts " + queued + "/" + processed + ", " + s + " pkts/sec, " + Util.uptime_short(u / 1000));*/
		log.debug("Pkts queued/processed {}/{}, {} pkts/sec, uptime {}",
				  queued, processed, s, Util.uptime_short(u / 1000));
	}
    }

    // ������ʵ��
    // static DatagramPacket tmpPacket = null;
    // ������ʵ��
    SampleManager sampler = null;
    {
	sampler = new SampleManager(sampleRate);
    }


	public void reader_loop() throws Throwable {
		System.out.println("Reader loop: ");
		DatagramSocket socket;

		try {
			try {
				System.out.println("before receive packet");
				socket = new DatagramSocket(localPort, localHost);
				socket.setReceiveBufferSize(receiveBufferSize);
				System.out.println("after receive packet");
			} catch (IOException exc) {
				syslog.syslog(Syslog.LOG_CRIT, "Reader - socket set receiv buffer: " + localHost + " - "
						+ SuperString.exceptionMsg(exc.toString()));
				throw exc;
			}

			while (true) {
				byte[] buf = new byte[2048];// 效率在这个地方可以提高
				DatagramPacket p = null;
				if (p == null) {
					p = new DatagramPacket(buf, buf.length);

					try {
						socket.receive(p);
						log.trace("Received a packet on port {} and IP {}", p.getPort(), p.getAddress());

					} catch (IOException exc) {
						syslog.syslog(Syslog.LOG_CRIT,
								"Reader - socket read error: "
										+ SuperString.exceptionMsg(exc
												.toString()));
						exc.printStackTrace();
						put_to_queue(null);// 表示notifyAll
						break;
					}
				}
				if (this.sampler.shouldDue()) {
					System.out.println("Putting into the queue");
					put_to_queue(p);
				}
				p = null;
			}
		} catch (Throwable e) {
			syslog.syslog(Syslog.LOG_CRIT, "Reader: exception, trying to abort collector");
			log.error("Reader: exception, trying to abort collector :", e);
			put_to_queue(null);
			throw e;
		}
	}

    /**
     * UDP���Ļ���
     *
     * @param p
     */
    void put_to_queue(final DatagramPacket p)
    {
		if (p == null) {
			log.info("null packet received in put_to_queue.. notifying all ");
			synchronized (data_queue) {
				data_queue.addLast(p);
				queued++;
				data_queue.notifyAll();// ��������ˣ���ô
			}
			return;
		}
			InetAddress router = p.getAddress();
	InetAddress group = (InetAddress) routers.get(router);

	if (group == null)
	{
	    syslog.syslog(Syslog.LOG_ERR, "A packet from an unconfigured router " + router + " is received.. adding..");
	    routers.put(router,router);
	    group = router;
	}

	syslog.syslog(Syslog.LOG_DEBUG, "Packet from " + router + " is moved to group " + group);
	p.setAddress(group);// ����ʵrouter�ĵ�ַ�ĳ�group�ĵ�ַ

	if (data_queue.size() > max_queue_length)
	    syslog.syslog(Syslog.LOG_WARNING,
		    "Reader - the queue is bigger then max_queue_length " + data_queue.size() + "/" + max_queue_length);

	synchronized (data_queue)
	{
	    data_queue.addLast(p);
	    queued++;

	    if (p == null)
		data_queue.notifyAll();// ��������ˣ���ô
	    else
		data_queue.notify();// ����
	}
    }

    /**
     * �ڶ�ɼ��̵߳�������
     *
     */
    void collector_loop()
    {
	boolean no_data = true;

	while (true)
	{
	    Object p = null;

	    synchronized (data_queue)
	    {
		try
		{
		    p = data_queue.removeFirst();// ȡ����һ��UDP��
		    System.out.println("received a packet in collector loop");

		    no_data = false;
		} catch (NoSuchElementException ex)
		{
		}
	    }

	    if (no_data)
	    {
		synchronized (data_queue)
		{
		    try
		    {
			data_queue.wait();// �ȴ���reader_loop notify
		    } catch (InterruptedException e)
		    {
		    }
		}
	    } else
	    {
		no_data = true;

		if (p != null) {
                    processPacket((DatagramPacket) p);
                }
	    }
	}
    }

	/**
	 * Process received packet from the data_queue
	 * @param p - received datagram packet
	 */
	private synchronized void processPacket(final DatagramPacket p) {
		System.out.println("Processing processPacket function");

		final byte[] buf = p.getData();
		int len = p.getLength();
		String addr = p.getAddress().getHostAddress().trim();
		boolean need_log = syslog.need(Syslog.LOG_INFO);

		synchronized (data_queue) {
			processed++;
		}

		if (need_log)
			syslog.syslog(Syslog.LOG_INFO, addr + "(" + p.getAddress().getHostName() + ") " + len + " bytes");
		log.trace("{} ({}) {} bytes", addr, p.getAddress().getHostName(), len);

		try {
			if (len < 2)
				throw new DoneException("  * too short packet *");

			short version = (short) Util.to_number(buf, 0, 2);
			long exportTime = Util.to_number(buf, 4, 4);
			log.trace("Processing a datagram packet with Export Time: {}", exportTime);

			long epochTime = Util.to_number(buf, 8, 4);
			long seq = 0;
			log.trace("Processing a datagram packet with Epoch Time: {}", epochTime);

			if (version > MAX_VERION || version <= 0)
				throw new DoneException("  * unsupported version * " + (version));

			if (!isVersionEnabled[version - 1])
				throw new DoneException("  * version " + version + " disabled *");

			if (need_log)
				syslog.syslog(Syslog.LOG_INFO, "  version: " + version);

			FlowPacket packet = null;

			switch (version) {
				case 1:
					packet = (FlowPacket) new V1_Packet(addr, buf, len);
					break;
				case 5:
					seq = Util.to_number(buf, 16, 4);
					packet = (FlowPacket) new V5_Packet(addr, buf, len,  epochTime);
					break;
				case 7:
					packet = (FlowPacket) new V7_Packet(addr, resources, buf, len);
					break;
				case 8:
					packet = (FlowPacket) new V8_Packet(addr, buf, len);
					break;
				case 9:
					seq = Util.to_number(buf, 12, 4);
					packet = (FlowPacket) new V9_Packet(addr, buf, len,  epochTime);
					break;
				case 10:
					packet = (FlowPacket) new V10_Packet(addr, buf, len);
					break;

				default:

					syslog.syslog(Syslog.LOG_CRIT, "Collector - BUG: Version problem, version=" + version);
					return;
			}
                        System.out.println(packet.getFlows().size());
			Iterator i = packet.getFlows().iterator();
                        while (i.hasNext()) {
                            System.out.println("VFLOW5" + i.next());
                        }
			record_count = record_count + packet.getFlows().size();
			System.out.println(record_count);
			if (record_count >= 100000) {
				Instant instant = Instant.now();
                                long edtime = instant.getEpochSecond();
				long difftime = edtime -sttime;
				System.out.println("Time take for 100K records" + difftime);
				System.exit(0);

			}
			//aggregator.process(packet);
		} catch (DoneException e) {
			e.printStackTrace();
			if (need_log)
				syslog.syslog(Syslog.LOG_INFO, e.toString());
		}
	}
}



