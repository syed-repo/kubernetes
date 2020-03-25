package com.calix.cnap.ipfix.jnca.cai.flow.collector.interpretator;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.calix.cnap.ipfix.jnca.cai.flow.packets.Flow;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Address;
import com.calix.cnap.ipfix.jnca.cai.flow.struct.Prefix;

abstract class PT_Var {
	public abstract boolean isNumeric();

	public abstract boolean isAddress();

	public abstract boolean isPrefix();

	public abstract String toString();
}

class PT_V_Port extends PT_Var {
	public boolean isNumeric() {
		return true;
	}

	public boolean isAddress() {
		return false;
	}

	public boolean isPrefix() {
		return false;
	}

	public String toString() {
		return "Port";
	}
}

class PT_V_SrcPort extends PT_Var {
	public boolean isNumeric() {
		return true;
	}

	public boolean isAddress() {
		return false;
	}

	public boolean isPrefix() {
		return false;
	}

	public String toString() {
		return "SrcPort";
	}
}

class PT_V_DstPort extends PT_Var {
	public boolean isNumeric() {
		return true;
	}

	public boolean isAddress() {
		return false;
	}

	public boolean isPrefix() {
		return false;
	}

	public String toString() {
		return "DstPort";
	}
}

class PT_V_Proto extends PT_Var {
	public boolean isNumeric() {
		return true;
	}

	public boolean isAddress() {
		return false;
	}

	public boolean isPrefix() {
		return false;
	}

	public String toString() {
		return "Proto";
	}
}

class PT_V_TOS extends PT_Var {
	public boolean isNumeric() {
		return true;
	}

	public boolean isAddress() {
		return false;
	}

	public boolean isPrefix() {
		return false;
	}

	public String toString() {
		return "TOS";
	}
}

class PT_V_AS extends PT_Var {
	public boolean isNumeric() {
		return true;
	}

	public boolean isAddress() {
		return false;
	}

	public boolean isPrefix() {
		return false;
	}

	public String toString() {
		return "AS";
	}
}

class PT_V_SrcAS extends PT_Var {
	public boolean isNumeric() {
		return true;
	}

	public boolean isAddress() {
		return false;
	}

	public boolean isPrefix() {
		return false;
	}

	public String toString() {
		return "SrcAS";
	}
}

class PT_V_DstAS extends PT_Var {
	public boolean isNumeric() {
		return true;
	}

	public boolean isAddress() {
		return false;
	}

	public boolean isPrefix() {
		return false;
	}

	public String toString() {
		return "DstAS";
	}
}

class PT_V_Addr extends PT_Var {
	public boolean isNumeric() {
		return false;
	}

	public boolean isAddress() {
		return true;
	}

	public boolean isPrefix() {
		return false;
	}

	public String toString() {
		return "Addr";
	}
}

class PT_V_SrcAddr extends PT_Var {
	public boolean isNumeric() {
		return false;
	}

	public boolean isAddress() {
		return true;
	}

	public boolean isPrefix() {
		return false;
	}

	public String toString() {
		return "SrcAddr";
	}
}

class PT_V_DstAddr extends PT_Var {
	public boolean isNumeric() {
		return false;
	}

	public boolean isAddress() {
		return true;
	}

	public boolean isPrefix() {
		return false;
	}

	public String toString() {
		return "DstAddr";
	}
}

class PT_V_NextHop extends PT_Var {
	public boolean isNumeric() {
		return false;
	}

	public boolean isAddress() {
		return true;
	}

	public boolean isPrefix() {
		return false;
	}

	public String toString() {
		return "NextHop";
	}
}

class PT_V_If extends PT_Var {
	public boolean isNumeric() {
		return true;
	}

	public boolean isAddress() {
		return false;
	}

	public boolean isPrefix() {
		return false;
	}

	public String toString() {
		return "If";
	}
}

class PT_V_InIf extends PT_Var {
	public boolean isNumeric() {
		return true;
	}

	public boolean isAddress() {
		return false;
	}

	public boolean isPrefix() {
		return false;
	}

	public String toString() {
		return "InIf";
	}
}

class PT_V_OutIf extends PT_Var {
	public boolean isNumeric() {
		return true;
	}

	public boolean isAddress() {
		return false;
	}

	public boolean isPrefix() {
		return false;
	}

	public String toString() {
		return "OutIf";
	}
}

class PT_V_Prefix extends PT_Var {
	public boolean isNumeric() {
		return false;
	}

	public boolean isAddress() {
		return false;
	}

	public boolean isPrefix() {
		return true;
	}

	public String toString() {
		return "Prefix";
	}
}

class PT_V_SrcPrefix extends PT_Var {
	public boolean isNumeric() {
		return false;
	}

	public boolean isAddress() {
		return false;
	}

	public boolean isPrefix() {
		return true;
	}

	public String toString() {
		return "SrcPrefix";
	}
}

class PT_V_DstPrefix extends PT_Var {
	public boolean isNumeric() {
		return false;
	}

	public boolean isAddress() {
		return false;
	}

	public boolean isPrefix() {
		return true;
	}

	public String toString() {
		return "DstPrefix";
	}
}

class PT_O_EndOfLine {
	public String toString() {
		return "*End-Of-Line*";
	}
}

class PT_O_LParen {
	public String toString() {
		return "(";
	}
}

class PT_O_RParen {
	public String toString() {
		return ")";
	}
}

class PT_O_Or {
	public String toString() {
		return "||";
	}
}

class PT_O_And {
	public String toString() {
		return "&&";
	}
}

class PT_O_Not {
	public String toString() {
		return "!";
	}
}

abstract class PT_O_Comp {
	public abstract String toString();
}

class PT_O_In extends PT_O_Comp {
	public String toString() {
		return "~";
	}
}

abstract class PT_O_Weak extends PT_O_Comp {
}

abstract class PT_O_Strong extends PT_O_Comp {
}

class PT_O_Equal extends PT_O_Weak {
	public String toString() {
		return "=";
	}
}

class PT_O_NotEqual extends PT_O_Weak {
	public String toString() {
		return "!=";
	}
}

class PT_O_Less extends PT_O_Strong {
	public String toString() {
		return "<";
	}
}

class PT_O_LessEqual extends PT_O_Strong {
	public String toString() {
		return "<=";
	}
}

class PT_O_Great extends PT_O_Strong {
	public String toString() {
		return ">";
	}
}

class PT_O_GreatEqual extends PT_O_Strong {
	public String toString() {
		return ">=";
	}
}

class PT_V_Number {
	public long v;

	public PT_V_Number(long v) {
		this.v = v;
	}

	public String toString() {
		return "Number " + v;
	}
}

class PT_V_Range {
	public long s, e;

	public PT_V_Range(long s, long e) {
		this.s = s;
		this.e = e;
	}

	public String toString() {
		return "Range " + s + "-" + e;
	}
}

class PT_V_Address {
	public long addr;

	public PT_V_Address(long addr) {
		this.addr = addr;
	}

	public String getValue() {
		return ((addr >>> 24) & 0xff) + "." + ((addr >>> 16) & 0xff) + "."
				+ ((addr >>> 8) & 0xff) + "." + (addr & 0xff);
	}

	public String toString() {
		return "Address " + getValue();
	}
}

class PT_V_VPrefix extends Prefix {
	public PT_V_VPrefix(long address, byte mask) {
		super(address, mask);
	}

	public String toString() {
		return "Network " + ((address >>> 24) & 0xff) + "."
				+ ((address >>> 16) & 0xff) + "." + ((address >>> 8) & 0xff)
				+ "." + (address & 0xff) + "/" + mask;
	}
}

class PT_Lexer {
	static String word_delimiters = " \t()!=|&<>~";

	static Hashtable vars;

	static {
		vars = new Hashtable();

		vars.put(new String("port"), new PT_V_Port());
		vars.put(new String("srcport"), new PT_V_SrcPort());
		vars.put(new String("dstport"), new PT_V_DstPort());
		vars.put(new String("proto"), new PT_V_Proto());
		vars.put(new String("tos"), new PT_V_TOS());
		vars.put(new String("as"), new PT_V_AS());
		vars.put(new String("srcas"), new PT_V_SrcAS());
		vars.put(new String("dstas"), new PT_V_DstAS());
		vars.put(new String("addr"), new PT_V_Addr());
		vars.put(new String("srcaddr"), new PT_V_SrcAddr());
		vars.put(new String("dstaddr"), new PT_V_DstAddr());
		vars.put(new String("nexthop"), new PT_V_NextHop());
		vars.put(new String("if"), new PT_V_If());
		vars.put(new String("inif"), new PT_V_InIf());
		vars.put(new String("outif"), new PT_V_OutIf());
		vars.put(new String("prefix"), new PT_V_Prefix());
		vars.put(new String("srcprefix"), new PT_V_SrcPrefix());
		vars.put(new String("dstprefix"), new PT_V_DstPrefix());
	}

	String source;

	char[] src;

	String i_am;

	Object back_obj;

	public int length;

	public int pos, cur_word;

	public PT_Lexer(String s, String who_i_am) {
		i_am = who_i_am;
		source = s + " ";
		length = s.length() + 1;
		src = source.toCharArray();
		pos = 0;
		cur_word = 0;
		back_obj = null;
	}

	private String filler(int pos, char ch) {
		if (pos > 0) {
			char[] b = new char[pos];

			for (int i = 0; i < pos; i++)
				b[i] = ch;

			return new String(b);
		}

		return "";
	}

	public PT_Error compose_error(String m1) {
		String ret = "\n*** Error while parsing of " + i_am + "\n"
				+ source.substring(0, length - 1) + "\n";

		if (pos - cur_word - 2 >= 0)
			ret += filler(cur_word, ' ') + "^"
					+ filler(pos - cur_word - 2, '-') + "^ " + m1;
		else
			ret += filler(cur_word, ' ') + "^ " + m1;

		return new PT_Error(ret);
	}

	public void error(String m1) throws PT_Error {
		throw compose_error(m1);
	}

	public void error(String msg, int start) throws PT_Error {
		int pos;
		PT_Error err;

		pos = cur_word;
		cur_word = start;
		err = compose_error(msg);
		cur_word = pos;

		throw err;
	}

	public void back(Object o) {
		if (!(o instanceof PT_O_EndOfLine))
			back_obj = o;
	}

	public Object get_symbol() throws PT_Error {
		if (back_obj != null) {
			Object a = back_obj;

			back_obj = null;
			return a;
		}

		while (pos < length && (src[pos] == ' ' || src[pos] == '\t'))
			pos++;

		if (pos >= length) {
			cur_word = pos - 1;
			return new PT_O_EndOfLine();
		}

		cur_word = pos;

		char ch = src[pos++], ch1 = src[pos];

		switch (ch) {
		case '=':
			return new PT_O_Equal();
		case '(':
			return new PT_O_LParen();
		case ')':
			return new PT_O_RParen();
		case '~':
			return new PT_O_In();
		}

		if (ch == '|')
			if (ch1 == '|') {
				pos++;
				return new PT_O_Or();
			} else
				error("Incomplete `||' operator");

		if (ch == '&')
			if (ch1 == '&') {
				pos++;
				return new PT_O_And();
			} else
				error("Incomplete `&&' operator");

		if (ch == '<')
			if (ch1 == '=') {
				pos++;
				return new PT_O_LessEqual();
			} else
				return new PT_O_Less();

		if (ch == '>')
			if (ch1 == '=') {
				pos++;
				return new PT_O_GreatEqual();
			} else
				return new PT_O_Great();

		if (ch == '!')
			if (ch1 == '=') {
				pos++;
				return new PT_O_NotEqual();
			} else
				return new PT_O_Not();

		if (Character.isDigit(ch))
			return get_number(ch);

		if (Character.isLetter(ch))
			return get_var(ch);

		error("Unknown character");
		return null; // unreachable return
	}

	private Object get_var(char c) throws PT_Error {
		String ret = "" + c;
		char ch = ' ';

		while (pos < length) {
			ch = src[pos];

			if (Character.isLetter(ch) || Character.isDigit(ch)) {
				ret += ch;
				pos++;
			} else
				break;
		}

		if (word_delimiters.indexOf(ch) == -1) {
			pos++;
			error("Illegal character in field name");
		}

		Object o = vars.get(ret.toLowerCase());

		if (o == null)
			error("Unknown field name");

		return o;
	}

	private Object get_number(char c) throws PT_Error {
		String from = "" + c, to = "";
		char ch = ' ';
		boolean first = true;

		while (pos < length) {
			ch = src[pos];

			if ((ch == '.' || ch == '/') && first)
				return get_address(from, ch);

			if (Character.isDigit(ch)) {
				if (first)
					from += ch;
				else
					to += ch;

				pos++;
			} else if (ch == '-' && first) {
				pos++;
				first = false;
			} else
				break;
		}

		if (word_delimiters.indexOf(ch) == -1) {
			pos++;
			error("Illegal character in " + (first ? "number" : "range"));
		}

		try {
			if (first)
				return new PT_V_Number(Long.parseLong(from));
			else {
				long s = Long.parseLong(from), e = Long.parseLong(to);

				if (e < s)
					error("Reversed range is not supported");

				if (e == s)
					error("Reduced range is suboptimal and cannot be used");

				return new PT_V_Range(s, e);
			}
		} catch (NumberFormatException e) {
		}

		error("BUG: Lexer failed while parse number/range");
		return null; // unreachable return
	}

	private int check_number(int limit, int b1) throws PT_Error {
		if (b1 > limit)
			error("Number " + b1 + " is too big, maximum " + limit
					+ " is allowed");

		return b1;
	}

	private int make_number_and_check(int limit, int b1, int digit)
			throws PT_Error {
		return check_number(limit, b1 * 10 + digit);
	}

	private void may_be_order_error(char ch, char last) throws PT_Error {
		if (!Character.isDigit(last))
			error("`" + ch + "' must be after number, not after `" + last + "'");
	}

	private Object get_address(String b0s, char ch_i) throws PT_Error {
		int b0 = 0, b1 = 0, b2 = 0, b3 = 0;
		int current_word = cur_word;

		try {
			b0 = check_number(255, Integer.parseInt(b0s));
		} catch (NumberFormatException e) {
			error("BUG: Lexer failed while parse 1st byte of address/preffix");
		}

		pos++;
		cur_word = pos;

		char ch = ' ';
		int addr = -1;
		int mask = 0;

		if (ch_i == '.')
			addr = 1;
		else if (ch_i == '/')
			addr = 4;
		else
			error("BUG: Lexer failed while parse `" + ch_i
					+ "' after 1st byte of address/preffix");

		char last = ch_i;

		while (pos < length) {
			ch = src[pos];

			if (Character.isDigit(ch)) {
				int digit = Character.digit(ch, 10);

				last = ch;
				pos++;

				switch (addr) {
				case 1:
					b1 = make_number_and_check(255, b1, digit);
					break;
				case 2:
					b2 = make_number_and_check(255, b2, digit);
					break;
				case 3:
					b3 = make_number_and_check(255, b3, digit);
					break;
				case 4:
					mask = make_number_and_check(32, mask, digit);
					break;
				default:
					error("BUG: Lexer failed while parse #" + addr
							+ " byte of address/prefix");
				}
			} else if (ch == '.') {
				may_be_order_error(ch, last);

				last = ch;
				pos++;

				if (++addr > 3)
					if (addr > 4)
						error("Illegal characrter in prefix");
					else
						error("Extra period in IP address");

				cur_word = pos;
			} else if (ch == '/') {
				may_be_order_error(ch, last);

				last = ch;
				pos++;

				if (addr <= 3)
					addr = 4;
				else
					error("Illegal characrter in prefix");

				cur_word = pos;
			} else
				break;
		}

		cur_word = current_word;

		if (word_delimiters.indexOf(ch) == -1) {
			pos++;
			error("Illegal character in address or network");
		}

		if (!Character.isDigit(last))
			error((addr == 4 ? "Prefix" : "Address")
					+ " cannot be finished by `" + last + "'");

		long address = (((long) b0) << 24) | (((long) b1) << 16)
				| (((long) b2) << 8) | b3;

		if (addr <= 3)
			return new PT_V_Address(address);

		if (addr == 4)
			return new PT_V_VPrefix(address, (byte) mask);

		error("BUG: Lexer failed while parse address/prefix");
		return null; // unreachable return
	}

}

abstract class PT_A_Instr {
	abstract public String toString();

	abstract public String toString(int bl);

	protected String filler(int bl) {
		if (bl <= 0)
			return "";

		char[] a = new char[bl];

		for (int i = 0; i < bl; i++)
			a[i] = ' ';

		return new String(a);
	}

	public boolean exec(Flow flow) throws PT_ExecError {
		throw new PT_ExecError("BUG: Opcode `" + this.getClass().getName()
				+ "' (" + this.toString() + ") cannot be executed");
	}
}

abstract class PT_A_TwoOpsInstr extends PT_A_Instr {
	protected PT_A_Instr left, right;

	public PT_A_TwoOpsInstr(PT_A_Instr l, PT_A_Instr r) {
		left = l;
		right = r;
	}

	public String toString(int bl) {
		return filler(bl) + toString() + "\n" + left.toString(bl + 2)
				+ right.toString(bl + 2);
	}
}

class PT_A_Or extends PT_A_TwoOpsInstr {
	public PT_A_Or(PT_A_Instr l, PT_A_Instr r) {
		super(l, r);
	}

	public String toString() {
		return "||";
	}

	public boolean exec(Flow flow) throws PT_ExecError {
		if (left.exec(flow))
			return true;

		return right.exec(flow);
	}
}

class PT_A_And extends PT_A_TwoOpsInstr {
	public PT_A_And(PT_A_Instr l, PT_A_Instr r) {
		super(l, r);
	}

	public String toString() {
		return "&&";
	}

	public boolean exec(Flow flow) throws PT_ExecError {
		if (!left.exec(flow))
			return false;

		return right.exec(flow);
	}
}

class PT_A_Not extends PT_A_Instr {
	PT_A_Instr prog;

	public PT_A_Not(PT_A_Instr prog) {
		this.prog = prog;
	}

	public String toString() {
		return "!";
	}

	public String toString(int bl) {
		return filler(bl) + toString() + "\n" + prog.toString(bl + 2);
	}

	public boolean exec(Flow flow) throws PT_ExecError {
		return !prog.exec(flow);
	}
}

class PT_A_Number extends PT_A_Instr {
	public PT_V_Number num;

	public PT_A_Number(PT_V_Number n) {
		num = n;
	}

	public String toString() {
		return num.toString();
	}

	public String toString(int bl) {
		return filler(bl) + toString() + "\n";
	}
}

class PT_A_VPrefix extends PT_A_Instr {
	public PT_V_VPrefix prefix;

	public PT_A_VPrefix(PT_V_VPrefix p) {
		prefix = p;
	}

	public String toString() {
		return prefix.toString();
	}

	public String toString(int bl) {
		return filler(bl) + toString() + "\n";
	}
}

class PT_A_Address extends PT_A_Instr {
	public PT_V_Address address;

	public PT_A_Address(PT_V_Address a) {
		address = a;
	}

	public String toString() {
		return address.toString();
	}

	public String toString(int bl) {
		return filler(bl) + toString() + "\n";
	}
}

abstract class PT_A_Field extends PT_A_Instr {
	public PT_Var var;

	public PT_A_Field(PT_Var v) {
		var = v;
	}

	public String toString() {
		return var.toString();
	}

	public String toString(int bl) {
		return filler(bl) + toString() + "\n";
	}

	public abstract Object getValue(Flow flow);
}

class PT_A_Port extends PT_A_Field {
	public PT_A_Port(PT_Var f) {
		super(f);
	}

	public Object getValue(Flow flow) {
		Vector a = new Vector(2);

		a.add(flow.getSrcPort());
		a.add(flow.getDstPort());

		return a;
	}
}

class PT_A_SrcPort extends PT_A_Field {
	public PT_A_SrcPort(PT_Var f) {
		super(f);
	}

	public Object getValue(Flow flow) {
		return flow.getSrcPort();
	}
}

class PT_A_DstPort extends PT_A_Field {
	public PT_A_DstPort(PT_Var f) {
		super(f);
	}

	public Object getValue(Flow flow) {
		return flow.getDstPort();
	}
}

class PT_A_Proto extends PT_A_Field {
	public PT_A_Proto(PT_Var f) {
		super(f);
	}

	public Object getValue(Flow flow) {
		return flow.getProto();
	}
}

class PT_A_TOS extends PT_A_Field {
	public PT_A_TOS(PT_Var f) {
		super(f);
	}

	public Object getValue(Flow flow) {
		return flow.getTOS();
	}
}

class PT_A_AS extends PT_A_Field {
	public PT_A_AS(PT_Var f) {
		super(f);
	}

	public Object getValue(Flow flow) {
		Vector a = new Vector(2);

		a.add(flow.getSrcAS());
		a.add(flow.getDstAS());

		return a;
	}
}

class PT_A_SrcAS extends PT_A_Field {
	public PT_A_SrcAS(PT_Var f) {
		super(f);
	}

	public Object getValue(Flow flow) {
		return flow.getSrcAS();
	}
}

class PT_A_DstAS extends PT_A_Field {
	public PT_A_DstAS(PT_Var f) {
		super(f);
	}

	public Object getValue(Flow flow) {
		return flow.getDstAS();
	}
}

class PT_A_Addr extends PT_A_Field {
	public PT_A_Addr(PT_Var f) {
		super(f);
	}

	public Object getValue(Flow flow) {
		Vector a = new Vector(2);

		a.add(flow.getSrcAddr());
		a.add(flow.getDstAddr());

		return a;
	}
}

class PT_A_SrcAddr extends PT_A_Field {
	public PT_A_SrcAddr(PT_Var f) {
		super(f);
	}

	public Object getValue(Flow flow) {
		return flow.getSrcAddr();
	}
}

class PT_A_DstAddr extends PT_A_Field {
	public PT_A_DstAddr(PT_Var f) {
		super(f);
	}

	public Object getValue(Flow flow) {
		return flow.getDstAddr();
	}
}

class PT_A_NextHop extends PT_A_Field {
	public PT_A_NextHop(PT_Var f) {
		super(f);
	}

	public Object getValue(Flow flow) {
		return flow.getNextHop();
	}
}

class PT_A_If extends PT_A_Field {
	public PT_A_If(PT_Var f) {
		super(f);
	}

	public Object getValue(Flow flow) {
		Vector a = new Vector(2);

		a.add(flow.getInIf());
		a.add(flow.getOutIf());

		return a;
	}
}

class PT_A_InIf extends PT_A_Field {
	public PT_A_InIf(PT_Var f) {
		super(f);
	}

	public Object getValue(Flow flow) {
		return flow.getInIf();
	}
}

class PT_A_OutIf extends PT_A_Field {
	public PT_A_OutIf(PT_Var f) {
		super(f);
	}

	public Object getValue(Flow flow) {
		return flow.getOutIf();
	}
}

class PT_A_Prefix extends PT_A_Field {
	public PT_A_Prefix(PT_Var f) {
		super(f);
	}

	public Object getValue(Flow flow) {
		Vector a = new Vector(2);

		a.add(flow.getSrcPrefix());
		a.add(flow.getDstPrefix());

		return a;
	}
}

class PT_A_SrcPrefix extends PT_A_Field {
	public PT_A_SrcPrefix(PT_Var f) {
		super(f);
	}

	public Object getValue(Flow flow) {
		return flow.getSrcPrefix();
	}
}

class PT_A_DstPrefix extends PT_A_Field {
	public PT_A_DstPrefix(PT_Var f) {
		super(f);
	}

	public Object getValue(Flow flow) {
		return flow.getDstPrefix();
	}
}

class PT_A_CompException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3928376356730099133L;
}

abstract class PT_A_Compr extends PT_A_Instr {
	protected PT_A_Field field;

	protected PT_A_Number number;

	protected PT_O_Comp comp;

	private static PT_A_Field createField(PT_Var f) throws PT_A_CompException {
		if (f instanceof PT_V_Port)
			return new PT_A_Port(f);
		if (f instanceof PT_V_SrcPort)
			return new PT_A_SrcPort(f);
		if (f instanceof PT_V_DstPort)
			return new PT_A_DstPort(f);
		if (f instanceof PT_V_Proto)
			return new PT_A_Proto(f);
		if (f instanceof PT_V_TOS)
			return new PT_A_TOS(f);
		if (f instanceof PT_V_AS)
			return new PT_A_AS(f);
		if (f instanceof PT_V_SrcAS)
			return new PT_A_SrcAS(f);
		if (f instanceof PT_V_DstAS)
			return new PT_A_DstAS(f);
		if (f instanceof PT_V_Addr)
			return new PT_A_Addr(f);
		if (f instanceof PT_V_SrcAddr)
			return new PT_A_SrcAddr(f);
		if (f instanceof PT_V_DstAddr)
			return new PT_A_DstAddr(f);
		if (f instanceof PT_V_NextHop)
			return new PT_A_NextHop(f);
		if (f instanceof PT_V_If)
			return new PT_A_If(f);
		if (f instanceof PT_V_InIf)
			return new PT_A_InIf(f);
		if (f instanceof PT_V_OutIf)
			return new PT_A_OutIf(f);
		if (f instanceof PT_V_Prefix)
			return new PT_A_Prefix(f);
		if (f instanceof PT_V_SrcPrefix)
			return new PT_A_SrcPrefix(f);
		if (f instanceof PT_V_DstPrefix)
			return new PT_A_DstPrefix(f);

		throw new PT_A_CompException();
	}

	protected PT_A_Compr(PT_Var f, PT_O_Comp c) throws PT_A_CompException {
		field = createField(f);
		comp = c;
	}

	public PT_A_Compr(PT_Var f, PT_V_Number n, PT_O_Comp c)
			throws PT_A_CompException {
		this(f, c);
		number = new PT_A_Number(n);
	}

	public String toString(int bl) {
		return filler(bl) + toString() + "\n";
	}

	public String toString() {
		return field.toString() + " " + comp.toString() + " "
				+ number.toString();
	}

	public boolean exec(Flow flow) throws PT_ExecError {
		Object o = field.getValue(flow);

		if (o instanceof Vector) {
			Enumeration e = ((Vector) o).elements();

			while (e.hasMoreElements())
				if (compare(e.nextElement()))
					return true;

			return false;
		}

		return compare(o);
	}

	protected boolean compare(Object value) throws PT_ExecError {
		throw new PT_ExecError("BUG: Opcode `" + this.getClass().getName()
				+ "' (" + this.toString() + ") not implemented yet");
	}
}

class PT_A_In extends PT_A_Compr {
	PT_V_Range range;

	public PT_A_In(PT_Var f, PT_V_Range r, PT_O_In i) throws PT_A_CompException {
		super(f, (PT_O_Comp) i);
		range = r;
	}

	public String toString() {
		return field.toString() + " " + comp.toString() + " "
				+ range.toString();
	}

	protected boolean compare(Object value) throws PT_ExecError {
		if (value == null)
			return false;

		if (!(value instanceof Long))
			throw new PT_ExecError("BUG: PT_A_In: not a Long value: "
					+ value.getClass().getName());

		long val = ((Long) value).longValue();

		return range.s <= val && val <= range.e;
	}
}

class PT_A_InPrefix extends PT_A_Compr {
	PT_V_VPrefix prefix;

	public PT_A_InPrefix(PT_Var f, PT_V_VPrefix p, PT_O_In i)
			throws PT_A_CompException {
		super(f, (PT_O_Comp) i);
		prefix = p;
	}

	public String toString() {
		return field.toString() + " " + comp.toString() + " "
				+ prefix.toString();
	}

	protected boolean compare(Object value) throws PT_ExecError {
		if (value == null)
			return false;

		// do not reorder!

		if (value instanceof Prefix)
			return prefix.includes((Prefix) value);

		if (value instanceof Address)
			return prefix.consists((Address) value);

		throw new PT_ExecError(
				"BUG: PT_A_InPrefix: not a Address/Prefix value: "
						+ value.getClass().getName());
	}
}

class PT_A_Equal extends PT_A_Compr {
	public PT_A_Equal(PT_Var f, PT_V_Number n, PT_O_Comp c)
			throws PT_A_CompException {
		super(f, n, c);
	}

	protected boolean compare(Object value) throws PT_ExecError {
		if (value == null)
			return false;

		if (!(value instanceof Long))
			throw new PT_ExecError("BUG: PT_A_Equal: not a Long value: "
					+ value.getClass().getName());

		return ((Long) value).longValue() == number.num.v;
	}
}

class PT_A_NotEqual extends PT_A_Equal {
	public PT_A_NotEqual(PT_Var f, PT_V_Number n, PT_O_Comp c)
			throws PT_A_CompException {
		super(f, n, c);
	}

	protected boolean compare(Object value) throws PT_ExecError {
		return !super.compare(value);
	}
}

class PT_A_EqualAddress extends PT_A_Compr {
	PT_V_Address address;

	public PT_A_EqualAddress(PT_Var f, PT_V_Address address, PT_O_Comp c)
			throws PT_A_CompException {
		super(f, c);
		this.address = address;
	}

	public String toString() {
		return field.toString() + " " + comp.toString() + " "
				+ address.toString();
	}

	protected boolean compare(Object value) throws PT_ExecError {
		if (value == null)
			return false;

		if (!(value instanceof String))
			throw new PT_ExecError(
					"BUG: PT_A_EqualAddress: not a String value: "
							+ value.getClass().getName());

		return ((String) value).equals(address.getValue());
	}
}

class PT_A_NotEqualAddress extends PT_A_EqualAddress {
	public PT_A_NotEqualAddress(PT_Var f, PT_V_Address address, PT_O_Comp c)
			throws PT_A_CompException {
		super(f, address, c);
	}

	protected boolean compare(Object value) throws PT_ExecError {
		return !super.compare(value);
	}
}

class PT_A_EqualPrefix extends PT_A_Compr {
	PT_V_VPrefix prefix;

	public PT_A_EqualPrefix(PT_Var f, PT_V_VPrefix prefix, PT_O_Comp c)
			throws PT_A_CompException {
		super(f, c);
		this.prefix = prefix;
	}

	public String toString() {
		return field.toString() + " " + comp.toString() + " "
				+ prefix.toString();
	}

	protected boolean compare(Object value) throws PT_ExecError {
		if (value == null)
			return false;

		if (!(value instanceof Prefix))
			throw new PT_ExecError(
					"BUG: PT_A_EqualPrefix: not a Prefix value: "
							+ value.getClass().getName());

		return ((Prefix) value).equals(prefix);
	}
}

class PT_A_NotEqualPrefix extends PT_A_EqualPrefix {
	public PT_A_NotEqualPrefix(PT_Var f, PT_V_VPrefix prefix, PT_O_Comp c)
			throws PT_A_CompException {
		super(f, prefix, c);
	}

	protected boolean compare(Object value) throws PT_ExecError {
		return !super.compare(value);
	}
}

class PT_A_Less extends PT_A_Compr {
	public PT_A_Less(PT_Var f, PT_V_Number n, PT_O_Comp c)
			throws PT_A_CompException {
		super(f, n, c);
	}

	protected boolean compare(Object value) throws PT_ExecError {
		if (value == null)
			return false;

		if (!(value instanceof Long))
			throw new PT_ExecError("BUG: PT_A_Less: not a Long value: "
					+ value.getClass().getName());

		return ((Long) value).longValue() < number.num.v;
	}
}

class PT_A_LessEqual extends PT_A_Compr {
	public PT_A_LessEqual(PT_Var f, PT_V_Number n, PT_O_Comp c)
			throws PT_A_CompException {
		super(f, n, c);
	}

	protected boolean compare(Object value) throws PT_ExecError {
		if (value == null)
			return false;

		if (!(value instanceof Long))
			throw new PT_ExecError("BUG: PT_A_LessEqual: not a Long value: "
					+ value.getClass().getName());

		return ((Long) value).longValue() <= number.num.v;
	}
}

class PT_A_Great extends PT_A_Compr {
	public PT_A_Great(PT_Var f, PT_V_Number n, PT_O_Comp c)
			throws PT_A_CompException {
		super(f, n, c);
	}

	protected boolean compare(Object value) throws PT_ExecError {
		if (value == null)
			return false;

		if (!(value instanceof Long))
			throw new PT_ExecError("BUG: PT_A_Great: not a Long value: "
					+ value.getClass().getName());

		return ((Long) value).longValue() > number.num.v;
	}
}

class PT_A_GreatEqual extends PT_A_Compr {
	public PT_A_GreatEqual(PT_Var f, PT_V_Number n, PT_O_Comp c)
			throws PT_A_CompException {
		super(f, n, c);
	}

	protected boolean compare(Object value) throws PT_ExecError {
		if (value == null)
			return false;

		if (!(value instanceof Long))
			throw new PT_ExecError("BUG: PT_A_GreatEqual: not a Long value: "
					+ value.getClass().getName());

		return ((Long) value).longValue() >= number.num.v;
	}
}

public class RuleSet {
	private PT_A_Instr prog;

	private RuleSet(PT_A_Instr prog) {
		this.prog = prog;
	}

	public String toString() {
		return prog.toString(0);
	}

	public boolean exec(Flow flow) throws PT_ExecError {
		return prog.exec(flow);
	}

	public static RuleSet create(String str, String who_i_am) throws PT_Error {
		return new RuleSet(compile_prog(new PT_Lexer(str, who_i_am)));
	}

	private static PT_A_Instr compile_prog(PT_Lexer lexer) throws PT_Error {
		Object o = lexer.get_symbol();

		if (o instanceof PT_O_EndOfLine)
			lexer.error("It is an empty expression", lexer.cur_word);
		else {
			o = compile_or(o, lexer);

			if (o instanceof PT_A_Instr) {
				Object o1 = lexer.get_symbol();

				if (!(o1 instanceof PT_O_EndOfLine)) {
					lexer.pos = lexer.length;
					lexer.error("Unexpected text after expression");
				}
			}
		}

		if (o instanceof PT_A_Instr)
			return (PT_A_Instr) o;

		String old_text = o.toString();

		lexer.pos = lexer.length;
		lexer.cur_word = 0;

		lexer.error("BUG: Compiler completed with `" + old_text + "'");
		return null; // unreachable return
	}

	private static Object compile_or(Object oi, PT_Lexer lexer) throws PT_Error {
		Object left = compile_and(oi, lexer);

		if (!(left instanceof PT_A_Instr))
			return left;

		Object o = lexer.get_symbol();

		if (o instanceof PT_O_Or) {
			Object right = compile_or(lexer.get_symbol(), lexer);

			if (!(right instanceof PT_A_Instr))
				return right;

			return new PT_A_Or((PT_A_Instr) left, (PT_A_Instr) right);
		}

		lexer.back(o);

		return left;
	}

	private static Object compile_and(Object oi, PT_Lexer lexer)
			throws PT_Error {
		Object left = compile_expr(oi, lexer);

		if (!(left instanceof PT_A_Instr))
			return left;

		Object o = lexer.get_symbol();

		if (o instanceof PT_O_And) {
			Object right = compile_and(lexer.get_symbol(), lexer);

			if (!(right instanceof PT_A_Instr))
				return right;

			return new PT_A_And((PT_A_Instr) left, (PT_A_Instr) right);
		}

		lexer.back(o);

		return left;
	}

	private static Object compile_expr(Object o, PT_Lexer lexer)
			throws PT_Error {
		if (o instanceof PT_O_LParen) {
			int start = lexer.cur_word;
			Object inner = compile_or(lexer.get_symbol(), lexer);

			if (!(inner instanceof PT_A_Instr))
				return inner;

			Object o1 = lexer.get_symbol();

			if (!(o1 instanceof PT_O_RParen))
				lexer.error("There is expected `)', not `" + o1.toString()
						+ "'", start);

			return inner;
		} else if (o instanceof PT_O_Not) {
			Object inner = compile_expr(lexer.get_symbol(), lexer);

			if (!(inner instanceof PT_A_Instr))
				return inner;

			return new PT_A_Not((PT_A_Instr) inner);
		}

		return compile_comp(o, lexer);
	}

	private static Object compile_comp(Object left_i, PT_Lexer lexer)
			throws PT_Error {
		if (!(left_i instanceof PT_Var))
			lexer.error("There is expected `field', not `" + left_i.toString()
					+ "'");

		PT_Var left = (PT_Var) left_i;
		Object o = lexer.get_symbol();

		try {
			if (o instanceof PT_O_In) {
				Object right = lexer.get_symbol();

				if (left.isNumeric()) {
					if (!(right instanceof PT_V_Range))
						lexer.error("There is expected `range', not `"
								+ right.toString() + "'");

					return new PT_A_In(left, (PT_V_Range) right, (PT_O_In) o);
				}

				if (left.isAddress() || left.isPrefix()) {
					if (!(right instanceof PT_V_VPrefix))
						lexer.error("There is expected `prefix', not `"
								+ right.toString() + "'");

					return new PT_A_InPrefix(left, (PT_V_VPrefix) right,
							(PT_O_In) o);
				}

				lexer.error("BUG: While parsing `" + left.toString()
						+ "' and `" + right.toString() + "' for `"
						+ o.toString() + "'");
			} else if (o instanceof PT_O_Comp) {
				Object right = lexer.get_symbol();

				if (o instanceof PT_O_Weak) {
					if (left.isNumeric() && right instanceof PT_V_Number) {
						if (o instanceof PT_O_Equal)
							return new PT_A_Equal(left, (PT_V_Number) right,
									(PT_O_Comp) o);

						if (o instanceof PT_O_NotEqual)
							return new PT_A_NotEqual(left, (PT_V_Number) right,
									(PT_O_Comp) o);
					}

					if (left.isAddress() && right instanceof PT_V_Address) {
						if (o instanceof PT_O_Equal)
							return new PT_A_EqualAddress(left,
									(PT_V_Address) right, (PT_O_Comp) o);

						if (o instanceof PT_O_NotEqual)
							return new PT_A_NotEqualAddress(left,
									(PT_V_Address) right, (PT_O_Comp) o);
					}

					if (left.isPrefix() && right instanceof PT_V_VPrefix) {
						if (o instanceof PT_O_Equal)
							return new PT_A_EqualPrefix(left,
									(PT_V_VPrefix) right, (PT_O_Comp) o);

						if (o instanceof PT_O_NotEqual)
							return new PT_A_NotEqualPrefix(left,
									(PT_V_VPrefix) right, (PT_O_Comp) o);
					}

					lexer.error("Incompatible arguments for `" + o.toString()
							+ "' - `" + left.toString() + "' and `"
							+ right.toString() + "'");
				} else if (o instanceof PT_O_Strong) {
					if (!(right instanceof PT_V_Number))
						lexer.error("There is expected `number', not `"
								+ right.toString() + "'");

					if (o instanceof PT_O_Less)
						return new PT_A_Less(left, (PT_V_Number) right,
								(PT_O_Comp) o);

					if (o instanceof PT_O_LessEqual)
						return new PT_A_LessEqual(left, (PT_V_Number) right,
								(PT_O_Comp) o);

					if (o instanceof PT_O_Great)
						return new PT_A_Great(left, (PT_V_Number) right,
								(PT_O_Comp) o);

					if (o instanceof PT_O_GreatEqual)
						return new PT_A_GreatEqual(left, (PT_V_Number) right,
								(PT_O_Comp) o);
				}

				lexer.error("BUG: Instancing source failed for operator: `"
						+ o.toString() + "'");
			}
		} catch (PT_A_CompException e) {
			lexer.error("BUG: Instancing code failed for operator: `"
					+ o.toString() + "'");
		}

		lexer.error("There is expected `operator', not `" + o.toString() + "'");
		return null; // unreachable return
	}

	/*
	 * static public void main( String arg[] ) throws PT_ExecError, PT_Error {
	 * String ar = ""; int i=0;
	 * 
	 * for( i=0; i<arg.length; i++ ) { if( !ar.equals( "" ) ) ar += " ";
	 * 
	 * ar += arg[i]; }
	 * 
	 * int count = 10000; RuleSet rules = null; long start =
	 * System.currentTimeMillis();
	 * 
	 * for( i=0; i<count; i++ ) rules = RuleSet.create( ar, "line:" );
	 * 
	 * System.out.println( "Compiled "+count+" times in "+
	 * (System.currentTimeMillis()-start)+ " mills to\n"+ rules.toString() );
	 * 
	 * boolean ret = false; oj ooo = new oj();
	 * 
	 * start = System.currentTimeMillis();
	 * 
	 * for( i=0; i<count; i++ ) ret=rules.exec( ooo );
	 * 
	 * System.out.println( "Evaluated "+count+" times in "+
	 * (System.currentTimeMillis()-start)+ " mills to ["+ret+"]" ); }
	 */

}

/*
 * class oj extends Flow { public Long getSrcPort() { return new Long( 1 ); }
 * public Long getDstPort() { return new Long( 2 ); } public Long getProto() {
 * return new Long( 3 ); } public Long getTOS() { return new Long( 4 ); } public
 * Long getSrcAS() { return new Long( 5 ); } public Long getDstAS() { return new
 * Long( 6 ); }
 * 
 * public Address getSrcAddr() { return new Address( 0x01010101 ); } public
 * Address getDstAddr() { return new Address( 0x02020202 ); } public Address
 * getNextHop() { return new Address( 0x03030303 ); }
 * 
 * public Long getInIf() { return new Long( 7 ); } public Long getOutIf() {
 * return new Long( 8 ); }
 * 
 * public Prefix getSrcPrefix() { return new Prefix( (long)0x01020304, (byte)8 ); }
 * public Prefix getDstPrefix() { return new Prefix( (long)0x04030201, (byte)24 ); } }
 */
