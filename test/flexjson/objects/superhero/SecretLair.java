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
 * This class represents the super power secret lair and is used to test FlexJSON.
 * 
 * @author Charlie Hubbard, Thomas Endres
 */
public class SecretLair {
	/**
	 * Name of the secret lair
	 */
    private String name;

    /**
     * Dummy constructor
     */
    protected SecretLair() {
    }

    /**
     * Dummy constructor setting the name of the secret lair
     * 
     * @param name Name of the secret lair
     */
    public SecretLair( String name ) {
    	// Class variables are set
        this.name = name;
    }

    /**
     * This method returns the name of the secret lair.
     * 
     * @return Secret identity's name
     */
    public String getName() {
        return name;
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

        SecretLair that = (SecretLair) o;

        // The object is checked for equality
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        // If there was no difference, the objects equal each other
        return true;
    }

    /**
     * This hashCode method returns the hash code of the path.
     * (Needed for DB purposes)
     */
    public int hashCode() {
        return (name != null ? name.hashCode() : 0);
    }
}
