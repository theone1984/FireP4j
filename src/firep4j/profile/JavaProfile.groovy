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
package firep4j.profile

import firep4j.tools.Filter

/**
 * This class is the FireP4j profile class for Java implementations.
 */
class JavaProfile extends FireP4jProfile {
	/**
	 * This constructor initializes the path and trace filters.
	 */
	public JavaProfile() {
		// The base filters are set 
		super()
		
		// There are no path filters in Java
		pathFilters = []
		
		// Trace filters are set (including reflection methods, native Java methods, Eclipse and Spring framework methods)
		traceFilters = [
			new Filter(type: Filter.Criteria.STARTS_WITH, value: "java."),
			new Filter(type: Filter.Criteria.STARTS_WITH, value: "javax."),
			new Filter(type: Filter.Criteria.STARTS_WITH, value: "sun."),
			new Filter(type: Filter.Criteria.STARTS_WITH, value: "junit."),
			new Filter(type: Filter.Criteria.STARTS_WITH, value: "org.eclipse"),
			new Filter(type: Filter.Criteria.STARTS_WITH, value: "org.springframework"),
		 	new Filter(type: Filter.Criteria.STARTS_WITH, value: "org.apache"),
		]
	}
}
