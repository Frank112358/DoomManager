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

package org.doommanager.editor;

/**
 * Indicates that the object has resources in memory that can be freed to
 * facilitate garbage collection, or any kind of file resources that should be
 * ended when the object has dispose() called.
 */
public interface Disposable {

	/**
	 * Performs clean-up of the object by releasing any resources or cleaning
	 * up any large data structures.
	 */
	void dispose();
}
