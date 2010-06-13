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
package flexjson.tools;

import java.util.List;
import java.util.LinkedList;

/**
 * This class is used by FlexJSON to represent a path to a field within a serialized stream.
 */
public class Path {
	/**
	 * List containing all the path elements
	 */
    LinkedList<String> path = new LinkedList<String>();

    /**
     * This method parses a path (explodes it using dots) and returns the resulting path object.
     * 
     * @param path Path string in dot notation
     * @return Resulting path
     */
    public static Path parse(String path) {
    	// The path is split and returned
        return path != null ? new Path( path.split("\\." ) ) : new Path();
    }
    
    /**
     * Dummy constructor
     */
    public Path() {
    }

    /**
     * This constructor sets the path to the given path elements
     * 
     * @param fields Path elements
     */
    public Path(String... fields) {
    	// Every path element is added to the path
        for (String field : fields) {
            path.add(field);
        }
    }

    /**
     * This method enqueues a new path element to the path.
     * 
     * @param field Path element to enqueue
     * @return The resulting path
     */
    public Path enqueue(String field) {
        path.add(field);
        return this;
    }

    /**
     * This method removes the last path element from the path.
     * 
     * @return The last path element
     */
    public String pop() {
        return path.removeLast();
    }

    /**
     * This method returns the current path
     * 
     * @return Current path
     */
    public List<String> getPath() {
        return path;
    }

    /**
     * This method returns the length of the current path
     * 
     * @return Current path
     */
    public int length() {
        return path.size();
    }

    /**
     * This method returns a dot notation string of the current path.
     * 
     * @return Dot notation string of the current path
     */
    public String toString() {
    	// The string builder is initialized
        StringBuilder builder = new StringBuilder ( "[ " );
        boolean afterFirst = false;
        // For all the path entries
        for( String current : path ) {
        	// The entry is added to the dot notation string (using dots as separators)
        	
            if( afterFirst ) {
                builder.append( "." );
            }
            builder.append( current );
            afterFirst = true;
        }
        // The build is ended and the resulting string is returned
        builder.append( " ]" );
        return builder.toString();
    }

    /**
     * This method checks whether the given path equals the current path.
     * 
     * @param o Path to check
     */
    public boolean equals(Object o) {
    	// If the same path is given as an argument, they are equal
        if (this == o) {
        	return true;
        }
        // If the class name of the object is not the same, they are not equal
        if (o == null || getClass() != o.getClass()) {
        	return false;
        }

        Path path1 = (Path) o;        
        // The path entries are checked for equality
        if (!path.equals(path1.path)) {
        	return false;
        }
        
        // If there was no difference, the paths equal each other
        return true;
    }
    
    /**
     * This hashCode method returns the hash code of the path.
     * (Needed for DB purposes)
     */
    public int hashCode() {
        return path.hashCode();
    }
}
