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
package firep4j

import firep4j.tools.Versioner;
import groovy.util.GroovyTestCase;

/**
 * This class tests the versioning utility methods for FireP4j.
 * 
 * @author Thomas Endres
 */
class VersionerTests extends GroovyTestCase {
	/**
	 * This method sets up test variables.
	 */
    protected void setUp() {
        super.setUp()
    }

    /**
     * This method tears down a test after performing it
     */
    protected void tearDown() {
        super.tearDown()
    }
    
    /**
     * This method tests the normalization of different version strings.
     */
    void testStandardizeVersion() {
    	// Several version strings are normalized and asserted    	
    	assertEquals("standard version string", "0.4", Versioner.standardizeVersion("0.4"))
    	assertEquals("version string with different separator characters", "0.3.4.5", Versioner.standardizeVersion("0-3+4 5"))
    	assertEquals("version containing letters", "0.4.RC", Versioner.standardizeVersion("0.4RC"))
    	assertEquals("version containing letters and numbers", "0.4.RC.1", Versioner.standardizeVersion("0.4RC1"))
    	assertEquals("version containing dots in inappropriate places", "0.0.40.0.1.0", Versioner.standardizeVersion(".0.40..1."))
    	assertEquals("version containing forbidden characters", "0.0.4", Versioner.standardizeVersion("0.%.4%"))
	}
    
    /**
     * This method tests the version comparison of different version strings.
     */
    void testCompareVersions() {
    	// Several version strings are compared to other version strings
    	assertEquals("simple version equality test", 0, Versioner.compareVersions("4.2.0", "4.2.0"))
    	assertEquals("complex version equality test", 0, Versioner.compareVersions("4.2.0", "4.2.0%"))
    	assertEquals("simple version test 1", -1, Versioner.compareVersions("4.2.0", "5.1.0"))
    	assertEquals("simple version test 2", 1, Versioner.compareVersions("4.2.0", "4.1.0"))
    	assertEquals("complex version test 1", 1, Versioner.compareVersions("4.2.0", "4.2.0x"))
    	assertEquals("complex version test 2", -1, Versioner.compareVersions("4.2RC1", "4.2.RC2"))
    	assertEquals("complex version test 3", -1, Versioner.compareVersions("4.2a", "4.2b"))
    	assertEquals("complex version test 4", 1, Versioner.compareVersions("4.2a", "4.2dev"))
    	assertEquals("complex version test 5", -1, Versioner.compareVersions("4.2", "4.2pl"))
    }
}
