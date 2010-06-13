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
package firep4j.objects

import firep4j.profile.GroovyProfile;

/**
 * This class is used for testing the FireP4j profiles.
 * 
 * @author Thomas Endres
 */
class TestProfile extends GroovyProfile {
	/**
	 * This constructor initializes the test profile class.
	 */
	public TestProfile() {
		// Base filters are set
		super()
		
		// Additional path filters are added to the base class filters
		pathFilters = pathFilters + [
			'*.race',
			'*.most*',
		]
	}
}
