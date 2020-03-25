package com.calix.cnap.ipfix.jnca.cai.utils;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

public abstract class ServiceThread extends Thread {
	protected Object o;

	String start, stop;

	Syslog syslog;
	
	DatagramSocket connection;
	public void setConnection(DatagramSocket conn)
	{
	    connection = conn;
	}
	public DatagramSocket getConnection()
	{
	    return connection;
	}

	public ServiceThread(Object o, Syslog syslog, String start, String stop) {
		this.o = o;
		this.syslog = syslog;
		this.start = start;
		this.stop = stop;
	}

	public void run() {
		syslog.syslog(Syslog.LOG_DEBUG, "START: " + getName() + ", " + start);

		try {
			exec();
		} catch (Throwable e) {
			syslog.print_exception(e);
		}

		syslog.syslog(Syslog.LOG_INFO, "STOP: " + getName() + ", " + stop);
	}

	public void stopService() {

	    if (this.connection == null) {
	        return;
        }
		try {
			this.connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.connection = null;
		this.stop();
	}
	public abstract void exec() throws Throwable;
}

