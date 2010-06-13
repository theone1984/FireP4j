/*
 * Copyright 2007 Charlie Hubbard, modified in 2010 by Thomas Endres
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

import junit.framework.TestCase;
import flexjson.tools.Path;

/**
 * This method tests FlexJSON paths.
 * 
 * @author Charlie Hubbard, Thomas Endres
 */
public class PathTests extends TestCase {
	/**
	 * This method tests path equality.
	 */
    public void testEquals() {
    	// Various paths are created
        Path path = new Path("foo", "bar", "baz");
        Path equalPath = new Path("foo", "bar", "baz");
        Path notEqualPath = new Path("Foo", "bar", "hizzle");
        Path tooBigPath = new Path("foo", "bar", "baz", "sherly");

        // The paths are asserted
        assertEquals("two paths with the same data are equal", equalPath, path);
        assertFalse("two paths with the same data are equal", path.equals(notEqualPath));
        assertFalse("two paths with the same data are equal", path.equals(tooBigPath));
        assertFalse("if we enqueue onto a path then we get something not equal", path.equals(equalPath.enqueue("snickers")));
        
        // The last element is popped from the path
        equalPath.pop();
        
        // The shortened path is asserted
        assertEquals("if we pop that same path then we get equal again", path, equalPath);
    }
    
    /**
     * This method tests various enqueue and pop operations on paths.
     */
    public void testEnqueueAndPop() {
    	// The path is created
        Path path = new Path();
        
        // The path is popped and enqueued in various ways, the results are asserted
        assertEquals("length is 0 when empty", 0, path.length());
        assertEquals("after appending we have a length of 1", 1, path.enqueue("hello").length());
        assertEquals("after appending we have a length of 2", 2, path.enqueue("world").length());
        assertEquals("after we pop we have the last thing we placed on there", "world", path.pop());
        assertEquals("after we pop we are back to 1", 1, path.length());
        assertEquals("after we pop we have the last thing we placed on there", "hello", path.pop());
        assertEquals("Assert that after we pop we are back to 0", 0, path.length());
        
        // A new path is created and its length is asserted
        Path foobarbaz = new Path("foo", "bar", "baz");
        assertEquals("Assert that our path is 3.", 3, foobarbaz.length());
    }
}
