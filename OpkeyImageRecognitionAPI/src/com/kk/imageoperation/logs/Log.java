package com.kk.imageoperation.logs;

public class Log {
	//TODO Implements Logger
	public static boolean debug = false;

	public static void debug(Object msg) {
		if (debug)
			System.out.println(msg);

	}
}
