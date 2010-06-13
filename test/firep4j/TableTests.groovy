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

import firep4j.tools.JSONUtils;
import firep4j.Table;
import groovy.util.GroovyTestCase

/**
 * This class tests the Table structure for FireP4j.
 * 
 * @author Thomas Endres
 */
class TableTests extends GroovyTestCase {
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
     * This method tests the serialization of a table structure.
     */
    void testSerialize() {
    	def response = ""
    	
    	// Table structure 1 is created
        Table table1 = new Table(2);
    	table1.setHeaders("H1", "H2")
        table1.addRow("00")
    	table1.addRow("10", "11")
    	table1.addRow("20", "21") 
    	
     	// A JSON utility object is created
    	JSONUtils jsonUtils = new JSONUtils()
 
        // The table is serialized
        response = table1.serialize(jsonUtils)
        
        // The output of the serializer is tested
        assertEquals("Table serialization", "[[\"H1\",\"H2\"],[\"00\",null],[\"10\",\"11\"],[\"20\",\"21\"]]", response)
        
    	// Table structure 2 is created
        Table table2 = new Table(2)
        table2.setHeaders("H1")
     
         // The table is serialized
        response = table2.serialize(jsonUtils)
        
        // The output of the serializer is tested
        assertEquals("Table serialization" + response, "[[\"H1\",\"\"]]", response)
        
    	// Table structure 3 is created
        Table table3 = new Table(2)
     
         // The table is serialized
        response = table3.serialize(jsonUtils)
        
        // The output of the serializer is tested
        assertEquals("Table serialization", "[[\"\",\"\"]]", response)
    }
}