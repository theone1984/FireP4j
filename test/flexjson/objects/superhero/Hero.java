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

/**
 * This class represents a hero and is used to test FlexJSON.
 * 
 * @author Charlie Hubbard, Thomas Endres
 */
public class Hero {
	/**
	 * Hero's secret lair
	 */
    private SecretLair lair;
	/**
	 * Hero's secret identity
	 */
    private SecretIdentity identity;
	/**
	 * Hero's name
	 */
    private String name;
	/**
	 * Hero's powers (as a list of super powers)
	 */
    private List<SuperPower> powers;

    /**
     * Dummy constructor
     */
    protected Hero() {
    }

    /**
     * Dummy constructor setting different values.
     * 
     * @param name Hero's name
     * @param identity Hero's secret identity
     * @param lair Hero's secret lair
     * @param powers Hero's powers
     */
    public Hero(String name, SecretIdentity identity, SecretLair lair, SuperPower... powers) {
    	// Class variables are set
        this.name = name;
        this.identity = identity;
        this.lair = lair;
        this.powers = Arrays.asList( powers );
    }

    /**
     * This method returns hero's secret lair.
     * 
     * @return Hero's secret lair
     */
    public SecretLair getLair() {
        return lair;
    }

    /**
     * This method returns hero's secret identity.
     * 
     * @return Hero's secret identity
     */
    public SecretIdentity getIdentity() {
        return identity;
    }

    /**
     * This method returns hero's name.
     * 
     * @return Hero's name
     */
    public String getName() {
        return name;
    }

    /**
     * This method returns hero's powers (as a list of super powers)
     * 
     * @return Hero's powers (as a list of super powers)
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

        Hero hero = (Hero) o;

        // The object is checked for equality
        // All the fields of the object are involved
        if (identity != null ? !identity.equals(hero.identity) : hero.identity != null){
        	return false;
        }
        if (lair != null ? !lair.equals(hero.lair) : hero.lair != null) {
        	return false;
        }
        if (name != null ? !name.equals(hero.name) : hero.name != null) {
        	return false;
        }
        if (powers != null ? !powers.equals(hero.powers) : hero.powers != null) {
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
        result = (lair != null ? lair.hashCode() : 0);
        result = 31 * result + (identity != null ? identity.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (powers != null ? powers.hashCode() : 0);
        return result;
    }
}
