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

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * This class represents a hero's villain and is used to test FlexJSON.
 * 
 * @author Charlie Hubbard, Thomas Endres
 */
public class Villain {
	/**
	 * Villain's name
	 */
    private String name;
    /**
     * Villain's nemesis
     */
    private Hero nemesis;
    /**
     * Villain's secret lair
     */
    private SecretLair lair;
    /**
     * Villain's powers (as a list of super powers)
     */
    private List<SuperPower> powers;
    
    /**
     * Dummy constructor
     */
    protected Villain() {
    	// Class variables are set
        powers = new ArrayList<SuperPower>();
    }
    
    /**
     * Dummy constructor setting different values.
     * 
     * @param name Villain's name
     * @param nemesis Villain's nemesis
     * @param lair Villain's secret lair
     * @param powers Villain's powers (as a list of super powers)
     */
    public Villain(String name, Hero nemesis, SecretLair lair, SuperPower... powers ) {
    	// Class variables are set
        this.name = name;
        this.nemesis = nemesis;
        this.lair = lair;
        this.powers = Arrays.asList( powers );
    }

    /**
     * This method returns villain's name.
     * 
     * @return Villain's name
     */
    public String getName() {
        return name;
    }

    /**
     * This method returns villain's nemesis.
     * 
     * @return Villain's nemesis.
     */
    public Hero getNemesis() {
        return nemesis;
    }

    /**
     * This method returns villain's secret lair.
     * 
     * @return Villain's secret lair
     */
    public SecretLair getLair() {
        return lair;
    }

    /**
     * This method returns villain's powers (as a list of super powers)
     * 
     * @return Villain's powers (as a list of super powers)
     */
    public List<SuperPower> getPowers() {
        return powers;
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

        Villain villian = (Villain) o;

        // The object is checked for equality
        // All the fields of the object are involved
        if (lair != null ? !lair.equals(villian.lair) : villian.lair != null) {
        	return false;
        }
        if (name != null ? !name.equals(villian.name) : villian.name != null) {
        	return false;
        }
        if (nemesis != null ? !nemesis.equals(villian.nemesis) : villian.nemesis != null) {
        	return false;
        }
        if (powers != null ? !powers.equals(villian.powers) : villian.powers != null) {
        	return false;
        }

        // If there was no difference, the objects equal each other
        return true;
    }

    /**
     * This method returns the hash code for the object.
     */
    public int hashCode() {
    	// The hash code is generated from the fields and returned
        int result;
        result = (name != null ? name.hashCode() : 0);
        result = 31 * result + (nemesis != null ? nemesis.hashCode() : 0);
        result = 31 * result + (lair != null ? lair.hashCode() : 0);
        result = 31 * result + (powers != null ? powers.hashCode() : 0);
        return result;
    }
}
