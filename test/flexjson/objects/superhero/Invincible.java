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
 * This class represents the super power invincibility and is used to test FlexJSON.
 * 
 * @author Charlie Hubbard, Thomas Endres
 */
public class Invincible implements SuperPower {

    /**
     * This method checks whether the given path equals the current path.
     * 
     * @param o Path to check
     */
    public boolean equals(Object object) {
    	// Every invincibility object is equal to each other invincibility object
        return object instanceof Invincible;
    }
    
    /**
     * This hashCode method returns the hash code of the path.
     * (Needed for DB purposes)
     */
    public int hashCode() {
        return 0;
    }


}
