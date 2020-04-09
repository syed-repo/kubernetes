package com.calix.cnap.ipfix.jnca.cai.flow.collector;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;

import com.calix.cnap.ipfix.jnca.cai.flow.packets.FlowPacket;
import com.calix.cnap.ipfix.jnca.cai.sql.SQL;
import com.calix.cnap.ipfix.jnca.cai.utils.ServiceThread;
import com.calix.cnap.ipfix.jnca.cai.utils.Syslog;
import com.calix.cnap.ipfix.jnca.cai.utils.Util;
/**
 * 所有归并的父类
 * @author CaiMao
 *
 */
public abstract class Scheme_Aggregator extends Hashtable {
	long Start, Stop;

	long interval;

	ServiceThread th;

	SQL sql;

	String scheme;

	protected PreparedStatement add_stm = null;

	protected String add_sql = null;

	public Scheme_Aggregator(SQL sql, String scheme, long interval) {
		this.scheme = scheme;//like "SrcAS"
		this.interval = interval;

		if (interval != 0) {
			add_sql = SQL.resources.getAndTrim("SQL.Add." + scheme);
			add_stm = sql.prepareStatement("准备插入" + scheme
					+ "表", add_sql);
			//入库线程
			th = new ServiceThread(this, Syslog.log, "Scheme " + scheme
					+ " with " + Util.toInterval(interval) + " interval",
					scheme) {
				public void exec() throws Throwable {
					((Scheme_Aggregator) o).save_loop();
				}
			};

			th.start();
		} else
			Syslog.log.syslog(Syslog.LOG_NOTICE, "Scheme " + scheme
					+ " disabled");
	}
	/**
	 * 由子类在add(FlowPacket packet)中调用
	 * @param it
	 */
	public void add(Scheme_Item it) {
		Integer hash = new Integer(it.hashCode());

		if (it.getData().RouterIP == null)
			throw new RuntimeException("it.getData().RouterIP == null for "
					+ it.toString());

		synchronized (this) {
			Object o = get(hash);

			if (o == null)
				put(hash, it);//增加这个scheme item到列表中
			else
				((Scheme_Item) o).add(it);//增加流量数据
		}
	}
	/**
	 * 由collector来的包到达这里
	 * @param packet
	 */
	public abstract void add(FlowPacket packet);

	private void init_times() {
		Start = System.currentTimeMillis() / 1000;
		Stop = Start + interval;
	}
	/**
	 * 入库线程的主循环
	 *
	 */
	public void save_loop() {
		init_times();//像系统时间对齐

		while (true) {
			try {
				long wait = Stop - Start;

				if (wait >= 0)
					Thread.sleep(wait * 1000);

				synchronized (this) {//和add同步
					for (Enumeration f = elements(); f.hasMoreElements();) {
						Scheme_Item item = (Scheme_Item) f.nextElement();

						try {
							//头四个字段是固定的
							add_stm.setDate(1, new java.sql.Date(Start * 1000));
							add_stm.setTime(2, new java.sql.Time(Start * 1000));
							add_stm.setDate(3, new java.sql.Date(Stop * 1000));
							add_stm.setTime(4, new java.sql.Time(Stop * 1000));
							item.fill(add_stm, 5);
							add_stm.executeUpdate();
						} catch (SQLException e) {
							SQL.error_msg("INSERT to " + scheme + " table", e,
									add_sql);
						}
					}

					clear();//包入库以后，清除Hashtable
				}

				init_times();
			} catch (InterruptedException e) {
			}
		}
	}

}
