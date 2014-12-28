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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RuntimeArgsContainer {

	/**
	 * A list of all the runtime args. This should only be filled out at the
	 * beginning.
	 */
	private static ArrayList<String> arguments = new ArrayList<>();
	
	/**
	 * The global log level. By default is at INFO level.
	 */
	private static Level globalLogLevel = Level.INFO;
	
	/**
	 * The logfile location. By default it is logfile.log in the same directory
	 * as the executable.
	 */
	private static String logFileLocation = "";
	
	/**
	 * The name of the log file.
	 */
	private static String logFileName = "logfile.log";
	
	/**
	 * Signifies that the program should error out if it isn't possible to
	 * generate a log file.
	 */
	private static boolean errorOutIfLogGenerationFailure = false;
	
	/**
	 * The logger for this class.
	 */
	private static final Logger log = Logger.getLogger(RuntimeArgsContainer.class.getName());
	
	/**
	 * Not to be instantiated.
	 */
	private RuntimeArgsContainer() {
	}
	
	/**
	 * Adds a list of arguments to the runtime args.
	 * 
	 * @param args
	 * 		The list of arguments to add. If this is null, nothing will be done.
	 */
	public static void addArgs(String... args) {
		if (args == null) {
			log.log(Level.WARNING, "Attempted to pass RuntimeArgsContainer a null argument list");
			return;
		}
		for (String arg : args)
			arguments.add(arg);
		processArgs();
	}
	
	/**
	 * Goes through the arguments and sets various fields with the data from
	 * the arguments.
	 */
	private static void processArgs() {
		for (int i = 0; i < arguments.size(); i++) {
			String arg = arguments.get(i).toLowerCase();
			
			// We're interested in arguments only that specify parameters and valid strings.
			if (arg.startsWith("-") && arg.length() >= 2) {
				switch (arg.substring(1)) {
				case "logpath":
					// Prevent overshooting into an invalid index, or any argument that most likely is a switch argument.
					if (i + 1 >= arguments.size()) {
						log.log(Level.WARNING, "Missing argument after " + arguments.get(i));
						return;
					} else if (arguments.get(i + 1).startsWith("-")) {
						log.log(Level.WARNING, "Missing argument after " + arguments.get(i) + ", found command instead");
						break;
					}
					// Handle possible missing slashes at the end based on the OS.
					// We assume a slash is wanted because the current directory would just be an empty string (which can't be provided/is default).
					String filePath = arguments.get(i + 1);
					if (!filePath.endsWith("\\") && !filePath.endsWith("/")) {
						log.log(Level.WARNING, "Provided log file path does not end with a slash, appending a slash");
						filePath += System.getProperty("os.name").toLowerCase().startsWith("windows") ? '\\' : '/'; 
					}
					// Set values with the proper information.
					log.log(Level.INFO, "Set file logging path to: " + filePath);
					logFileLocation = filePath;
					i++; // Move past the processed argument, since we've already read ahead.
					break;
					
				case "logfile":
					// Prevent overshooting into an invalid index, or any argument that most likely is a switch argument.
					// Must also disallow any kind of slashes or double periods.
					if (i + 1 >= arguments.size()) {
						log.log(Level.WARNING, "Missing argument after " + arguments.get(i));
						return;
					} else if (arguments.get(i + 1).startsWith("-")) {
						log.log(Level.WARNING, "Missing argument after " + arguments.get(i) + ", found command instead");
						break;
					}
					i++; // Move past the processed argument, since we've already read ahead.
					String nextArg = arguments.get(i + 1);
					if (nextArg.contains("/") || nextArg.contains("\\") || nextArg.contains("..")) {
						log.log(Level.WARNING, "The file name should not contain slashes or multiple periods adjacent to each other");
						continue;
					}
					log.log(Level.INFO, "Set file logging file to: " + nextArg);
					logFileName = nextArg;
					break;
					
				case "logfilerequired":
					errorOutIfLogGenerationFailure = true;
					log.log(Level.INFO, "Set exit instruction if log file cannot be written to");
					break;
				
				default:
					log.log(Level.WARNING, "Unexpected parameter: " + arguments.get(i));
					break;
				}
			} else {
				log.log(Level.WARNING, "Unexpected argument: " + arguments.get(i));
			}
		}
	}
	
	/**
	 * Gets all the args that have been passed to the container.
	 * 
	 * @return
	 * 		Gets an unmodifiable list of all the runtime arg strings. If there
	 * 		are no args, this will be an empty list.
	 */
	public static List<String> getArgs() {
		return Collections.unmodifiableList(arguments);
	}
	
	/**
	 * Gets the global log level.
	 * 
	 * @return
	 * 		The global log level, or Level.INFO by default.
	 */
	public static Level getGlobalLogLevel() {
		return globalLogLevel;
	}
	
	/**
	 * Gets the log file location.
	 * 
	 * @return
	 * 		The place the log file should be written at. By default, this is in
	 * 		the same executable directory named 'logfile.log'.
	 */
	public static String getLogFileLocation() {
		return logFileLocation;
	}
	
	/**
	 * Gets the log file name.
	 * 
	 * @return
	 * 		The name of the log file. By default it is 'logfile.log'.
	 */
	public static String getLogFileName() {
		return logFileName;
	}
	
	/**
	 * Gets the log file path and file name as one.
	 * 
	 * @return
	 * 		The path and name concatenated. By default, this will be the
	 * 		current directory + 'logfile.log'.
	 */
	public static String getLogFilePathName() {
		return logFileLocation + logFileName;
	}
	
	/**
	 * Checks if the program should not continue if log file generation fails.
	 * 
	 * @return
	 * 		True if it should exit prematurely, false if not (default).
	 */
	public static boolean exitIfLogGenerationFails() {
		return errorOutIfLogGenerationFailure;
	}
}
