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
	private static String logFileLocation = "logfile.log";
	
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
		// TODO
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
	public static String getLogfileLocation() {
		return logFileLocation;
	}
}
