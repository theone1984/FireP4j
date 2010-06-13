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
 * This class represents an address and is used to test FlexJSON.
 * 
 * @author Charlie Hubbard, Thomas Endres
 */
public class Address {
	/**
	 * Person that belongs to the address
	 */
    private Person person;
    /**
     * Street name
     */
    private String street;
    /**
     * City name
     */
    private String city;
    /**
     * State name
     */
    private String state;
    /**
     * ZIP code
     */
    private Zipcode zipCode;

    /**
     * Dummy constructor
     */
    public Address() {
    }

    /**
     * Dummy constructor setting the attributes of the object.
     * 
     * @param street Street name
     * @param city City name
     * @param state State name
     * @param zipcode ZIP code
     */
    public Address(String street, String city, String state, Zipcode zipcode) {
    	// Class variables are set
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipcode;
    }
    
    /**
     * This method sets the person belonging to the address to the given value.
     * 
     * @return Person belonging to the address
     */
    public Person getPerson() {
        return person;
    }
    
    /**
     * This method returns the person belonging to the address.
     * 
     * @param person Person belonging to the address
     */
    public void setPerson(Person person) {
        this.person = person;
    }

    /**
     * This method returns the street name.
     * 
     * @return Street name
     */
    public String getStreet() {
        return street;
    }

    /**
     * This method sets the street name to the given value.
     * 
     * @param street Street name
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * This method returns the city name.
     * 
     * @return City name
     */
    public String getCity() {
        return city;
    }

    /**
     * This method sets the city name to the given value.
     * 
     * @param city City name
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * This method returns the state name.
     * 
     * @return State name
     */
    public String getState() {
        return state;
    }

    /**
     * This method sets the state name to the given value.
     * 
     * @param state State name
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * This method returns the ZIP code.
     * 
     * @return ZIP code
     */
    public Zipcode getZipCode() {
        return zipCode;
    }

    /**
     * This method sets the ZIP code to the given value.
     * 
     * @param zipcode ZIP code
     */
    public void setZipCode(Zipcode zipcode) {
        this.zipCode = zipcode;
    }
}
