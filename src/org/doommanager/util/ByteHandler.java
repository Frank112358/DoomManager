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

import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Similar to the ByteBuffer, this allows for inputting data and reading any
 * primitive types from the data in a simple manner. Supports all the basic
 * signed/unsigned primitives, and String/null-terminated String reading. This
 * class throws runtime exceptions through various methods.
 */
public class ByteHandler {
	
	/**
	 * The raw data that makes up this ByteHandler.
	 */
	private byte[] data;
	
	/**
	 * The order of the data.
	 */
	private ByteOrder order;
	
	/**
	 * A marker pointer of where we can read our next data piece from. This is
	 * automatically incremented when a read method is performed.
	 */
	private int marker;
	
	/**
	 * The logger for this class.
	 */
	private static final Logger log = Logger.getLogger(ByteHandler.class.getName());
	
	/**
	 * Constructs a ByteHandler that wraps around the specified data. This does
	 * not copy any data, only points to the array provided.
	 * 
	 * @param data
	 * 		The data to allow operations on.
	 * 
	 * @param order
	 * 		The byte order of the data.
	 * 
	 * @throws NullPointerException
	 * 		If the argument provided is null.
	 */
	public ByteHandler(byte[] data, ByteOrder order) {
		if (data == null) {
			NullPointerException e = new NullPointerException("Provided a null byte array to ByteHandler.");
			log.log(Level.SEVERE, "ByteHandler data is null", e);
			throw e;
		}
		if (order == null) {
			NullPointerException e = new NullPointerException("Provided a null byte order type to ByteHandler.");
			log.log(Level.SEVERE, "ByteHandler order is null", e);
			throw e;
		}
		this.marker = 0;
		this.order = order;
		this.data = data;
	}
	
	/**
	 * Gets how many bytes in length the data is.
	 *  
	 * @return
	 * 		The number of bytes in the data array.
	 */
	public int size() {
		return this.data.length;
	}
	
	/**
	 * Changes the reading order of this object.
	 * 
	 * @param order
	 * 		The order to read as.
	 * 
	 * @throws NullPointerException
	 * 		If the argument is null.
	 */
	public void changeOrder(ByteOrder order) {
		if (order == null)
			throw new NullPointerException("Attempted to change ByteHandler order to null.");
		this.order = order;
	}
	
	/**
	 * Gets the location in the array where the next read will begin.
	 * 
	 * @return
	 * 		The location of the marker as an offset from the beginning of the
	 * 		data.
	 */
	public int getMarkerLocation() {
		return this.marker;
	}
	
	/**
	 * Sets the market location to a specified index.
	 * 
	 * @param index
	 * 		The byte to set the marker to. Note that this ranges from zero to
	 * 		the array size. If the value is greater than the array size, an
	 * 		exception will be thrown. Negative values are not allowed.
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 * 		If the index is greater than the size of the byte array or 
	 * 		negative.
	 */
	public void setMarkerLocation(int index) {
		if (index > this.data.length || index < 0)
			throw new ArrayIndexOutOfBoundsException("Attempted to set location on ByteHandler past the array length or negative.");
		this.marker = index;
	}
	
	/**
	 * Resets the marker to the beginning of the array.
	 */
	public void resetMarkerLocation() {
		this.marker = 0;
	}
	
	/**
	 * Reads a byte at the marker.
	 * 
	 * @return
	 * 		The byte next in line from the marker.
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 * 		If the marker is past the length of the array.
	 */
	public byte getByte() {
		byte b = this.data[this.marker];
		this.marker++;
		return b;
	}
	
	/**
	 * Reads a byte at the index. This does not advance the marker.
	 * 
	 * @param index
	 * 		The index to read the data from.
	 * 
	 * @return
	 * 		The byte at the specified index.
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 * 		If the index is invalid (too large or negative).
	 */
	public byte getByte(int index) {
		return this.data[index];
	}
	
	/**
	 * Reads a byte at the marker and returns a short that will encompass the
	 * byte as potentially being unsigned. This does not advance the marker.
	 * 
	 * @return
	 * 		The unsigned byte next in line from the marker.
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 * 		If the marker is past the length of the array.
	 */
	public short getByteUnsigned() {
		short b = (short)(this.data[this.marker] & 0x00FF);
		this.marker++;
		return b;
	}

	/**
	 * Reads an unsigned byte at the index.
	 * 
	 * @param index
	 * 		The index to read the data from.
	 * 
	 * @return
	 * 		The short at the specified index as a short to encompass the full
	 * 		range of an unsigned byte.
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 * 		If the index is invalid (too large or negative).
	 */
	public short getByteUnsigned(int index) {
		return (short)(this.data[index] & 0x00FF);
	}
	
	/**
	 * Creates a copy of the bytes of the specified length and returns that new
	 * copy.
	 * 
	 * @param length
	 * 		The length of bytes to copy into a new array.
	 * 
	 * @return
	 * 		A fully copied array.
	 * 
	 * @throws IndexOutOfBoundsException
	 * 		If the length overshoots the array size.
	 */
	public byte[] getBytes(int length) {
		byte[] newData = new byte[length];
		System.arraycopy(this.data, this.marker, newData, 0, length);
		this.marker += length;
		return newData;
	}
	
	/**
	 * Creates a copy of the bytes of the specified length and returns that new
	 * copy.
	 * 
	 * @param length
	 * 		The length of bytes to copy into a new array.
	 * 
	 * @return
	 * 		A fully copied array.
	 * 
	 * @throws IndexOutOfBoundsException
	 * 		If the length overshoots the array size.
	 */
	public byte[] getBytes(int index, int length) {
		if (index + length > this.data.length)
			throw new IndexOutOfBoundsException("ByteHandler getBytes was passed an index and length that would go out of bounds.");
		byte[] newData = new byte[length];
		System.arraycopy(this.data, this.marker, newData, 0, length);
		this.marker += length;
		return newData;
	}
	
	/**
	 * Reads a short at the marker.
	 * 
	 * @return
	 * 		The short next in line from the marker.
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 * 		If the marker is past the length of the array.
	 */
	public short getShort() {
		short s = (short)(this.order == ByteOrder.BIG_ENDIAN ? 
					(((this.data[this.marker] & 0xFF) << 8) + (this.data[this.marker + 1] & 0xFF)) : 
					(((this.data[this.marker] & 0xFF) + ((this.data[this.marker + 1] & 0xFF) << 8))));
		this.marker += 2;
		return s;
	}
	
	/**
	 * Reads a short at the index. This does not advance the marker.
	 * 
	 * @param index
	 * 		The index to read the data from.
	 * 
	 * @return
	 * 		The short at the specified index.
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 * 		If the index is invalid (too large or negative).
	 */
	public short getShort(int index) {
		return (short)(this.order == ByteOrder.BIG_ENDIAN ? 
				(((this.data[index] & 0xFF) << 8) + (this.data[index + 1] & 0xFF)) : 
				(((this.data[index] & 0xFF) + ((this.data[index + 1] & 0xFF) << 8))));
	}
	
	/**
	 * Reads a short at the marker and returns a short that will encompass the
	 * byte as potentially being unsigned.
	 * 
	 * @return
	 * 		The unsigned short next in line from the marker.
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 * 		If the marker is past the length of the array.
	 */
	public int getShortUnsigned() {
		int i = (this.order == ByteOrder.BIG_ENDIAN ? 
				(((this.data[this.marker] & 0xFF) << 8) + (this.data[this.marker + 1] & 0xFF)) : 
				(((this.data[this.marker] & 0xFF) + ((this.data[this.marker + 1] & 0xFF) << 8))));
		this.marker += 2;
		return i;
	}

	/**
	 * Reads an unsigned short at the index. This does not advance the marker.
	 * 
	 * @param index
	 * 		The index to read the data from.
	 * 
	 * @return
	 * 		The short at the specified index as a short to encompass the full
	 * 		range of an unsigned byte.
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 * 		If the index is invalid (too large or negative).
	 */
	public int getShortUnsigned(int index) {
		return (this.order == ByteOrder.BIG_ENDIAN ? 
				(((this.data[this.marker] & 0xFF) << 8) + (this.data[this.marker + 1] & 0xFF)) : 
				(((this.data[this.marker] & 0xFF) + ((this.data[this.marker + 1] & 0xFF) << 8))));
	}
	
	/**
	 * Reads an integer at the marker.
	 * 
	 * @return
	 * 		The int next in line from the marker.
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 * 		If the marker is past the length of the array.
	 */
	public int getInt() {
		int i = (this.order == ByteOrder.BIG_ENDIAN ? 
					(((this.data[this.marker] & 0xFF) << 24) + ((this.data[this.marker + 1] & 0xFF) << 16) + ((this.data[this.marker + 2] & 0xFF) << 8) + (this.data[this.marker + 3] & 0xFF)) : 
					((this.data[this.marker] & 0xFF) + ((this.data[this.marker + 1] & 0xFF) << 8) + ((this.data[this.marker + 2] & 0xFF) << 16) + (((this.data[this.marker + 3] & 0xFF) << 24))));
		this.marker += 4;
		return i;
	}
	
	/**
	 * Reads an int at the marker and returns a short that will encompass the
	 * byte as potentially being unsigned.
	 * 
	 * @return
	 * 		The unsigned int next in line from the marker.
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 * 		If the marker is past the length of the array.
	 */
	public long getIntUnsigned() {
		long l = (long)(this.order == ByteOrder.BIG_ENDIAN ? 
						(((this.data[this.marker] & 0xFFL) << 24) + ((this.data[this.marker + 1] & 0xFFL) << 16) + ((this.data[this.marker + 2] & 0xFFL) << 8) + (this.data[this.marker + 3] & 0xFFL)) : 
						((this.data[this.marker] & 0xFFL) + ((this.data[this.marker + 1] & 0xFFL) << 8) + ((this.data[this.marker + 2] & 0xFFL) << 16) + (((this.data[this.marker + 3] & 0xFFL) << 24))));
		this.marker += 4;
		return l;
	}

	/**
	 * Reads an unsigned int at the index. This does not advance the marker.
	 * 
	 * @param index
	 * 		The index to read the data from.
	 * 
	 * @return
	 * 		The int at the specified index as a short to encompass the full
	 * 		range of an unsigned byte.
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 * 		If the index is invalid (too large or negative).
	 */
	public long getIntUnsigned(int index) {
		return (long)(this.order == ByteOrder.BIG_ENDIAN ? 
						(((this.data[this.marker] & 0xFFL) << 24) + ((this.data[this.marker + 1] & 0xFFL) << 16) + ((this.data[this.marker + 2] & 0xFFL) << 8) + (this.data[this.marker + 3] & 0xFFL)) : 
						((this.data[this.marker] & 0xFFL) + ((this.data[this.marker + 1] & 0xFFL) << 8) + ((this.data[this.marker + 2] & 0xFFL) << 16) + (((this.data[this.marker + 3] & 0xFFL) << 24))));
	}
	
	/**
	 * Gets the String at the marker with a specified length.
	 * 
	 * @param length
	 * 		The length of characters to read.
	 * 
	 * @return
	 * 		The string from the marker to the length.
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 * 		If the length would overshoot the data bounds.
	 */
	public String getString(int length) {
		if (this.marker + length > this.data.length)
			throw new ArrayIndexOutOfBoundsException("Attempted to get a String length that runs past the array end.");
		String returnStr = "";
		for (int i = this.marker; i < this.marker + length; i++)
			returnStr += (char)this.data[i];
		return returnStr;
	}
	
	/**
	 * Gets a null terminated string from the marker position. This does not
	 * return the null character.
	 * 
	 * @return
	 * 		A string of characters without the null.
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 * 		If there was no null terminator and the reader goes out of bounds.
	 */
	public String getStringNullTerminated() {
		int index = this.marker;
		String returnStr = "";
		while (this.data[index] != 0) {
			returnStr += (char)this.data[index];
			index++;
		}
		this.marker += index + 1;
		return returnStr;
	}
	
	/**
	 * In Doom, there are places where the texture name is 8 chars long, and
	 * ends with a null terminator if it's not 8 characters long. This method
	 * is a convenience method which will end when it hits a null terminator,
	 * or it reaches the end of the provided length.
	 * 
	 * @param length
	 * 		The length to read if no null terminators are found beforehand.
	 * 
	 * @return
	 * 		The String with no null characters.
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 * 		If the length is invalid.
	 */
	public String getStringWithNoNull(int length) {
		String returnStr = "";
		for (int i = this.marker; i < this.marker + length && this.data[i] != 0; i++)
			returnStr += (char)this.data[i];
		return returnStr;
	}
}
