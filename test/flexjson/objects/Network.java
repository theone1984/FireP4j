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

import java.util.List;
import java.util.ArrayList;

/**
 * This class represents a network of persons and is used to test FlexJSON.
 * 
 * @author Charlie Hubbard, Thomas Endres
 */
public class Network {
	/**
	 * Name of the network
	 */
    String name;
    /**
     * List of people in the network
     */
    List people;
    
    /**
     * Dummy constructor
     */
    public Network() {
    }
    
    /**
     * Dummy constructor setting the attributes of the object.
     * 
     * @param name Name of the network
     * @param people List of people in the network
     */
    public Network(String name, Person... people) {
    	// Class variables are set
        this.name = name;
        // Each person is added to the list of persons in the network
        this.people = new ArrayList();
        for(Person person : people) {
        	this.people.add(person);
        }
    }

    /**
     * This method returns the name of the network.
     * 
     * @return Name of the network
     */
    public String getName() {
        return name;
    }

    /**
     * This method sets the name of the network to the given value.
     * 
     * @param name Name of the network
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method returns the list of people in the network.
     * 
     * @return List of people in the network
     */
    public List getPeople() {
        return people;
    }

    /**
     * This method sets the list of people in the network to the given value.
     * 
     * @param people List of people in the network
     */
    public void setPeople(List people) {
        this.people = people;
    }
}
