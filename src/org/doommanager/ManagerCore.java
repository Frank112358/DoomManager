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

package org.doommanager;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import org.doommanager.util.LogFormatter;
import org.doommanager.util.RuntimeArgsContainer;
import org.doommanager.view.MainViewController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Program execution starts here.
 */
public class ManagerCore extends Application {

	/**
	 * The main Stage for this application.
	 */
	private Stage primaryStage;
	
	/**
	 * The controller for this object.
	 */
	private MainViewController mainViewController;
	
	/**
	 * The root layout.
	 */
	private BorderPane rootLayout;

	/**
	 * The name of the application.
	 */
	public static final String APPLICATION_TITLE = "Doom Manager";

	/**
	 * The major version of this application.
	 */
	public static final int MAJOR_VERSION = 0;

	/**
	 * The minor version of this application.
	 */
	public static final int MINOR_VERSION = 1;
	
	/**
	 * Any type of version appendage (alpha, beta, or empty...etc).
	 */
	public static final String VERSION_TYPE = "alpha";
	
	/**
	 * The logger for this class.
	 */
	private static final Logger log = Logger.getLogger(ManagerCore.class.getName());
	
	/**
	 * Initializes the window by reading in the FXML definitions of how the
	 * window should be rendered.
	 * 
	 * @throw RuntimeException
	 * 		If any error occurs while instantiating the window from the FXML
	 * 		file. This should be fatal regardless, catching it won't do much.
	 */
	private void initWindow() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ManagerCore.class.getResource("view/MainView.fxml"));
			
			rootLayout = (BorderPane)loader.load();
			this.mainViewController = (MainViewController)loader.getController();
			this.mainViewController.setManagerCore(this);
			
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			RuntimeException rte = new RuntimeException("Missing view/MainView.fxml file.", e);
			log.log(Level.SEVERE, "Could not find the MainView window definition to render the window with. Is the .jar missing something?", rte);
			throw rte;
		}
	}
	
	/**
	 * Stops the program by calling the Platform's exit() method.
	 */
	public void quit() {
		log.info("Exiting " + APPLICATION_TITLE);
		Platform.exit();
	}
	
	/**
	 * Gets the main stage.
	 * 
	 * @return
	 * 		The stage for the main window.
	 */
	public Stage getStage() {
		return this.primaryStage;
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(APPLICATION_TITLE + " v" + MAJOR_VERSION + "." + MINOR_VERSION + (VERSION_TYPE.isEmpty() == false ? " " + VERSION_TYPE : ""));
		initWindow();
	}
	
	/**
	 * Sets up the logging information.
	 */
	private static void setupLogging() {
		// Set up the global log level.
		LogManager.getLogManager().getLogger("").setLevel(RuntimeArgsContainer.getGlobalLogLevel());
		LogManager.getLogManager().getLogger("").addHandler(new StreamHandler(System.out, new LogFormatter(false)));
		
		// Set up the file writing.
		try {
			FileHandler fileHandler = new FileHandler(RuntimeArgsContainer.getLogfileLocation());
			fileHandler.setLevel(RuntimeArgsContainer.getGlobalLogLevel());
			fileHandler.setFormatter(new SimpleFormatter());
		} catch (SecurityException se) {
			log.log(Level.WARNING, "Security exception preventing generation of the log file", se);
		} catch (IOException ioe) {
			log.log(Level.WARNING, "IO exception preventing generation of the log file", ioe);
		}
	}
	
	/**
	 * Main method.
	 * 
	 * @param args
	 * 		Runtime arguments.
	 */
	public static void main(String[] args) {
		// Parse the runtime arguments.
		RuntimeArgsContainer.addArgs(args);
		
		// Set up logging and file generation.
		setupLogging();
		
		// Launch the GUI.
		launch(args);
	}
}
