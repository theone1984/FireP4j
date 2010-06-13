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

import junit.framework.Test;
import junit.framework.TestSuite;

import junit.framework.*;
import flexjson.*;

/**
 * This class is used as a TestSuite for all the FlexJSON tests.
 * 
 * @author Thomas Endres
 */
class FlexJSONTestSuite extends TestSuite {
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
        suite.addTestSuite(JSONSerializerTests.class)
        suite.addTestSuite(ObjectFiltersTests.class)
        suite.addTestSuite(PathExpressionTests.class)
        suite.addTestSuite(PathTests.class)                                        
        
        // The resulting test suite is returned
        suite
    }
}
