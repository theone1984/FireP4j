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

import java.util.Date;

/**
 * This class represents an employee and is used to test FlexJSON.
 * 
 * @author Charlie Hubbard, Thomas Endres
 */
public class Employee extends Person {
	/**
	 * Employee's company
	 */
    String company;

    /**
     * Dummy constructor
     */
    public Employee() {
    }
    
	/**
     * Dummy constructor setting the attributes of the object.
     * 
	 * @param firstname Person's first name
	 * @param lastname Person's last name
	 * @param birthdate Person's birthdate
	 * @param home Person's home address
	 * @param work Person's work address
	 * @param company Employee's company
	 */
    public Employee(String firstname, String lastname, Date birthdate, Address home, Address work, String company) {
    	// The parent constructor is called
        super(firstname, lastname, birthdate, home, work);
        // Class variables are set
        this.company = company;
    }

    /**
     * This method returns the employee's company.
     * 
     * @return Employee's company
     */
    public String getCompany() {
        return company;
    }

    /**
     * This method sets the employee's company to the given value.
     * 
     * @param company Employee's company
     */
    public void setCompany(String company) {
        this.company = company;
    }
}
