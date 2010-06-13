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
 * This class represents a group of persons and is used to test FlexJSON.
 * 
 * @author Charlie Hubbard, Thomas Endres
 *
 */
public class Group {
	/**
	 * Group name
	 */
    private String groupName;
    /**
     * List of persons in the group
     */
    private Person[] people;

    /**
     * Dummy constructor
     */
    public Group() {
    }
    
    /**
     * Dummy constructor setting the attributes of the object.
     * 
     * @param groupName Group name
     * @param people List of persons in the group
     */
    public Group(String groupName, Person... people) {
    	// Class variables are set
        this.groupName = groupName;
        this.people = people;
    }
    
    /**
     * This method returns the group name.
     * 
     * @return Group name
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * This method sets the group name to the given value.
     * 
     * @param groupName Group name
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * This method returns the list of people in the group.
     * 
     * @return List of people in the group
     */
    public Person[] getPeople() {
        return people;
    }

    /**
     * This method sets the list of people in the group to the given value.
     * 
     * @param people List of people in the group
     */
    public void setPeople(Person[] people) {
        this.people = people;
    }
}
