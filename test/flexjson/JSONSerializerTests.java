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
package flexjson;

import junit.framework.TestCase;

import java.util.*;

import flexjson.JSONSerializer;
import flexjson.filters.PathExpression;
import flexjson.objects.*;
import flexjson.tools.FixtureCreator;
import flexjson.visitors.CustomVisitor;
import flexjson.visitors.DeepVisitor;
import flexjson.visitors.ShallowVisitor;

/**
 * This class tests FlexJSON serialization.
 * 
 * @author Thomas Endres
 */
public class JSONSerializerTests extends TestCase {
	/**
	 * Person Charlie
	 */
    private Person charlie = null;
	/**
	 * Person Ben
	 */
    private Person ben = null;
	/**
	 * Person Pedro
	 */
    private Person pedro = null;

    /**
     * List of people
     */
    @SuppressWarnings("unchecked")
    private List people;
    /**
     * Network
     */
    private Network network;
    /**
     * Dilbert
     */
    private Employee dilbert;

    /**
     * Color HashMap
     */
    @SuppressWarnings("unchecked")
	private Map colors;
    
    /**
     * This method sets up the tests and creates some fixtures.
     */
    @SuppressWarnings({"unchecked"})
    public void setUp() {
    	// Charlie
    	charlie = FixtureCreator.createCharlie();

        // Ben
        ben = FixtureCreator.createBen();

        // Pedro
        pedro = FixtureCreator.createPedro();
        
        // List of people
        people = FixtureCreator.createPeopleList(charlie, ben, pedro);
        
        // Network
        network = FixtureCreator.createNetwork("My Network", charlie, ben);
        
        // Dilbert
        dilbert = FixtureCreator.createDilbert();
        
        // Color HashMap
        colors = FixtureCreator.createColorHashMap();
    }
    
    /**
     * This method tests object serialization.
     */
    public void testObject() {
    	// The Charlie fixture is serialized
    	ShallowVisitor visitor = new ShallowVisitor();    	
        String charlieJson = JSONSerializer.serialize(visitor, charlie);
        
        // Various attributes are asserted in the resulting JSON string
        assertStringValue(Person.class.getName(), charlieJson);
        assertAttribute("firstName", charlieJson);
        assertStringValue("Charlie", charlieJson);
        assertAttribute("lastName", charlieJson);
        assertStringValue("Hubbard", charlieJson);
        assertAttribute("work", charlieJson);
        assertAttribute("home", charlieJson);
        assertAttribute("street", charlieJson);
        assertStringValue(Address.class.getName(), charlieJson);
        assertAttribute("zipCode", charlieJson);
        assertStringValue(Zipcode.class.getName(), charlieJson);
        assertAttributeMissing("person", charlieJson);
        assertAttributeMissing("phones", charlieJson);
        assertStringValueMissing(Phone.class.getName(), charlieJson);
        assertAttributeMissing("hobbies", charlieJson);
        
        // A path is excluded from serialization
        visitor.excludePath("home", "work");
        
    	// The Ben fixture is serialized
        String benJson = JSONSerializer.serialize(visitor, ben);
        
        // Various attributes are asserted in the resulting JSON string
        assertStringValue(Person.class.getName(), benJson);
        assertAttribute("firstName", benJson);
        assertStringValue("Ben", benJson);
        assertAttribute("lastName", benJson);
        assertStringValue("Hubbard", benJson);
        assertAttribute("birthDate", benJson);
        assertStringValueMissing(Address.class.getName(), benJson);
        assertAttributeMissing("work", benJson);
        assertAttributeMissing("home", benJson);
        assertAttributeMissing("street", benJson);
        assertAttributeMissing("city", benJson);
        assertAttributeMissing("state", benJson);
        assertStringValueMissing(Zipcode.class.getName(), benJson);
        assertAttributeMissing("zipCode", benJson);
        assertStringValueMissing(Phone.class.getName(), benJson);
        assertAttributeMissing("hobbies", benJson);
        assertAttributeMissing("person", benJson);
        
        // The visitor is reinitialized and another path is excluded
        visitor = new ShallowVisitor();
        visitor.excludePath("home.zipCode", "work.zipCode");

    	// The Charlie fixture is serialized again
        String charlieJson2 = JSONSerializer.serialize(visitor, charlie);
        
        // Various attributes are asserted in the resulting JSON string
        assertStringValue(Person.class.getName(), charlieJson2);
        assertAttribute("work", charlieJson2);
        assertAttribute("home", charlieJson2);
        assertAttribute("street", charlieJson2);
        assertStringValue(Address.class.getName(), charlieJson2);
        assertAttributeMissing("zipCode", charlieJson2);
        assertAttributeMissing("phones", charlieJson2);
        assertStringValueMissing(Zipcode.class.getName(), charlieJson2);
        assertStringValueMissing(Phone.class.getName(), charlieJson2);
        assertAttributeMissing("hobbies", charlieJson2);
        assertAttributeMissing("type", charlieJson2);
        assertStringValueMissing("PAGER", charlieJson2);
        
        // A path are included, others are excluded
        visitor.includePath("hobbies");
        visitor.excludePath("phones.areaCode", "phones.exchange", "phones.number");

    	// The Charlie fixture is serialized again
        String charlieJson3 = JSONSerializer.serialize(visitor, charlie);
        
        // Various attributes are asserted in the resulting JSON string
        assertStringValue(Person.class.getName(), charlieJson3);
        assertAttribute("work", charlieJson3);
        assertAttribute("home", charlieJson3);
        assertAttribute("street", charlieJson3);
        assertStringValue(Address.class.getName(), charlieJson3);
        assertAttribute("phones", charlieJson3);
        assertAttribute("phoneNumber", charlieJson3);
        assertStringValue(Phone.class.getName(), charlieJson3);
        assertAttribute("hobbies", charlieJson3);
        assertAttributeMissing("zipCode", charlieJson3);
        assertAttributeMissing(Zipcode.class.getName(), charlieJson3);
        assertAttributeMissing("areaCode", charlieJson3);
        assertAttributeMissing("exchange", charlieJson3);
        assertAttributeMissing("number", charlieJson3);
        assertAttribute("type", charlieJson3);
        assertStringValue("PAGER", charlieJson3);
        
        // Standard starting and closing paranetheses are asserted
        assertTrue("JSON string starts with parenthesis", charlieJson3.startsWith("{"));
        assertTrue("JSON string ends with parenthesis", charlieJson3.endsWith("}"));
    }
    
    /**
     * This method tests HashMap serialization.
     */    
    @SuppressWarnings({"unchecked"})
    public void testMap() {
        // The color HashMap is serialized
    	ShallowVisitor visitor = new ShallowVisitor();
        String colorsJson = JSONSerializer.serialize(visitor, colors);
        
        // All the entries in the HashMap are looked for within the JSON string
        for(Iterator i = colors.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            assertAttribute(entry.getKey().toString(), colorsJson);
            assertStringValue(entry.getValue().toString(), colorsJson);
        }
        
        // Standard starting and closing paranetheses are asserted
        assertTrue("JSON string starts with parenthesis", colorsJson.startsWith("{"));
        assertTrue("JSON string ends with parenthesis", colorsJson.endsWith("}"));
    }

    /**
     * This method tests array serialization.
     */
    public void testArray() {
    	// The array is created
        int[] array = new int[30];
        for(int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        
        // The array is serialized
    	ShallowVisitor visitor = new ShallowVisitor();
        String json = JSONSerializer.serialize(visitor, array);
        
        // All the numbers in the array are looked for within the JSON string
        for(int i = 0; i < array.length; i++) {
            assertNumber(i, json);
        }

        // Assertions are done for quotes within the JSON string
        assertFalse("there are no double quotes in the JSON string " + json, json.contains("\""));
        assertFalse("there are no single quotes in the JSON string " + json, json.contains("\'"));
    }

    /**
     * This method tests serialization of a collection.
     */
    @SuppressWarnings({"unchecked"})
    public void testCollection() {
        // The color HashMap is serialized
    	ShallowVisitor visitor = new ShallowVisitor();
        String colorsJson = JSONSerializer.serialize(visitor, colors.values());
        
        // All the entries in the HashMap are looked for within the JSON string
        for(Iterator i = colors.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry)i.next();
            assertAttributeMissing(entry.getKey().toString(), colorsJson);
            assertStringValue(entry.getValue().toString(), colorsJson);
        }
        
        // Starting and closing paranetheses are asserted
        assertTrue(colorsJson.startsWith("["));
        assertTrue(colorsJson.endsWith("]"));
    }

    /**
     * This method tests string serialization.
     */
    public void testString() {
    	// Various strings are created and the serialized string is asserted
        assertSerializedTo("Hello", "\"Hello\"");
        assertSerializedTo("Hello World", "\"Hello World\"");
        assertSerializedTo("Hello\nWorld", "\"Hello\\nWorld\"");
        assertSerializedTo("Hello 'Charlie'", "\"Hello 'Charlie'\"");
        assertSerializedTo("Hello \"Charlie\"", "\"Hello \\\"Charlie\\\"\"");
    }

    /**
     * This method tests serialization of a list of objects.
     */
    public void testListOfObjects() {
    	// The list of persons is serialized
    	ShallowVisitor visitor = new ShallowVisitor();
        String peopleJson = JSONSerializer.serialize(visitor, people);

        // Various attributes are asserted in the resulting JSON string
        assertStringValue(Person.class.getName(), peopleJson);
        assertAttribute("firstName", peopleJson);
        assertStringValue("Charlie", peopleJson);
        assertStringValue("Ben", peopleJson);
        assertAttribute("lastName", peopleJson);
        assertStringValue("Hubbard", peopleJson);
        assertStringValue(Address.class.getName(), peopleJson);
        assertStringValue("Pedro", peopleJson);
        assertStringValue("Neves", peopleJson);

        // The visitor object is reinitialized and a path is excluded
    	visitor = new ShallowVisitor();
    	visitor.excludePath("home", "work");
    	// The list of persons is serialized again
        peopleJson = JSONSerializer.serialize(visitor, people);

        // Various attributes are asserted in the resulting JSON string
        assertStringValue(Person.class.getName(), peopleJson);
        assertAttribute("firstName", peopleJson);
        assertStringValue("Charlie", peopleJson);
        assertStringValue("Ben", peopleJson);
        assertAttribute("lastName", peopleJson);
        assertStringValue("Hubbard", peopleJson);
        assertStringValueMissing(Address.class.getName(), peopleJson);
    }
    
    /**
     * This method tests date serialization
     */
    public void testDates() {
    	// A new shallow visitor object is created and a path is excluded
    	ShallowVisitor visitor = new ShallowVisitor();
    	visitor.excludePath("home", "work");
    	
    	// The Charlie fixture is serialized
        String peopleJson = JSONSerializer.serialize(visitor, charlie);
        
        // Various attributes are asserted in the resulting JSON string
        assertAttribute("firstName", peopleJson);
        assertStringValue("Charlie", peopleJson);
        
        // The birth date value is checked
        assertNumber(charlie.getBirthDate().getTime(), peopleJson);
        assertStringValueMissing("java.util.Date", peopleJson);
    }

    /**
     * This method tests object serialization using the object root name.
     */
    public void testRootName() {
    	// A new shallow visitor object is created and a path is excluded
    	ShallowVisitor visitor = new ShallowVisitor();
    	visitor.excludePath("home", "work");
    	
    	// The people list fixture is serialized using a root name
        String peopleJson = JSONSerializer.serialize(visitor, "people", people);
        
        // The root name is asserted
        assertTrue(peopleJson.startsWith("{\"people\":"));
    }
    
    /**
     * This method tests deep includes (serializing elements at deeper levels)
     */
    public void testDeepIncludes() {
    	// A new shallow visitor object is created and a path is included
    	ShallowVisitor visitor = new ShallowVisitor();
    	visitor.includePath("people.hobbies");
    	// The network object is serialized
        String peopleJson = JSONSerializer.serialize(visitor, network);

        // Various attributes are asserted in the resulting JSON string
        assertAttribute("name", peopleJson);
        assertStringValue("My Network", peopleJson);
        assertAttribute("firstName", peopleJson);
        assertStringValue("Charlie", peopleJson);
        assertStringValue("Ben", peopleJson);
        assertAttribute("lastName", peopleJson);
        assertStringValue("Hubbard", peopleJson);
        assertAttribute("hobbies", peopleJson);
        assertStringValue("Purse snatching", peopleJson);
    }

    /**
     * This method tests path expression includes.
     */
    public void testSetIncludes() {
    	// A new shallow visitor object is created and some paths are included
    	ShallowVisitor visitor = new ShallowVisitor();
    	visitor.setPathIncludes(Arrays.asList("people.hobbies", "phones", "home", "people.resume"));
    	// The include path expressions are retrieved
        List<PathExpression> includes = visitor.getPathIncludes();
    	
        // The includes are asserted
        assertFalse("include list not empty", includes.isEmpty());
        assertEquals("include list count check", 4, includes.size());
        assertTrue("include list contains people.hobbies", includes.contains(new PathExpression("people.hobbies", true)));
        assertTrue("include list contains people.resume", includes.contains(new PathExpression("people.resume", true)));
        assertTrue("include list contains phones", includes.contains(new PathExpression("phones", true)));
        assertTrue("include list contains home", includes.contains(new PathExpression("home", true)));
    }

    /**
     * This method tests UTF-8 support in the serialized string.
     */
    public void testI18n() {
    	// A new shallow visitor object is created and some paths are included
    	ShallowVisitor visitor = new ShallowVisitor();
    	visitor.includePath("work","home");
        
    	// The Pedro fixture is serialized
        String json = JSONSerializer.serialize(visitor, pedro);

        // Various attributes are asserted in the resulting JSON string
        assertAttribute("work", json);
        assertAttribute("home", json);
        
        // An internationalized string value is asserted in the resulting JSON string
        assertEquals(2, occurs("Acrel�ndia", json));
    }

    /**
     * This method tests deep serialization (deep array and HashMap serialization)
     */
    public void testDeepSerialization() {
    	// The network fixture is serialized using deep serialization
    	DeepVisitor visitor = new DeepVisitor();     	
        String peopleJson = JSONSerializer.serialize(visitor, network);

        // Various attributes are asserted in the resulting JSON string
        assertAttribute("name", peopleJson);
        assertStringValue("My Network", peopleJson);
        assertAttribute("firstName", peopleJson);
        assertStringValue("Charlie", peopleJson);
        assertStringValue("Ben", peopleJson);
        assertAttribute("lastName", peopleJson);
        assertStringValue("Hubbard", peopleJson);
        assertAttributeMissing("hobbies", peopleJson); // there is an annotation that explicitly excludes this!
        assertStringValueMissing("Purse snatching", peopleJson);
    }

    /**
     * This method tests deep serialization and serialization of annotated properties.
     */
    public void testDeepSerializationWithIncludeOverrides() {
    	// A new deep visitor object is created and a path that would not be serialized by annotation is included
    	DeepVisitor visitor = new DeepVisitor();
    	visitor.includePath("people.hobbies");
    	
    	// The network fixture is serialized
        String peopleJson = JSONSerializer.serialize(visitor, network);

        // Various attributes are asserted in the resulting JSON string
        assertAttribute("firstName", peopleJson);
        assertStringValue("Charlie", peopleJson);
        assertAttribute("hobbies", peopleJson);
        assertStringValue("Purse snatching", peopleJson);
        assertStringValue("Running sweat shops", peopleJson);
        assertStringValue("Fixing prices", peopleJson);
    }

    /**
     * This method tests deep serialization in combination with path excludes.
     */
    public void testDeepSerializationWithExcludes() {
    	// A new deep visitor object is created and a path is excluded
    	DeepVisitor visitor = new DeepVisitor();
    	visitor.excludePath("people.work");
    	
    	// The network fixture is serialized
        String peopleJson = JSONSerializer.serialize(visitor, network);

        // Various attributes are asserted in the resulting JSON string
        assertAttribute("firstName", peopleJson);
        assertStringValue("Charlie", peopleJson);
        assertAttributeMissing("work", peopleJson);
        assertStringValue("4132 Pluto Drive", peopleJson);
        assertAttribute("home", peopleJson);
        assertAttribute("phones", peopleJson);
    }

    /**
     * This method tests deep serialization with serialization cycles.
     */
    public void testDeepSerializationCycles() {
    	// A new deep visitor object is created and a path exclude is excluded
    	DeepVisitor visitor = new DeepVisitor();
    	visitor.excludePath("people.work");
    	
    	// The people list fixture is serialized
        String json = JSONSerializer.serialize(visitor, people);

        // Various attributes are asserted in the resulting JSON string
        assertAttribute("zipCode", json);
        assertEquals(2, occurs("49404", json));
        assertAttributeMissing("person", json);
    }
    
    /**
     * This method tests custom serialization using max depth and object filtering restrictions.
     */
    public void testCustomSerialization() {
    	// A new custom visitor object is created, the maxDepth property and object filters are set
    	CustomVisitor visitor = new CustomVisitor();
    	visitor.setMaxDepth(2);
    	visitor.addObjectFilter("flexjson.objects.Person", "firstName");

    	// The people list of the network fixture is serialized
    	String json = JSONSerializer.serialize(visitor, network.getPeople(), true);
    	
        // Various attributes are asserted in the resulting JSON string
        assertAttribute("private:birthDate", json);
        assertEquals(2, occurs("private:birthDate", json));
    	assertAttributeMissing("private:firstName", json);
        assertEquals(8, occurs("** Max Depth (2) **", json));
    }

    /**
     * This method tests serialization of the super class.
     */
    public void testSerializeSuperClass() {
    	// The Dilbert fixture is serialized
    	ShallowVisitor visitor = new ShallowVisitor();    	
        String json = JSONSerializer.serialize(visitor, dilbert);

        // Various attributes are asserted in the resulting JSON string
        assertAttribute("company", json);
        assertStringValue("Initech", json);
        assertAttribute("firstName", json);
        assertStringValue("Dilbert", json);
    }

    /**
     * This method tests serialization of public bean properties within an object.
     */
    public void testSerializePublicFields() {
    	// A Spiderman object is created 	
        Spiderman spiderman = new Spiderman();
        
        // The Spiderman object is serialized
    	ShallowVisitor visitor = new ShallowVisitor();   
        String json = JSONSerializer.serialize(visitor, spiderman);

        // Various attributes are asserted in the resulting JSON string
        assertAttribute("spideySense", json);
        assertAttribute("superpower", json);
        assertStringValue("Creates web", json);
    }

    /**
     * This method tests the pretty print functionality (output to the console).
     * This is not an automated test.
     */
    public void testPrettyPrint() {
    	// A new shallow visitor object is created and a path is included
    	ShallowVisitor visitor = new ShallowVisitor();
    	visitor.includePath("phones"); 
    	// The Charlie fixture is serialized (using pretty print)
        String charlieJson = JSONSerializer.serialize(visitor, charlie, true);
        
        // The resulting JSON string is output
        System.out.println(charlieJson);
    }

    /**
     * This method tests the usage of wildcards within path excludes.
     */
    public void testWildcardExcludes() {
    	// A new shallow visitor object is created and some paths are excluded using wildcards
    	ShallowVisitor visitor = new ShallowVisitor();
    	visitor.includePath("phones");
    	visitor.excludePath("*.class");    	
    	
    	// The Charlie fixture is serialized
        String json = JSONSerializer.serialize(visitor, charlie);

        // Various attributes are asserted in the resulting JSON string
        assertAttributeMissing("class", json);
        assertAttribute("phones", json);
        assertAttributeMissing("hobbies", json);
    }

    /**
     * This method tests the usage of wildcards within path includes.
     */
    public void testWildcardIncludes() {
    	// A new shallow visitor object is created and some paths are included using wildcards
    	ShallowVisitor visitor = new ShallowVisitor();
    	visitor.includePath("*.class");
    	
    	// The Charlie fixture is serialized
        String json = JSONSerializer.serialize(visitor, charlie, true);

        // Various attributes are asserted in the resulting JSON string
        assertAttributeMissing("phones", json);
        assertAttributeMissing("hobbies", json);
    }

    /**
     * This method tests the exclusion of every attribute using path expressions.
     */
    public void testExcludeAll() {
    	// A new shallow visitor object is created and every path is excluded
    	ShallowVisitor visitor = new ShallowVisitor();
    	visitor.excludePath("*");
    	
    	// The Charlie fixture is serialized
        String json = JSONSerializer.serialize(visitor, charlie);
        
        // Missing attributes and an empty JSON string are asserted
        assertEquals("empty JSON string", "{}", json);
        assertAttributeMissing("class", json);
        assertAttributeMissing("phones", json);
        assertAttributeMissing("firstName", json);
        assertAttributeMissing("lastName", json);
        assertAttributeMissing("hobbies", json);
    }
    
    /**
     * This method tests the excludePath() methods using shallow paths and wildcards.
     */
    public void testExcludeWildCard() {
    	// A new visitor object is created
    	ShallowVisitor visitor = new ShallowVisitor();
    	// A shallow path is excluded
    	visitor.excludePath("*Date");
    	
    	// The object is serialized
        String json = JSONSerializer.serialize(visitor, charlie);
        
        // Various attributes are asserted in the resulting JSON string
        assertAttributeMissing("birthDate", json);
        assertAttribute("class", json);
        assertAttributeMissing("phones", json);
        assertAttribute("firstName", json);
        assertAttribute("lastName", json);
        assertAttributeMissing("hobbies", json);
    }
    
    /**
     * This method tests the excludePath() methods using deep paths and wildcards.
     */
    public void testDeepExcludeWildCard() {
    	// A new visitor object is created
    	DeepVisitor visitor = new DeepVisitor();
    	// Some deep paths are excluded
    	visitor.excludePath("home.cit*");
    	visitor.excludePath("work.cit*");
    	
    	// The object is serialized
        String json = JSONSerializer.serialize(visitor, charlie);
        
        // Various attributes are asserted in the resulting JSON string
        assertAttributeMissing("city", json);
        assertAttribute("state", json);
        assertAttribute("phones", json);
        assertAttribute("firstName", json);
        assertAttribute("lastName", json);
    }

    /**
     * This method tests wildcard exclusion mixed with concrete field includes.
     */
    public void testMixedWildcards() {
    	// A new shallow visitor object is created excluding every path except some defined before
    	ShallowVisitor visitor = new ShallowVisitor();
    	visitor.includePath("firstName","lastName");
    	visitor.excludePath("*");
    	
    	// The Charlie fixture is serialized
        String json = JSONSerializer.serialize(visitor, charlie, true);

        // Various attributes are asserted in the resulting JSON string
        assertAttribute("firstName", json);
        assertStringValue("Charlie",json);
        assertAttribute("lastName", json);
        assertStringValue("Hubbard",json);
        assertAttributeMissing("class", json);
        assertAttributeMissing("phones", json);
        assertAttributeMissing("birthDate", json);
        
    	// A new shallow visitor object is created excluding every path except some other paths
    	visitor = new ShallowVisitor();
        visitor.includePath("firstName","lastName", "phones.areaCode", "phones.exchange", "phones.number");
        visitor.excludePath("*");
        
        // The Charlie fixture is serialized again
        json = JSONSerializer.serialize(visitor, charlie, true);

        // Various attributes are asserted in the resulting JSON string
        assertAttribute("firstName", json);
        assertStringValue("Charlie",json);
        assertAttribute("lastName", json);
        assertStringValue("Hubbard",json);
        assertAttributeMissing("class", json);
        assertAttribute("phones", json);
        assertAttributeMissing("birthDate", json);
    }

    /**
     * This method counts the occurence of the given string within the given JSON string.
     * 
     * @param str String whose occurence should be counted
     * @param json JSON string
     * @return Occurence count of the first string in the JSON string
     */
    private int occurs(String str, String json) {
        int current = 0;
        int count = 0;
        // While the string occurs within the JSON string
        while(current >= 0) {
        	// The string is searched within the JSON string
            current = json.indexOf(str, current);
            if(current > 0) {
            	// If there was an occurence, the occurence count is incremented
                count++;
                current += str.length();
            }
        }
        return count;
    }

    /**
     * This method asserts that the given attribute is missing within the given JSON string.
     * 
     * @param attribute Attribute name
     * @param json JSON string where the attribute should not occur
     */
    private void assertAttributeMissing(String attribute, String json) {
        assertAttribute(attribute, json, false);
    }

    /**
     * This method asserts that the given attribute is present within the given JSON string.
     * 
     * @param attribute Attribute name
     * @param json JSON string where the attribute should not occur
     */
    private void assertAttribute(String attribute, String json) {
        assertAttribute(attribute, json, true);
    }

    /**
     * This method asserts attribute occurence (whether the attribute is present or not).
     * 
     * @param attribute Attribute name
     * @param json JSON string
     * @param isPresent Flag indicating whether the attribute should be present or not
     */
    private void assertAttribute(String attribute, String json, boolean isPresent) {
        if(isPresent) {
        	// Assertion of an existing attribute
            assertTrue("'" + attribute + "' attribute is missing in JSON string " + json, json.contains("\"" + attribute + "\":"));
        } else {
        	// Assertion of a non-existing attribute
            assertFalse("'" + attribute + "' attribute is present when it's not expected in JSON string " + json, json.contains("\"" + attribute + "\":"));
        }
    }
    
    /**
     * This method asserts that the given string is missing within the given JSON string.
     * 
     * @param value String that should not occur
     * @param json JSON string where the attribute should not occur
     */
    private void assertStringValueMissing(String value, String json) {
        assertStringValue(value, json, false);
    }

    /**
     * This method asserts that the given string is present within the given JSON string.
     * 
     * @param value String that should occur
     * @param json JSON string where the attribute should occur
     */
    private void assertStringValue(String value, String json) {
        assertStringValue(value, json, true);
    }

    /**
     * This method asserts string occurence (whether the string is present or not).
     * 
     * @param value String that should or should not occur
     * @param json JSON string
     * @param isPresent Flag indicating whether the string should be present or not
     */
    private void assertStringValue(String value, String json, boolean isPresent) {
        if(isPresent) {
        	// Assertion of an existing string
            assertTrue("'" + value + "' value is missing in JSON string " + json, json.contains("\"" + value + "\""));
        } else {
        	// Assertion of a non-existing string
            assertFalse("'" + value + "' value is present when it's not expected in JSON string " + json, json.contains("\"" + value + "\""));
        }
    }

    /**
     * This method asserts that the given number is present within the given JSON string.
     * 
     * @param number Number that should occur
     * @param json JSON string where the attribute should occur
     */
    private void assertNumber(Number number, String json) {
        assertTrue(number + " is missing as a number in JSON string " + json, json.contains(number.toString()));
    }
    
    /**
     * This method serializes the first string and asserts that it equals the second string.
     * 
     * @param original First string that should be serialized
     * @param expected Second string that is expected as output of the serialization
     */
    private void assertSerializedTo(String original, String expected) {
    	// The first string is serialized
    	ShallowVisitor visitor = new ShallowVisitor();    	
        String json = JSONSerializer.serialize(visitor, original);
        // The serialized string is asserted
        assertEquals("serialization of " + original, expected, json);
    }
}
