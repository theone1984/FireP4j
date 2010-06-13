/*
 * Copyright 2007 Charlie Hubbard
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
package flexjson.tools;

import flexjson.objects.*;
import flexjson.objects.superhero.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * This class creates fixtures for the different FlexJSON tests.
 * 
 * @author Charlie Hubbard, Thomas Endres
 */
public class FixtureCreator {
	/**
	 * This method creates a Charlie fixture for the FlexJSON tests.
	 * 
	 * @return Charlie fixture
	 */
    @SuppressWarnings("unchecked")
	public static Person createCharlie() {
    	// The fixture is created
    	
        Address charlieHome = new Address("4132 Pluto Drive", "Atlanta", "Ga", new Zipcode("33913"));
        Address charlieWork = new Address("44 Planetary St.", "Neptune", "Milkiway", new Zipcode("30328-0764"));

        Phone charliePagerPhone = new Phone(PhoneNumberType.PAGER, "404 555-1234");
        Phone charlieCellPhone = new Phone(PhoneNumberType.MOBILE, "770 777 5432");

        Calendar charlieCal = Calendar.getInstance();
        charlieCal.set(1976, Calendar.MARCH, 21, 8, 11);
        Person charlie = new Person("Charlie", "Hubbard", charlieCal.getTime(), charlieHome, charlieWork);
        charlie.getPhones().add(charliePagerPhone);
        charlie.getPhones().add(charlieCellPhone);

        charlie.getHobbies().add("Shorting volatile stocks");
        charlie.getHobbies().add("Fixing Horse Races");
        charlie.getHobbies().add("Taking dives in the 3rd round");

        return charlie;
    }
    
	/**
	 * This method creates a Ben fixture for the FlexJSON tests.
	 * 
	 * @return Ben fixture
	 */
    @SuppressWarnings("unchecked")
	public static Person createBen() {
    	// The fixture is created
        Address benhome = new Address("8735 Hilton Way", "Chattanooga", "Tn", new Zipcode("82742"));
        Address benwork = new Address("44 Planetary St.", "Neptune", "Milkiway", new Zipcode("12345"));
        
        Calendar benCal = Calendar.getInstance();
        benCal.set(1978, Calendar.JULY, 5, 8, 11);
        Person ben = new Person("Ben", "Hubbard", benCal.getTime(), benhome, benwork);
        ben.getHobbies().add("Purse snatching");
        ben.getHobbies().add("Running sweat shops");
        ben.getHobbies().add("Fixing prices");

        return ben;
    }
    
	/**
	 * This method creates a Pedro fixture for the FlexJSON tests.
	 * 
	 * @return Pedro fixture
	 */
    @SuppressWarnings("unchecked")
	public static Person createPedro() {
    	// The fixture is created
    	
        Zipcode pedroZip = new Zipcode("49404");
        Address pedroHome = new Address(" 12 Acrel�ndia Way", "Rio de Janeiro", "Brazil", pedroZip);
        Address pedroWork = new Address(" 12 Acrel�ndia Way", "Rio de Janeiro", "Brazil", pedroZip);

        Phone pedroPhone = new Phone(PhoneNumberType.MOBILE, "123 555 2323");

        Calendar pedroCal = Calendar.getInstance();
        pedroCal.set(1980, Calendar.APRIL, 12, 11, 45);
        Person pedro = new Person("Pedro", "Neves", pedroCal.getTime(), pedroHome, pedroWork);
        pedro.getPhones().add(pedroPhone);

        return pedro;
    }
    
    /**
     * This method creates a list of persons.
     * 
     * @param persons Persons to add to the list
     * @return List of persons
     */
    public static ArrayList createPeopleList(Person... persons) {
    	ArrayList people = new ArrayList();
    	
    	// All the persons are added to the list
    	for(int i = 0; i < persons.length; i++) {
    		people.add(persons[i]);
    	}
    	
    	return people;
    }
    
    /**
	 * This method creates a network fixture for the FlexJSON tests.
     * 
     * @param name Name of the network
     * @param people People in the network
     * @return
     */
    public static Network createNetwork(String name, Person... people) {
    	// The fixture is created   
        return new Network(name, people);
    }

	/**
	 * This method creates a Dilbert fixture for the FlexJSON tests.
	 * 
	 * @return Dilbert fixture
	 */
    public static Employee createDilbert() {
    	// The fixture is created
        return new Employee("Dilbert", "", new Date(), new Address("123 Finland Dr", "Cubicleville", "Hell", new Zipcode("66666")), new Address("123 Finland Dr", "Cubicleville", "Hell", new Zipcode("66666")), "Initech");        
    }

	/**
	 * This method creates a Superman fixture for the FlexJSON tests.
	 * 
	 * @return Superman fixture
	 */
    public static Hero createSuperman() {
    	// The fixture is created
        return new Hero("Super Man", new SecretIdentity("Clark Kent"), new SecretLair("Fortress of Solitude"), new XRayVision(0.8f), new HeatVision(0.7f), new Flight(1000.0f), new Invincible());
    }
    
	/**
	 * This method creates a Lex Luthor fixture for the FlexJSON tests.
	 * 
	 * @return Lex Luthor fixture
	 */
    public static Villain createLexLuthor() {
    	// The fixture is created
        return new Villain("Lex Luthor", createSuperman(), new SecretLair("Legion of Doom"));
    }
    
    /**
     * This method creates a color HashMap.
     * 
     * @return Color HashMap
     */
    public static HashMap createColorHashMap() {
    	HashMap colors = new HashMap();

    	// Various colors are added to the HashMap
        colors.put("blue", "#0000ff");
        colors.put("green", "#00ff00");
        colors.put("black", "#000000");
        colors.put("grey", "#888888");
        colors.put("yellow", "#00ffff");
        colors.put("purple", "#ff00ff");
        colors.put("white", "#ffffff");
        
        return colors;
    }
}
