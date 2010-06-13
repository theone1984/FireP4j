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
package firep4j.tools


import flexjson.*
import flexjson.visitors.*;

/**
 * This class is used for JSON serialization.
 * 
 * @author Thomas Endres
 */
public class JSONUtils {
	/**
	 * The visitor object for serializing using FlexJSON
	 */
	private CustomVisitor visitor = null	
	
	public JSONUtils() {
		// The visitor class is created
		visitor = new CustomVisitor()
		
		// The object filters are created and the metaClass property is added as a filter
		LinkedList<String> filters = new LinkedList<String>()
	}
	
	/**
	 * This method serializes the given object using JSON (and the FlexJSON library).
	 * 
	 * @param object Object to be serialized
	 * @return The serialized string value
	 */
	public String serialize(Object object) {
		// The object is serialized	
		String objectString = JSONSerializer.serialize(visitor, object)
		
		// The empty string is put in parentheses
		if (objectString == "") {
			objectString = "\"\""
		}
		
		// The resulting object is returned
		objectString
	}
	
	/**
	 * This method sets the max depth for JSON serialization (-1 for infinite depth).
	 * 
	 * @param maxDepth Maximum depth
	 */
	public void setMaxDepth(int maxDepth) {
		// Each value greater than 0 and -1 (for infinite depth) is allowed
		if (maxDepth <= 0 && maxDepth != -1) {
			return;
		}
		
		visitor.setMaxDepth(maxDepth)
	}
	
	/**
	 * This method sets an object filter.
	 * 
	 * @param className Class name for the object filter
	 * @param fieldNames Field names that should be filtered
	 */
	public void addObjectFilter(String className, String... fieldNames) {
		visitor.addObjectFilter(className, fieldNames);
	}
	
	/**
	 * This method removes an existing object filter.
	 * 
	 * @param className Class name for the object filter
	 * @param fieldNames Field names that should not be filtered anymore
	 */
	public void removeObjectFilter(String className, String... fieldNames) {
		visitor.removeObjectFilter(className, fieldNames);
	}
	
	/**
	 * This method prevents the specified path from being serialized.
	 * 
	 * @param pathFilter The path to exclude
	 */
	public void addPathFilter(String pathFilter) {
		// The path filter is removed
		if (pathFilter != null) {
			visitor.excludePath(pathFilter)
		}
	}	
}
