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

import java.util.ArrayList

import firep4j.tools.Filter

/**
 * This class is the base class for language specific FireP4j profiles.
 * It employs the following features:
 * - Trace filtering (not showing some path entries when tracing)
 * - Path filtering (not showing some object elements in the output)
 */
abstract class FireP4jProfile{
	/**
	 * Trace filters (not showing some path entries when tracing)
	 */
	protected Filter[] traceFilters = null
	/**
	 * Path filters (not showing some object elements in the output)
	 */
	protected String[] pathFilters = null
	
	/**
	 * Dummy constructor
	 */
	public FireP4jProfile() {
	}
	
	// Getters
	
	/**
	 * This method returns all trace filters  (not showing some path entries when tracing).
	 * 
	 * @return Trace filters
	 */
	public ArrayList<Filter> getTraceFilters() { traceFilters }	
	
	/**
	 * This method returns all path filters (not showing some object elements in the output).
	 * 
	 * @return Path filters
	 */
	public ArrayList<String> getPathFilters() { pathFilters }
}
