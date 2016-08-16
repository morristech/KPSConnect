package org.xdevs23.debugutils;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StackTraceParser {
	
	public static String parse(Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter  pw = new PrintWriter(sw);
		throwable.printStackTrace(pw);
		return sw.toString();
	}

    public static void logStackTrace(Throwable throwable) {
        Log.e("RssManager", parse(throwable));
    }

}
