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
 * This class is the FireP4j profile class for Groovy implementations.
 */
class GroovyProfile extends JavaProfile {
	/**
	 * This constructor initializes the path and trace filters.
	 */
	public GroovyProfile() {
		// The base filters are set
		super()
		
		// Path filters are added (mainly Groovy metaclass information)
		pathFilters = pathFilters + [
			'*.metaClass',
			'*.$*',
			'*.__timeStamp*',
		]
		// Trace filters are added (native Groovy methods)
		traceFilters = traceFilters + [
			new Filter(type: Filter.Criteria.STARTS_WITH, value: "org.codehaus"),
			new Filter(type: Filter.Criteria.STARTS_WITH, value: "groovy."), 
		]
	}
}
