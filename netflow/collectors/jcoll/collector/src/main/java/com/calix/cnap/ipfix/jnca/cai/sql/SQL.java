package com.calix.cnap.ipfix.jnca.cai.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import com.calix.cnap.ipfix.jnca.cai.utils.DoneException;
import com.calix.cnap.ipfix.jnca.cai.utils.Resources;
import com.calix.cnap.ipfix.jnca.cai.utils.SuperString;
import com.calix.cnap.ipfix.jnca.cai.utils.Syslog;

public class SQL {
	static public Resources resources = new Resources("SQL");

	static boolean JDBC_abort_on_error = resources
			.isTrue("JDBC.abort.on.error");

	private Hashtable prep_stms = new Hashtable();

	public static final int MAX_CONN = 10;

	public static Connection[] ConPool = new Connection[MAX_CONN];

	public static void error_msg(String msg, SQLException e, String stm)
			throws RuntimeException {
		String s = new String("SQL: " + msg + ": " + e.getErrorCode() + "/"
				+ e.getSQLState() + " " + e.getMessage());

		if (stm != null)
			s += "\nSQL: Statement `" + stm + "'";

		if (Syslog.log != null)
			Syslog.log.syslog(Syslog.LOG_ERR, s);

		if (JDBC_abort_on_error || Syslog.log == null)
			throw new RuntimeException(s);
	}

	static String SQLURI = resources.getAndTrim("JDBC.URI");

	static String SQLdriverName = resources.getAndTrim("JDBC.Driver");;
	static {
		try {
			Class.forName(SQLdriverName).newInstance();
		} catch (Exception e) {
			throw new DoneException("SQL: Unable to load " + SQLdriverName
					+ ": " + SuperString.exceptionMsg(e.toString()));
		}
		for (int conIdx = 0; conIdx < MAX_CONN; conIdx++) {
			try {
				ConPool[conIdx] = DriverManager.getConnection(SQLURI);
				;
			} catch (SQLException e) {
				error_msg("Cannot to connect", e, null);
			}
		}
	}

	private static int ConIdx = 0;

	public static Connection getConn() {
		return ConPool[(ConIdx++) % MAX_CONN];
	}

	private Connection connection = null;

	public SQL() throws DoneException {
		// 有几个Instance就有几个连接 but < MAX_CONN
		connection = getConn();

	}

	public ResultSet execToken(String msg, String token) throws DoneException {
		String sqlStr = resources.getAndTrim(token);
		return exec(msg, sqlStr);
	}

	public ResultSet exec(String msg, String stm) throws DoneException {
		ResultSet re = null;

		try {
			if (stm.trim().toUpperCase().startsWith("SELECT")) {
				Statement stmt = connection.createStatement();
				re = stmt.executeQuery(stm);
			} else {
				try {
					connection.createStatement().execute(stm);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			error_msg(msg, e, stm);
		}

		return re;
	}

	public ResultSet exec(String msg, PreparedStatement stm)
			throws DoneException {
		ResultSet re = null;

		try {
			re = stm.executeQuery();
		} catch (SQLException e) {
			error_msg(msg, e, null);
		}

		return re;
	}

	public PreparedStatement prepareStatement(String msg, String stm) {
		Integer hash = new Integer(Thread.currentThread().hashCode()
				^ stm.hashCode() ^ msg.hashCode());// 取唯一值
		PreparedStatement st = (PreparedStatement) prep_stms.get(hash);

		if (st != null)
			return st;// 返回缓冲内容

		try {
			st = connection.prepareStatement(stm);
		} catch (SQLException e) {
			error_msg(msg, e, stm);
		}

		if (st != null)
			prep_stms.put(hash, st);

		return st;
	}

	public void create_DB() {
		exec("Create IpSegments", resources.getAndTrim("SQL.Create.IpSegments"));
		exec("Create V1 raw table", resources.getAndTrim("SQL.Create.RawV1"));
		exec("Create V5 raw table", resources.getAndTrim("SQL.Create.RawV5"));
		exec("Create V7 raw table", resources.getAndTrim("SQL.Create.RawV7"));
		exec("Create V8 AS raw table", resources
				.getAndTrim("SQL.Create.RawV8.AS"));
		exec("Create V8 ProtoPort raw table", resources
				.getAndTrim("SQL.Create.RawV8.ProtoPort"));
		exec("Create V8 DstPrefix raw table", resources
				.getAndTrim("SQL.Create.RawV8.DstPrefix"));
		exec("Create V8 SrcPrefix raw table", resources
				.getAndTrim("SQL.Create.RawV8.SrcPrefix"));
		exec("Create V8 Prefix raw table", resources
				.getAndTrim("SQL.Create.RawV8.Prefix"));
		exec("Create V9 raw table", resources.getAndTrim("SQL.Create.RawV9"));
		exec("Create option table", resources
				.getAndTrim("SQL.Create.OptionsTable"));
		exec("Create SrcAS table", resources.getAndTrim("SQL.Create.SrcAS"));
		exec("Create DstAS table", resources.getAndTrim("SQL.Create.DstAS"));
		exec("Create ASMatrix table", resources
				.getAndTrim("SQL.Create.ASMatrix"));
		exec("Create SrcNode table", resources.getAndTrim("SQL.Create.SrcNode"));
		exec("Create DstNode table", resources.getAndTrim("SQL.Create.DstNode"));
		exec("Create HostMatrix table", resources
				.getAndTrim("SQL.Create.HostMatrix"));
		exec("Create SrcInterface table", resources
				.getAndTrim("SQL.Create.SrcInterface"));
		exec("Create DstInterface table", resources
				.getAndTrim("SQL.Create.DstInterface"));
		exec("Create InterfaceMatrix table", resources
				.getAndTrim("SQL.Create.InterfaceMatrix"));
		exec("Create SrcPrefix table", resources
				.getAndTrim("SQL.Create.SrcPrefix"));
		exec("Create DstPrefix table", resources
				.getAndTrim("SQL.Create.DstPrefix"));
		exec("Create PrefixMatrix table", resources
				.getAndTrim("SQL.Create.PrefixMatrix"));
		exec("Create Protocol table", resources
				.getAndTrim("SQL.Create.Protocol"));
	}

	public void delete_DB() {
		// exec("Remove IpSegments",
		// resources.getAndTrim("SQL.Drop.IpSegments"));

		exec("Remove V1 raw table", resources.getAndTrim("SQL.Drop.RawV1"));
		exec("Remove V5 raw table", resources.getAndTrim("SQL.Drop.RawV5"));
		exec("Remove V7 raw table", resources.getAndTrim("SQL.Drop.RawV7"));
		exec("Remove V8 AS raw table", resources
				.getAndTrim("SQL.Drop.RawV8.AS"));
		exec("Remove V8 ProtoPort raw table", resources
				.getAndTrim("SQL.Drop.RawV8.ProtoPort"));
		exec("Remove V8 DstPrefix raw table", resources
				.getAndTrim("SQL.Drop.RawV8.DstPrefix"));
		exec("Remove V8 SrcPrefix raw table", resources
				.getAndTrim("SQL.Drop.RawV8.SrcPrefix"));
		exec("Remove V8 Prefix raw table", resources
				.getAndTrim("SQL.Drop.RawV8.Prefix"));
		exec("Remove V9 raw table", resources.getAndTrim("SQL.Drop.RawV9"));
		exec("Remove option table", resources
				.getAndTrim("SQL.Drop.OptionsTable"));
		exec("Remove SrcAS table", resources.getAndTrim("SQL.Drop.SrcAS"));
		exec("Remove DstAS table", resources.getAndTrim("SQL.Drop.DstAS"));
		exec("Remove ASMatrix table", resources.getAndTrim("SQL.Drop.ASMatrix"));
		exec("Remove SrcNode table", resources.getAndTrim("SQL.Drop.SrcNode"));
		exec("Remove DstNode table", resources.getAndTrim("SQL.Drop.DstNode"));
		exec("Remove HostMatrix table", resources
				.getAndTrim("SQL.Drop.HostMatrix"));
		exec("Remove SrcInterface table", resources
				.getAndTrim("SQL.Drop.SrcInterface"));
		exec("Remove DstInterface table", resources
				.getAndTrim("SQL.Drop.DstInterface"));
		exec("Remove InterfaceMatrix table", resources
				.getAndTrim("SQL.Drop.InterfaceMatrix"));
		exec("Remove SrcPrefix table", resources
				.getAndTrim("SQL.Drop.SrcPrefix"));
		exec("Remove DstPrefix table", resources
				.getAndTrim("SQL.Drop.DstPrefix"));
		exec("Remove PrefixMatrix table", resources
				.getAndTrim("SQL.Drop.PrefixMatrix"));
		exec("Remove Protocol table", resources.getAndTrim("SQL.Drop.Protocol"));
	}
}

// while( rez.next() )
// System.out.println( rez.getString( "NUM" )+" "+rez.getString( "FIRST" )+
// " "+rez.getString( "SECOND" ) );
