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
package firep4j.objects

import java.io.Serializable

/**
 * Person test class
 * 
 * @author Thomas Endres
 */
public class Person implements Serializable {
	/**
	 * First name (with getters and setters)
	 */
	String firstName = ""
	/**
	 * Last name (with getters and setters)
	 */
	String lastName = ""
	
	/**
	 * House object for the person (with getters and setters)
	 */
	House house = null
		
	/**
	 * Person constructor
	 * 
	 * @param firstName First name
	 * @param lastName Last name
	 * @param house House object for the person
	 */
	public Person(String firstName, String lastName, House house) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.house = house;
	}
}