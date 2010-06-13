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

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Collection;

/**
 * This method creates a chained set from a parent and a child set. The parent set is never changed.
 * All the changes only affect the child set.
 * 
 * @author Thomas Endres
 */
public class ChainedSet implements Set {
	/**
	 * Parent set
	 */
    Set parent;
    /**
     * Child set
     */
    Set child;

    /**
     * This constructor creates the chained set from the given parent set.
     * 
     * @param parent
     */
    public ChainedSet(Set parent) {
    	// Class variables are set
        this.parent = parent;
        this.child = new HashSet();
    }

    /**
     * This method determines the size of the chained set.
     * 
     * @return Size of the chained set
     */
    public int size() {
    	// The size of child and parent sets are added
        return this.child.size() + parent.size();
    }

    /**
     * This method determines whether the chained set is empty.
     * 
     * @return Flag determining whether the chained set is empty
     */
    public boolean isEmpty() {
    	// The flag is determined for both child and parent set
        return this.child.isEmpty() && parent.isEmpty();
    }

    /**
     * This method determines whether the chained set contains a specific object.
     * 
     * @return Flag determining whether the chained contains a specific object
     */    
    public boolean contains(Object o) {
    	// The flag is determined for both child and parent set
        return child.contains(o) || parent.contains(o);
    }

    /**
     * This method creates a chained iterator from the chained set.
     * 
     * @return Chained iterator
     */
    public Iterator iterator() {
    	// The chained iterator is created and returned
        return new ChainedIterator( child, parent );
    }
    
    /**
     * This method creates an array from the chained set.
     * 
     * @return Object array containing the elements of the chained set
     */
    public Object[] toArray() {
    	// The sets are converted to arrays
        Object[] carr = child.toArray();
        Object[] parr = parent.toArray();
        // The arrays are combined and returned
        Object[] combined = new Object[ carr.length + parr.length ];
        System.arraycopy( carr, 0, combined, 0, carr.length );
        System.arraycopy( parr, 0, combined, carr.length, parr.length );
        return combined;
    }

    /**
     * Not implemented method
     */
    public Object[] toArray(Object[] a) {
        throw new IllegalStateException( "Not implemeneted" );
    }

    /**
     * This method adds an object to the chained set.
     * 
     * @return True if adding the object was successful, false otherwise
     */
    public boolean add(Object o) {
    	// The object is added to the child set
        return child.add(o);
    }

    /**
     * This method removes an object to the chained set.
     * 
     * @return True if removing the object was successful, false otherwise
     */
    public boolean remove(Object o) {
    	// The object is removed from the child set
        return child.remove(o);
    }

    /**
     * This method checks whether the chained set contains all the objects of the given collection.
     * 
     * @return True if the chained set contains all the objects of the given collection, false otherwise
     */
    public boolean containsAll(Collection c) {
        return child.containsAll(c) || parent.containsAll(c); 
    }

    /**
     * This method adds all the objects of the given collection to the chained set.
     * 
     * @param c Collection containing all the objects to add
     * @return True if adding the objects was successful, false otherwise
     */
    public boolean addAll(Collection c) {
    	// The object are added to the child set
        return child.addAll(c);
    }

    /**
     * This method retains all the objects of the given collection from the chained set.
     * 
     * @param c Collection containing all the objects to retain
     * @return True if retaining the objects was successful, false otherwise
     */
    public boolean retainAll(Collection c) {
    	// The object are removed from the child set
        return child.retainAll( c );
    }

    /**
     * This method removes all the objects of the given collection from the chained set.
     * 
     * @param c Collection containing all the objects to remove
     * @return True if removing the objects was successful, false otherwise
     */
    public boolean removeAll(Collection c) {
    	// The object are removed from the child set
        return child.removeAll(c);
    }

    /**
     * This method clears the chained set.
     */
    public void clear() {
    	// The child set is cleared
        child.clear();
    }

    /**
     * This method returns the parent set.
     */
    public Set getParent() {
        return parent;
    }
}
