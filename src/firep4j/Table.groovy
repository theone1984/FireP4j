/*
 * Copyright 2010 Thomas Endres
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package firep4j;

import java.io.Serializable;
import java.util.LinkedList;

import firep4j.tools.JSONUtils;

/**
 * This class implements the table structure needed for table output.
 * 
 * @author Thomas Endres
 */
public class Table implements Serializable {
	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = -4485564433544914202L;
	
	/**
	 * JSON utility class instance
	 */
	private JSONUtils jsonUtils = null;
	
	/**
	 * Number of columns the table contains
	 */
	private int columns = 0;
	
	/**
	 * Head row entries of the table
	 */
	private String[] headers = null;
	
	/**
	 * Content rows of the table
	 */
	private LinkedList<Object[]> data = null;
	
	/**
	 * This constructor instantiates the table structure.
	 * 
	 * @param columns Number of columns the table contains
	 * @throws IllegalArgumentException
	 */
	public Table(int columns)
		throws IllegalArgumentException {
		// Input variables are tested
		if (columns <= 0) {
			throw new IllegalArgumentException("Only numbers greater than 0 are allowed as column counts!");
		}
		
		// Column count is set
		this.columns = columns;		
		setHeaders("");
		data = new LinkedList<Object[]>();

	}
	
	/**
	 * This method sets the table header strings.
	 * 
	 * @param headers Table header strings
	 */
	public void setHeaders(String... headers) {
		// If there are too many elements, an exception is thrown
		if (headers.length > columns) {
			throw new IllegalArgumentException("The argument count must be smaller than the column count of the table!");
		}
		
		// The headers are initialized as empty strings
		this.headers = new String[columns];		
		for (int i = 0; i < this.headers.length; i++) {
			this.headers[i] = "";
		}
		
		// The new headers are set
		for (int i = 0; i < headers.length; i++) {
			this.headers[i] = headers[i];
		}		
	}
	
	/**
	 * This method adds a new row to the table (consisting of the values given).
	 * 
	 * @param columnEntries Row values
	 */
	public void addRow(Serializable... columnEntries) {
		// If there are too many elements, an exception is thrown
		if (columnEntries.length > columns) {
			throw new IllegalArgumentException("The argument count must be smaller than the column count of the table!");
		}
		
		// The new data row is initialized
		Object[] dataEntry = new Object[columns];
		
		// Row entries are set according to the row value objects given
		for (int i = 0; i < columnEntries.length; i++) {
			dataEntry[i] = columnEntries[i];
		}
		
		// The new row is added to the table
		data.add(dataEntry);
	}
	
	/**
	 * This method returns the column count of the table.
	 * 
	 * @return Column count of the table
	 */
	public int getColumnCount() {
		return columns;
	}
	
	/**
	 * This method serializes the table.
	 * 
	 * @return The serialized table string
	 */
	public String serialize(JSONUtils jsonUtils) {
		if (jsonUtils == null) {
			throw new Exception("JSON utility object must be given!")
		}
		this.jsonUtils = jsonUtils;
		
		String tableString = "";
		
		// The header string is built
		tableString = buildEntries(headers);
		
		// The row strings are built and added to the resulting string		
		for (int i = 0; i < data.size(); i++) {
			tableString += "," + buildEntries(data.get(i));
		}
		
		// The table string is put in parentheses and returned
		tableString = "[" + tableString + "]";	
		return tableString;
	}
	
	/**
	 * This method builds a row string for the given values.
	 * 
	 * @param values Values
	 * @return Resulting row string
	 */
	private String buildEntries(Object[] values) {
		String entryString = "";
		
		// All the objects are serialized and concatenated
		for (int i = 0; i < values.length; i++) {
			if (i != 0) {
				entryString += ",";
			}
			// Each object is serialized using JSON
			entryString += jsonUtils.serialize(values[i]);			
		}
		
		// The resulting string is put in parentheses and returned
		entryString = "[" + entryString + "]";		
		return entryString;
	}
}