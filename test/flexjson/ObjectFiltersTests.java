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
package flexjson;

import flexjson.filters.ObjectFilters;
import junit.framework.TestCase;

/**
 * This class tests FlexJSON object filtering
 * 
 * @author Thomas Endres
 */
public class ObjectFiltersTests extends TestCase {
	/**
	 * Object filters
	 */
	private ObjectFilters objectFilters = null;
	
	/**
	 * This method sets up test variables.
	 */
	protected void setUp() throws Exception {
		// The parent constructor is called
		super.setUp();
		// The object filters are created
		objectFilters = new ObjectFilters();
	}
	
	/**
	 * This method tests the addition of object filters.
	 */
	public void testAddObjectFilter() {
		// An object filter is created and asserted
		objectFilters.addObjectFilter("class1", "field1");
		assertTrue("add object", objectFilters.isFieldExcluded("class1", "field1"));
		
		// Additional object filters are created and asserted
		objectFilters.addObjectFilter("class1", "field2", "field3");
		assertTrue("additional objects occur", objectFilters.isFieldExcluded("class1", "field2"));
		assertTrue("additional objects occur", objectFilters.isFieldExcluded("class1", "field3"));
		
		// An already existing object filter is created and the existence of all the object filters so far is asserted
		objectFilters.addObjectFilter("class2", "field1");
		assertTrue("additional objects occur", objectFilters.isFieldExcluded("class2", "field1"));
		assertFalse("additional objects occur", objectFilters.isFieldExcluded("class2", "field2"));
		assertFalse("additional objects occur", objectFilters.isFieldExcluded("class2", "field3"));
	}

	/**
	 * This method tests the removal of object filters.
	 */
	public void testRemoveObjectFilter() {
		// A non existing object filter is removed and asserted (this should not throw an exception)
		try {
			objectFilters.removeObjectFilter("class1", "field1");
		} catch (Exception e) {
			fail("removing non existing object filter threw an exception");
		}
		assertTrue("removal of non-existing object", !objectFilters.isFieldExcluded("class1", "field2"));
		
		// An object filter is added and removed, remaining object filters are asserted
		objectFilters.addObjectFilter("class1", "field2", "field3");		
		objectFilters.removeObjectFilter("class2", "field2");
		
		assertTrue("removal of existing object", objectFilters.isFieldExcluded("class1", "field2"));
		assertTrue("removal of existing object", objectFilters.isFieldExcluded("class1", "field3"));
		
		// More object filters are removed and asserted
		objectFilters.removeObjectFilter("class1", "field2", "field3");
		
		assertFalse("removal of object filters", objectFilters.isFieldExcluded("class1", "field2"));
		assertFalse("removal of object filters", objectFilters.isFieldExcluded("class1", "field3"));
		
		// Object filters are added,  removed and asserted
		objectFilters.addObjectFilter("class1", "field2", "field3");
		objectFilters.removeObjectFilter("class1", "field2");
		
		assertFalse("removal of all object filters", objectFilters.isFieldExcluded("class1", "field2"));
		assertTrue("removal of all object filters", objectFilters.isFieldExcluded("class1", "field3"));
	}
}
