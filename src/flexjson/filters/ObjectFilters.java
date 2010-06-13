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
package flexjson.filters;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class contains filters that allow object filtering.
 * 
 * @author Thomas Endres
 */
public class ObjectFilters {
	/**
	 * Object filter HashMap
	 */
	private HashMap<String, ArrayList<String>> filters = null;
	
	/**
	 * Dummy constructor
	 */
	public ObjectFilters() {
		// Class variables are set
		filters = new HashMap<String, ArrayList<String>>();
	}
	
	/**
	 * This method adds an object filter to the object filter list.
	 * 
	 * @param className Class name containing the filtered fields
	 * @param fieldNames Field names that should be filtered
	 */
	public void addObjectFilter(String className, String... fieldNames) {
		// If no field names are present, method execution is aborted
		if (fieldNames.length == 0) {
			return;
		}
		
		// If the class name is not found in the HashMap, a new entry is created
		if (filters.get(className) == null) {
			filters.put(className, new ArrayList<String>());
		}
		
		// All the fields are added to the HashMap entry if they are not present yet
		for (int i = 0; i < fieldNames.length; i++) {
			if (!isFieldExcluded(className, fieldNames[i])) {
				filters.get(className).add(fieldNames[i]);
			}
		}
	}
	
	/**
	 * This method removes an object filter from the object filter list.
	 * 
	 * @param className Class name containing the fields
	 * @param fieldNames Field names that should not be filtered anymore
	 */
	public void removeObjectFilter(String className, String... fieldNames) {
		// If no field names are present, method execution is aborted
		if (fieldNames.length == 0) {
			return;
		}
		
		// All the fields are added to the HashMap entry if they are present
		for (int i = 0; i < fieldNames.length; i++) {
			if (isFieldExcluded(className, fieldNames[i])) {
				filters.get(className).remove(fieldNames[i]);				
			}
		}
		
		// If there is no filtered field left, the HashMap entry is removed
		if (filters.get(className) != null && filters.get(className).size() == 0) {
			filters.remove(className);
		}
	}
	
	/**
	 * This method checks whether the given field is excluded.
	 * 
	 * @param className Object class name
	 * @param fieldName Field name within the object
	 * @return True if the field is excluded, false otherwise
	 */
	public boolean isFieldExcluded(String className, String fieldName) {
		// If there is no HashMap entry for the class name, the field is not excluded
		if (filters.get(className) == null) {
			return false;
		}		
		// If the HashMap entry does not contain the field name, the field is not excluded (otherwise, it is)
		return filters.get(className).contains(fieldName);
	}
}
