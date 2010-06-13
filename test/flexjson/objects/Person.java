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

import flexjson.JSON;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * This class represents a person and is used to test FlexJSON.
 * 
 * @author Charlie Hubbard, Thomas Endres
 */
public class Person {
	/**
	 * Person's first name
	 */
    private String firstName;
    /**
     * Person's last name
     */
    private String lastName;
    /**
     * Person's birthdate
     */
    private Date birthDate;
    /**
     * Person's home address
     */
    private Address home;
    /**
     * Person's work address
     */
    private Address work;
    /**
     * Person's phone numbers (list of phone entries)
     */
    private List phones = new ArrayList();
    /**
     * Person's hobbies (list of hobbies)
     */
    private List hobbies = new ArrayList();

    /**
     * Dummy constructor
     */
    public Person() {
    }

	/**
     * Dummy constructor setting the attributes of the object.
     * 
	 * @param firstname Person's first name
	 * @param lastname Person's last name
	 * @param birthdate Person's birthdate
	 * @param home Person's home address
	 * @param work Person's work address
	 */
    public Person(String firstname, String lastname, Date birthdate, Address home, Address work) {
    	// Class variables are set
        this.firstName = firstname;
        this.lastName = lastname;
        this.birthDate = birthdate;
        setHome(home);
        setWork(work);
    }

    /**
     * This method returns the person's first name.
     * 
     * @return Person's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * This method sets the person's first name to the given value.
     * 
     * @param firstname Person's first name
     */
    public void setFirstName(String firstname) {
        this.firstName = firstname;
    }

    /**
     * This method returns the person's last name.
     * 
     * @return Person's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * This method sets the person's last name to the given value.
     * 
     * @param lastname Person's last name
     */
    public void setLastName(String lastname) {
        this.lastName = lastname;
    }

    /**
     * This method returns the person's birthdate.
     * 
     * @return Person's birthdate
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * This method sets the person's birthdate to the given value.
     * 
     * @param birthdate Person's birthdate
     */
    public void setBirthDate(Date birthdate) {
        this.birthDate = birthdate;
    }

    /**
     * This method returns the person's home address.
     * 
     * @return Person's home address
     */
    public Address getHome() {
        return home;
    }

    /**
     * This method sets the person's home address to the given value.
     * 
     * @param firstName Person's birthdate
     */
    public void setHome(Address home) {
        this.home = home;
        if( home != null ) this.home.setPerson( this );
    }

    /**
     * This method returns the person's work address.
     * 
     * @return Person's work address
     */
    public Address getWork() {
        return work;
    }

    /**
     * This method sets the person's work address to the given value.
     * 
     * @param firstName Person's work address
     */
    public void setWork(Address work) {
        this.work = work;
        if( work != null ) this.work.setPerson( this );
    }

    /**
     * This method returns the person's phone numbers.
     * 
     * @return Person's phone numbers
     */
    public List getPhones() {
        return phones;
    }

    /**
     * This method sets the person's phone numbers to the given value.
     * 
     * @param phones Person's phone numbers
     */
    public void setPhones(List phones) {
        this.phones = phones;
    }

    /**
     * This method returns the person's hobbies.
     * 
     * @return Person's hobbies
     */
    @JSON(include = false)
    public List getHobbies() {
        return hobbies;
    }

    /**
     * This method sets the person's hobbies to the given value.
     * 
     * @param phones Person's phone numbers
     */
    public void setHobbies(List hobbies) {
        this.hobbies = hobbies;
    }
}
