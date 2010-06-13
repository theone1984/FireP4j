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
 * This class represents a ZIP code and is used for FlexJSON testing.
 * 
 * @author Thomas Endres
 */
public class Zipcode {
	/**
	 * ZIP code
	 */
    private String zipcode;
    
    /**
     * Dummy constructor
     */
    public Zipcode() {
    }

    /**
     * Dummy constructor setting the ZIP code
     * 
     * @param zipcode ZIP code
     */
    public Zipcode(String zipcode) {
    	// Class variables are set
        this.zipcode = zipcode;
    }
    
    /**
     * This method returns the ZIP code
     * 
     * @return ZIP code
     */
    public String getZipcode() {
        return zipcode;
    }
    
    /**
     * This method sets the ZIP code
     * 
     * @param zipcode ZIP code
     */
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
