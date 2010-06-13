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

/**
 * This class represents a simple filter that contains different containment filter types.
 * 
 * @author Thomas Endres
 */
class Filter implements Serializable {
	/**
	 * All possible filter criteria
	 */
	public enum Criteria {
		/**
		 * A filter where the expression starts with the given value
		 */
		STARTS_WITH,
		/**
		 * A filter where the expression contains the given value
		 */
		CONTAINS,
		/**
		 * A filter where the expression ends with the given value
		 */
		ENDS_WITH,
		/**
		 * A filter where the expression equals the given value
		 */
		EQUALS
	}
	
	/**
	 * The filter criterium type
	 */
	public Criteria type
	
	/**
	 * The filter value 
	 */
	public String value
}
