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

import firep4j.tools.JSONUtils

import groovy.util.GroovyTestCase

import firep4j.objects.*

/**
 * This class tests the JSON utility methods for FireP4j.
 * 
 * @author Thomas Endres
 */
class JSONUtilsTests extends GroovyTestCase {
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
    
    private JSONUtils initJSONUtils() {
    	// The JSON utility object is created
    	JSONUtils jsonUtils = new JSONUtils()
    	
    	// Path filters are set
    	jsonUtils.addPathFilter('*.metaClass')
    	jsonUtils.addPathFilter('*.$*')
    	jsonUtils.addPathFilter('*.__timeStamp*')
    	
    	jsonUtils
    }
    
    /**
     * This method tests JSON serialization.
     */
    void testSerialize() {
    	String response = ""
    		
    	// Test objects are created
        def house = new House("123 Fake St.")
    	def person = new Person("Bart", "Simpson", house)    
    	
    	// The JSON utility object is created
    	JSONUtils jsonUtils = initJSONUtils() 	
    	
    	// The JSON serialization is asserted

    	// Serialization of null
    	response = jsonUtils.serialize(null)
        assertEquals("null test", "null", response)        

        // Serialization of an integer variable
    	response = jsonUtils.serialize(32)
    	assertEquals("integer test", "32", response)
    	
    	// Serialization of a double variable
    	response = jsonUtils.serialize(3.14)
    	assertEquals("double test", "3.14", response)

    	// Serialization of a boolean variable
    	response = jsonUtils.serialize(Boolean.TRUE)
    	assertEquals("boolean test", "true", response)
    	
    	// Serialization of a string variable
    	response = jsonUtils.serialize("string")
    	assertTrue("string test", response == "\"string\"" || response == "'string'")

    	// Serialization of an array variable
    	String[] array = ["foo", "bar"]  	
     	response = jsonUtils.serialize(array)
     	assertTrue("array test", response == "[\"foo\",\"bar\"]" || response == "['foo','bar']")
     	
    	// Serialization of a shallow object (using a regular expression and occurence tests)
    	def houseObjectPattern = "^\\{(\\\"[^\"]+\\\":\\\"[^\"]+\\\"(,)?)*\\}?"
    	response = jsonUtils.serialize(house)
    	assertTrue("object test", (response.contains("\"private:address\":\"123 Fake St.\"") || response.contains("'private:address':'123 Fake St.'")) &&
    	                          (response.contains("\"__className\":\"firep4j.objects.House\"") || response.contains("'__className':'firep4j.objects.House'")) &&
    	                           response.matches(houseObjectPattern))
    	
    	// Serialization of a deep object (using a regular expression and occurence tests)
    	def personObjectPattern = "^\\{((\\\"[^\"]+\\\":\\\"[^\"]+\\\"(,)?)|\\\"[^\"]+\\\":\\{(\\\"[^\"]+\\\":\\\"[^\"]+\\\"(,)?)*\\}(,)?)*\\}?"
        response = jsonUtils.serialize(person)   
        assertTrue("deep serialize test", (response.contains("\"private:address\":\"123 Fake St.\"") || response.contains("'private:address':'123 Fake St.'")) &&
                                          (response.contains("\"__className\":\"firep4j.objects.House\"") || response.contains("'__className':'firep4j.objects.House'")) &&
                                          (response.contains("\"private:firstName\":\"Bart\"") || response.contains("'private:firstName':'Bart'")) &&
                                          (response.contains("\"private:lastName\":\"Simpson\"") || response.contains("'private:lastName':'Simpson'")) &&
                                          (response.contains("\"__className\":\"firep4j.objects.Person\"") || response.contains("'__className':'firep4j.objects.Person'")) &&
                                          response.matches(personObjectPattern))
    }
}