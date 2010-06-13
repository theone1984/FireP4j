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

import java.util.Iterator;
import java.util.Set;

/**
 * This class contains a chained iterator set.
 * 
 * @author Thomas Endres
 */
public class ChainedIterator implements Iterator {
	/**
	 * Array of iterators
	 */
    Iterator[] iterators;
    /**
     * Currently used iterator
     */
    int current = 0;

    /**
     * This constructor creates the array of iterators from the given sets.
     * 
     * @param sets Sets used for building the chained iterator
     */
    public ChainedIterator(Set... sets) {
    	// All the iterators are added to the iterator array
        iterators = new Iterator[sets.length];
        for( int i = 0; i < sets.length; i++ ) {
            iterators[i] = sets[i].iterator();
        }
    }
    
    /**
     * This method checks whether there are elements remaining.
     * If there are no elements remaining for the current iterator, the next iterator is taken.
     */
    public boolean hasNext() {
        if( iterators[current].hasNext() ) {
        	// If there are elements remaining for the current iterator, they are returned
            return true;
        } else {
        	// If there are no elements remaining for the current iterator, the next iterator is taken
            current++;
            // It is checked whether there is a new iterator and whether it contains elements
            return current < iterators.length && iterators[current].hasNext();
        }
    }

    /**
     * This method returns the next object for the current iterator and increments the iterator.
     * 
     * @return Next object
     */
    public Object next() {
        return iterators[current].next();
    }

    /**
     * This method removes the current object from the current iterator.
     */
    public void remove() {
        iterators[current].remove();
    }
}
