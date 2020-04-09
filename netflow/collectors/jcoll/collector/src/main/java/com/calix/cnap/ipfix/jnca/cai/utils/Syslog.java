/*
 * @(#)Syslog.java	1.1 01/01/04
 *
 * Copyright 2000-2001 Cai Mao & cai. All rights reserved.
 *
 * This software is absolutely written by com.calix.cnap.ipfix.jnca.cai.ao. You may use, distribute
 * and modify it as you wish, but you MUSTN'T remove copyright
 * information. Kinda relax.
 *
 */

package com.calix.cnap.ipfix.jnca.cai.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

/**
 * 
 * The Syslog class is a BSD-like Syslog implementation. This implementation
 * uses only UDP for logging.
 * 
 * @author Cai Mao <swingler@126.com>
 * @version 1.1, 04/01/01
 * 
 */

public class Syslog extends OutputStream {
	// Facility codes

	public static final short LOG_KERN = (0 << 3); // kernel messages

	public static final short LOG_USER = (1 << 3); // random user-level
													// messages

	public static final short LOG_MAIL = (2 << 3); // mail system

	public static final short LOG_DAEMON = (3 << 3); // system daemons

	public static final short LOG_AUTH = (4 << 3); // security/authorization
													// messages

	public static final short LOG_SYSLOG = (5 << 3); // messages generated
														// internally

	// by syslogd
	public static final short LOG_LPR = (6 << 3); // line printer subsystem

	public static final short LOG_NEWS = (7 << 3); // netnews subsystem

	public static final short LOG_UUCP = (8 << 3); // uucp subsystem

	public static final short LOG_CRON = (15 << 3); // cron/at subsystem

	// other codes through 15 reserved for system use

	public static final short LOG_LOCAL0 = (16 << 3); // reserved for local
														// use

	public static final short LOG_LOCAL1 = (17 << 3); // reserved for local
														// use

	public static final short LOG_LOCAL2 = (18 << 3); // reserved for local
														// use

	public static final short LOG_LOCAL3 = (19 << 3); // reserved for local
														// use

	public static final short LOG_LOCAL4 = (20 << 3); // reserved for local
														// use

	public static final short LOG_LOCAL5 = (21 << 3); // reserved for local
														// use

	public static final short LOG_LOCAL6 = (22 << 3); // reserved for local
														// use

	public static final short LOG_LOCAL7 = (23 << 3); // reserved for local
														// use

	public static final short LOG_ILLEGAL_F = -1; // error returned by
													// translateFacility

	public static final short LOG_NFACILITIES = 24; // maximum number of
													// facilities

	public static final short LOG_FACMASK = 0x03f8; // mask to extract facility
													// part

	// Priorities (these are ordered)

	public static final byte LOG_EMERG = 0; // system is unusable

	public static final byte LOG_ALERT = 1; // action must be taken immediately

	public static final byte LOG_CRIT = 2; // critical conditions

	public static final byte LOG_ERR = 3; // error conditions

	public static final byte LOG_WARNING = 4; // warning conditions

	public static final byte LOG_NOTICE = 5; // normal but signification
												// condition

	public static final byte LOG_INFO = 6; // informational

	public static final byte LOG_DEBUG = 7; // debug-level messages

	public static final byte LOG_ILLEGAL_P = -1; // error returned by
													// translatePriority

	public static final byte LOG_PRIMASK = 0x07; // mask to extract priority

	// part (internal)

	// Option flags for openlog.
	//
	// LOG_ODELAY no longer does anything; LOG_NDELAY is the
	// inverse of what it used to be.
	//
	// LOG_NOWAIT does nothing

	public static final byte LOG_PID = 0x01; // log the pid with each message

	// SORRY, java version shows
	// only thread name there
	public static final byte LOG_CONS = 0x02; // log on the console if errors

	// in sending
	public static final byte LOG_ODELAY = 0x04; // delay open until syslog() is
												// called

	public static final byte LOG_NDELAY = 0x08; // don't delay open

	public static final byte LOG_NOWAIT = 0x10; // if forking to log on console,

	// don't wait()
	public static final byte LOG_PERROR = 0x20; // log to standard error output

	// as well to the system log

	public static final byte LOG_ILLEGAL_O = -1; // error returned by
													// translateOptions

	// -------------------------------------------------------------------------

	protected String log_host = "localhost";

	protected String ident = null;

	protected byte logopt = 0;

	protected short facility = LOG_USER;

	protected byte mask = LOG_PRIMASK;

	private int open_try = 0;

	private static final int MAX_TRY = 2;

	private Exception exception = null;

	protected String buf = new String();

	protected String file_name = null;

	protected DatagramSocket socket = null;

	public static Syslog log = null;

	// translators

	public static final short translateFacility(String facility) {
		if (facility.equals("KERN"))
			return LOG_KERN;
		if (facility.equals("USER"))
			return LOG_USER;
		if (facility.equals("MAIL"))
			return LOG_MAIL;
		if (facility.equals("DAEMON"))
			return LOG_DAEMON;
		if (facility.equals("AUTH"))
			return LOG_AUTH;
		if (facility.equals("SYSLOG"))
			return LOG_SYSLOG;
		if (facility.equals("LPR"))
			return LOG_LPR;
		if (facility.equals("NEWS"))
			return LOG_NEWS;
		if (facility.equals("UUCP"))
			return LOG_UUCP;
		if (facility.equals("CRON"))
			return LOG_CRON;
		if (facility.equals("LOCAL0"))
			return LOG_LOCAL0;
		if (facility.equals("LOCAL1"))
			return LOG_LOCAL1;
		if (facility.equals("LOCAL2"))
			return LOG_LOCAL2;
		if (facility.equals("LOCAL3"))
			return LOG_LOCAL3;
		if (facility.equals("LOCAL4"))
			return LOG_LOCAL4;
		if (facility.equals("LOCAL5"))
			return LOG_LOCAL5;
		if (facility.equals("LOCAL6"))
			return LOG_LOCAL6;
		if (facility.equals("LOCAL7"))
			return LOG_LOCAL7;

		return LOG_ILLEGAL_F;
	}

	public static final byte translatePriority(String priority) {
		if (priority.equals("EMERG"))
			return LOG_EMERG;
		if (priority.equals("ALERT"))
			return LOG_ALERT;
		if (priority.equals("CRIT"))
			return LOG_CRIT;
		if (priority.equals("ERR"))
			return LOG_ERR;
		if (priority.equals("WARNING"))
			return LOG_WARNING;
		if (priority.equals("NOTICE"))
			return LOG_NOTICE;
		if (priority.equals("INFO"))
			return LOG_INFO;
		if (priority.equals("DEBUG"))
			return LOG_DEBUG;

		return LOG_ILLEGAL_P;
	}

	public static final byte translateOptions(String options) {
		StringTokenizer st = new StringTokenizer(options, ",");
		byte ret = 0;

		for (int i = 0; st.hasMoreTokens(); i++) {
			String s = st.nextToken();

			if (s.equals("PID"))
				ret |= LOG_PID;
			else if (s.equals("CONS"))
				ret |= LOG_CONS;
			else if (s.equals("ODELAY"))
				ret |= LOG_ODELAY;
			else if (s.equals("NDELAY"))
				ret |= LOG_NDELAY;
			else if (s.equals("NOWAIT"))
				ret |= LOG_NOWAIT;
			else if (s.equals("PERROR"))
				ret |= LOG_PERROR;
			else
				return LOG_ILLEGAL_O;
		}

		return ret;
	}

	// arguments to setlogmask

	public static final short LOG_MASK(short pri) {
		return (short) (1 << pri);
	}

	public static final short LOG_UPTO(short pri) {
		return (short) ((1 << (pri + 1)) - 1);
	}

	public static final short LOG_PRI(short pri) {
		return (short) (pri & LOG_PRIMASK);
	}

	// constructors

	public Syslog(String ident, byte logopt, short facility) {
		this(ident, logopt, facility, null);
	}

	public Syslog(String ident, int logopt, short facility) {
		this(ident, (byte) logopt, facility, null);
	}

	public Syslog(String ident, int logopt, short facility, String log_host) {
		this(ident, (byte) logopt, facility, log_host);
	}

	public Syslog(String ident, byte logopt, short facility, String log_host) {
		this.ident = ident;
		this.logopt = logopt;

		if (log_host != null)
			this.log_host = log_host;

		if (facility != 0 && (facility & ~LOG_FACMASK) == 0)
			this.facility = facility;

		String sep = System.getProperty("file.separator");
		file_name = new String(sep.equals("/") ? "/dev/console" : "con");

		if ((logopt & LOG_NDELAY) == LOG_NDELAY)
			openlog();

		log = this;
	}

	// openlog() for internal usage

	private void openlog() {
		if (open_try < MAX_TRY) {
			++open_try;

			try {
				socket = new DatagramSocket();
				socket.connect(InetAddress.getByName(log_host), 514);
			} catch (SocketException e) {
				exception = e;
			} catch (UnknownHostException e) {
				exception = e;
			}
		}
	}

	// user interface functions

	public void closelog() {
		if (socket != null) {
			socket.close();
			socket = null;
		}
	}

	public byte setlogmask(short priority) {
		return setlogmask((byte) priority);
	}

	public byte setlogmask(int priority) {
		return setlogmask((byte) priority);
	}

	public byte setlogmask(byte priority) {
		byte old = mask;

		if (priority != 0)
			mask = priority;

		return old;
	}

	public byte getMask() {
		return mask;
	}

	public DatagramSocket getSocket() {
		return socket;
	}

	public Exception getException() {
		return exception;
	}

	// logging functions

	public void syslog(int priority, String par_msg) {
		syslog((short) priority, par_msg);
	}

	public boolean need(short priority) {
		return (LOG_MASK(LOG_PRI(priority)) & mask) != 0;
	}

	public void syslog(short priority, String par_msg) {
		synchronized (this) {
			if ((priority & ~(LOG_PRIMASK | LOG_FACMASK)) != 0) {
				syslog(LOG_ERR | LOG_CONS | LOG_PERROR | LOG_PID | LOG_USER,
						Thread.currentThread().getName()
								+ ": syslog: unknown facility/priority: "
								+ Integer.toHexString(priority));
				priority &= LOG_PRIMASK | LOG_FACMASK;
			}

			if (!need(priority))
				return;

			if ((priority & LOG_FACMASK) == 0)
				priority |= facility;

			String msg = new String();
			boolean any_add = false;

			if (ident != null) {
				msg += ident;
				any_add = true;
			}

			if ((logopt & LOG_PID) == LOG_PID) {
				msg += "[" + Thread.currentThread().getName() + "]";
				any_add = true;
			}

			if (any_add)
				msg += ": " + par_msg;
			else
				msg += par_msg;

			if ((logopt & LOG_PERROR) == LOG_PERROR)
				System.err.print(msg + "\r\n");

			if (socket == null)
				openlog();

			if (socket != null) {
				String msg1 = new String("<" + priority + ">" + msg);
				byte[] buf = msg1.getBytes();
				DatagramPacket p = new DatagramPacket(buf, buf.length);

				try {
					socket.send(p);
					return;
				} catch (IOException e) {
				}
			}

			if ((logopt & LOG_CONS) == LOG_CONS)
				try {
					PrintWriter file = new PrintWriter(new FileOutputStream(
							file_name));

					file.print(msg + "\r\n");
					file.close();
					file = null;
				} catch (FileNotFoundException e) {
				}
		}
	}

	// PrintWriter implementation

	public void write(int b) throws IOException {
		char ch = (char) (b & 0xff);

		switch (ch) {
		case '\n':
			flush();
			break;

		case '\r':
			break;

		case '\t':
			buf += ' ';
			break;

		default:
			buf += ch;
		}
	}

	public void close() throws IOException {
		flush();
	}

	protected void finalize() throws Throwable {
		flush();
		closelog();
	}

	public void flush() throws IOException {
		if (buf.length() != 0) {
			syslog(Syslog.LOG_CRIT, buf);
			buf = new String();
		}
	}

	// kewl exception tracer ;-)

	public void print_exception(Throwable e) {
		if (e instanceof DoneException) // Sorry, not for you
			syslog(Syslog.LOG_CRIT, "Abort: " + e.toString()); // just kill
																// this if()
		else // up to here
		{
			PrintWriter prt = new PrintWriter(this, true);
			String errmsg = new String("FATAL: "
					+ Thread.currentThread().getName() + " Exception ");

			prt.print(errmsg);
			e.printStackTrace(prt);
			prt.flush();

			System.err.print(errmsg);
			e.printStackTrace();
		}
	}

	/*
	 * public static void main( String[] a ) { String m = new String();
	 * 
	 * for( int i=0; i<a.length; i++ ) m += a[i]+" ";
	 * 
	 * Syslog log = new Syslog( "test Syslog", translateOptions( "CONS,PID" ),
	 * translateFacility( "DAEMON" ) ); Throwable e = new Throwable();
	 * 
	 * log.syslog( LOG_ALERT, "the test: "+m ); e.fillInStackTrace();
	 * log.print_exception( e ); }
	 */
}
