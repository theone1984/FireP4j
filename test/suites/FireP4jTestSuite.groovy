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
package suites

import junit.framework.*;

/**
 * This class is used as a TestSuite for all the FireP4j tests.
 * 
 * @author Thomas Endres
 */
class FireP4jTestSuite extends TestSuite {
	/**
	 * Test suite root directory
	 */
    private static final String TEST_ROOT = "test/firep4j/";
    
    /**
     * This method returns the test suite.
     * 
     * @return The resulting test suite
     */
    public static Test suite()  {
    	// The test suite is created
    	TestSuite suite = new TestSuite()
        GroovyTestSuite gsuite = new GroovyTestSuite()
    	
        // All unit test classes are added to the test suite
        suite.addTestSuite(gsuite.compile(TEST_ROOT + "VersionerTests.groovy"))
        suite.addTestSuite(gsuite.compile(TEST_ROOT + "JSONUtilsTests.groovy"))
        suite.addTestSuite(gsuite.compile(TEST_ROOT + "TableTests.groovy"))
        suite.addTestSuite(gsuite.compile(TEST_ROOT + "FireP4jTests.groovy"))
        
        // The resulting test suite is returned
        suite
    }
}
