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
 * This class represents the super power heat vision and is used to test FlexJSON.
 * 
 * @author Charlie Hubbard, Thomas Endres
 */
public class HeatVision implements SuperPower {
	/**
	 * Heat vision power
	 */
    private float power;
    
    /**
     * Heat vision strength
     */
    private int strength = 10;

    /**
     * Dummy constructor
     */
    protected HeatVision() {
    }

    /**
     * Dummy constructor setting the heat vision power
     * 
     * @param power Flight velocity
     */
    public HeatVision(float power) {
    	// Class variables are set
        this.power = power;
    }

    /**
     * This method returns the heat vision power.
     * 
     * @return Heat vision power
     */
    public float getPower() {
        return power;
    }

    /**
     * This method returns the heat vision strength.
     * 
     * @return Heat vision strength
     */
    public int getStrength() {
        return strength;
    }

    /**
     * This method sets the heat vision strength.
     * 
     * @return Heat vision power
     */
    public void setStrength(int strength) {
        this.strength = strength;
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

        HeatVision that = (HeatVision) o;
        
        // The object is checked for equality
        if (Float.compare(that.power, power) != 0) {
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
        return (power != +0.0f ? Float.floatToIntBits(power) : 0);
    }
}
