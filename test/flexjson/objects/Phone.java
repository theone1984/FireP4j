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

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * This class represents a phone and is used to test FlexJSON.
 * 
 * @author Charlie Hubbard, Thomas Endres
 */
public class Phone {
	/**
	 * Phone number type
	 */
    private PhoneNumberType type;
    /**
     * Area code
     */
    private String areaCode;
    /**
     * Exchange number
     */
    private String exchange;
    /**
     * Phone number
     */
    private String number;
    
    /**
     * Phone number pattern that can be used.
     */
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\(?(\\d{3})\\)?[\\s-](\\d{3})[\\s-](\\d{4})");

    /**
     * Dummy constructor
     */
    protected Phone() {
    }
    
    /**
     * Dummy constructor setting the attributes of the object.
     * 
     * @param type Phone number type
     * @param number Complete phone number (with area code and exchange number)
     */
    public Phone(PhoneNumberType type, String number) {
    	// Class variables are set
        this.type = type;
        // The phone number is matched with the phone number pattern
        Matcher matcher = PHONE_PATTERN.matcher(number);
        if(matcher.matches()) {
        	// Different variables are extracted from the phone number
            this.areaCode = matcher.group(1);
            this.exchange = matcher.group(2);
            this.number = matcher.group(3);
        } else {
        	// An exception is thrown if the phone number didn't match the phone number pattern
            throw new IllegalArgumentException( number + " does not match one of these formats: (xxx) xxx-xxxx, xxx xxx-xxxx, or xxx xxx xxxx.");
        }
    }

    /**
     * This method returns the phone number type.
     * 
     * @return Phone number type
     */
    public PhoneNumberType getType() {
        return type;
    }

    /**
     * This method sets the phone number type to the given value.
     * 
     * @param type Phone number type
     */
    public void setType(PhoneNumberType type) {
        this.type = type;
    }
    
    /**
     * This method returns the area code.
     * 
     * @return Area code
     */
    public String getAreaCode() {
        return areaCode;
    }
    
    /**
     * This method sets the area code to the given value.
     * 
     * @param areaCode Area code
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    /**
     * This method returns the exchange number.
     * 
     * @return Exchange number
     */
    public String getExchange() {
        return exchange;
    }
    
    /**
     * This method sets the exchange number to the given value.
     * 
     * @param exchange Exchange number
     */
    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    /**
     * This method gets the phone number.
     * 
     * @return Phone number
     */
    public String getNumber() {
        return number;
    }

    /**
     * This method sets the phone number to the given value.
     * 
     * @param number Phone number
     */
    public void setNumber(String number) {
        this.number = number;
    }
    
    /**
     * This method returns the complete phone number (with area code and exchange number).
     * 
     * @return Complete phone number
     */
    public String getPhoneNumber() {
    	// The complete phone number is determined and retrieved
        return "(" + areaCode + ") " + exchange + "-" + number;
    }
}
