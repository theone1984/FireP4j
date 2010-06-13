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
package flexjson.objects.superhero;

/**
 * This class represents the super power flying and is used to test FlexJSON.
 * 
 * @author Charlie Hubbard, Thomas Endres
 */
public class Flight implements SuperPower {
	/**
	 * Flight Velocity
	 */
    private float velocity;

    /**
     * Dummy constructor
     */
    protected Flight() {
    }

    /**
     * Dummy constructor setting the velocity of the flight
     * 
     * @param velocity Flight velocity
     */
    public Flight(float velocity) {
    	// Class variables are set
        this.velocity = velocity;
    }

    /**
     * This method returns the flight velocity.
     * 
     * @return Flight velocity
     */
    public float getVelocity() {
        return velocity;
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

        Flight flight = (Flight) o;

        // The object is checked for equality
        if (Float.compare(flight.velocity, velocity) != 0) {
        	return false;
        }

        // If there was no difference, the objects equal each other
        return true;
    }

    /**
     * This method returns the hash code for the object.
     */
    public int hashCode() {
        return (velocity != +0.0f ? Float.floatToIntBits(velocity) : 0);
    }
}
