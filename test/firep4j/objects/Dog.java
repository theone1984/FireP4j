/*
 * Copyright 2010 Thomas Endres
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
package firep4j.objects;

import java.io.Serializable;

/**
 * This class represents a dog and is used to test JSON visibility and class encoding.
 * 
 * @author Thomas Endres
 */
@SuppressWarnings("serial")
public class Dog implements Serializable {
	/**
	 * Most popular dog name (public, static)
	 */
	public static String mostPopularName = "Puppy";
	
	/**
	 * Name (private)
	 */
	private String name = "";
	
	/**
	 * Race (public)
	 */
	public String race = "";
	
	/**
	 * Tag ID (protected)
	 */
	protected String tagId = "";
	
	/**
	 * Dummy constructor
	 * 
	 * @param name Name
	 * @param race Race
	 * @param tagId Tag ID
	 */
	public Dog(String name, String race, String tagId) {
		// Class variables are set
		this.name = name;
		this.race = race;
		this.tagId = tagId;
	}
	
	// Getters
	
	/**
	 * This method returns the name of the dog.
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method returns the tag ID of the dog.
	 */
	public String getTagId() {
		return tagId;
	}	
}
