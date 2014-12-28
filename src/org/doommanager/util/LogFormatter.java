/*
 * DoomManager
 * Copyright (C) 2014  Chris K
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
