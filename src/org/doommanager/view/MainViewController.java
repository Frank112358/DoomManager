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

package org.doommanager.view;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.doommanager.ManagerCore;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

public class MainViewController {

	@FXML
	private BorderPane mainBorderContainer;
	
	@FXML
	private MenuItem menuFileNewWad;
	
	@FXML
	private MenuItem menuFileNewPK3;
	
	@FXML
	private MenuItem menuFileOpen;
	
	@FXML
	private MenuItem menuFileExit;
	
	@FXML
	private TabPane fileTabContainer;
	
	@FXML
	private Rectangle bottomBarRectangle;
	
	@FXML
	private Label bottomBarStatusLabel;
	
	@FXML
	private AnchorPane bottomBarAnchorContainer;
	
	/**
	 * The logger for this class.
	 */
	private static final Logger log = Logger.getLogger(MainViewController.class.getName());
	
	/**
	 * The reference to the manager core that controls this.
	 */
	private ManagerCore managerCore;
	
	public MainViewController() {
		this.managerCore = null;
	}
	
	/**
	 * Sets the ManagerCore for this object. This should be the main container
	 * that controls everything.
	 * 
	 * @param managerCore
	 * 		The manager core.
	 */
	public void setManagerCore(ManagerCore managerCore) {
		if (managerCore == null) {
			NullPointerException npe = new NullPointerException("Tried adding a null ManagerCore to the MainViewController.");
			log.log(Level.SEVERE, "Tried adding a null ManagerCore to the MainViewController", npe);
			throw npe;
		}
		this.managerCore = managerCore;
	}
	
	/**
	 * Gets the manager core for this object.
	 * 
	 * @return
	 * 		The manager core. This can return null if it's not set.
	 */
	public ManagerCore getManagerCore() {
		return this.managerCore;
	}
	
	/**
	 * Initializes the object from FXML's loading.
	 */
	public void initialize() {
		// Let the bottom rectangle stretch to the size of the size of the container width.
		bottomBarRectangle.widthProperty().bind(mainBorderContainer.widthProperty());
	}
	
	/**
	 * Quits the entire program.
	 */
	public void quitProgram() {
		this.managerCore.quit();
	}
	
	/**
	 * Starts a Dialog File Chooser.
	 */
	public void openDialogFileChooser() {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extensionWad = new FileChooser.ExtensionFilter("Wad Files (*.wad)", "*.wad");
		FileChooser.ExtensionFilter extensionPK3 = new FileChooser.ExtensionFilter("PK3 Files (*.pk3)", "*.pk3");
		fileChooser.getExtensionFilters().add(extensionWad);
		fileChooser.getExtensionFilters().add(extensionPK3);
		fileChooser.setTitle("Open Wad/PK3");
		File file = fileChooser.showOpenDialog(this.managerCore.getStage());
		if (file != null) {
			log.info("Open file dialog chooser selected: " + file.getAbsolutePath());
		} else {
			log.log(Level.FINER, "Got a null file from open dialog file chooser");
		}
	}
}
