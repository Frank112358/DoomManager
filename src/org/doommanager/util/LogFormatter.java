package org.doommanager.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * A custom log formatting class.
 */
public class LogFormatter extends Formatter {

	/**
	 * The static class for formatting dates in this logger format.
	 */
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	
	/**
	 * Should the thread name be displayed?
	 */
	private boolean displayThreadName;
	
	public LogFormatter(boolean displayThreadName) {
		this.displayThreadName = displayThreadName;
	}
	
	@Override
	public String format(LogRecord logRecord) {
		return String.format("%s %s%n%s: %s", 
							 dateFormatter.format(new Date(logRecord.getMillis())),
							 this.displayThreadName ? " [" + logRecord.getThreadID() + "]" : "",
							 logRecord.getLevel().getName(),
							 logRecord.getMessage());
	}
}
