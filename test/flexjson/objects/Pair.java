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
package flexjson.objects;

/**
 * This class contains two objects of the types given and is used to test FlexJSON. 
 *
 * @param <T> Type of the first object
 * @param <U> Type of the second object
 * @author Charlie Hubbard, Thomas Endres
 */
public class Pair <T,U> {
	/**
	 * First object (of the first type)
	 */
    private T first;
    /**
     * Second object (of the second type)
     */
    private U second;

    /**
     * Dummy constructor
     */
    protected Pair() {
    }

    /**
     * Dummy constructor setting the attributes of the object.
     * 
     * @param first First object
     * @param second Second object
     */
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    /**
     * This method returns the first object.
     * 
     * @return First object
     */
    public T getFirst() {
        return first;
    }

    /**
     * This method sets the first object to the given value.
     * 
     * @param first First object
     */
    protected void setFirst(T first) {
        this.first = first;
    }

    /**
     * This method returns the second object.
     * 
     * @return Second object.
     */
    public U getSecond() {
        return second;
    }

    /**
     * This method sets the second object to the given value.
     * 
     * @param first Second object
     */
    protected void setSecond(U second) {
        this.second = second;
    }

    /**
     * This method checks whether the given object equals the current object.
     * 
     * @param o Object to check
     */
    public boolean equals(Object o) {
    	// If the same object is given as an argument, they are equal
        if (this == o) {
        	return true;
        }
        // If the class name of the object is not the same, they are not equal
        if (o == null || getClass() != o.getClass()) {
        	return false;
        }

        Pair pair = (Pair) o;

        // The object is checked for equality
        // All the fields of the object are involved
        if (first != null ? !first.equals(pair.first) : pair.first != null) {
        	return false;
        }
        if (second != null ? !second.equals(pair.second) : pair.second != null) {
        	return false;
        }

        // If there was no difference, the objects equal each other
        return true;
    }

    /**
     * This hashCode method returns the hash code of the path.
     * (Needed for DB purposes)
     */
    public int hashCode() {
    	// The hash code is generated from the fields and returned
        int result;
        result = (first != null ? first.hashCode() : 0);
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }
}
