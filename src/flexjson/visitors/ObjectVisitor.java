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
package flexjson.visitors;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import flexjson.JSONException;
import flexjson.filters.PathExpression;
import flexjson.tools.ChainedSet;
import flexjson.tools.Path;

/**
 * This method is the base class for all visitors. It visits all the nodes in an oject and serializes it.
 * 
 * @author Charlie Hubbard, Thomas Endres
 */
public abstract class ObjectVisitor {
	/**
	 * Identifier
	 */
    public final static char[] HEX = "0123456789ABCDEF".toCharArray();
	
    /**
     * Stringbuilder for building the JSON string
     */
    protected StringBuilder builder;
    
    /**
     * Flag indicating whether the JSON string should be formatted nicely
     */
    protected boolean prettyPrint = false;
    
    /**
     * Current amount of whitespaces for indenting
     */
    protected int amount = 0;
    
    /**
     * Flag indicating whether an array is currently opened
     */
    protected boolean insideArray = false;
    
    /**
     * Current path within an object
     */
    protected Path path;
    
    /**
     * Set containing all visits to objects
     */
    protected ChainedSet visits = new ChainedSet(Collections.EMPTY_SET);
    
    /**
     * Dummy constructor (initializing the objects)
     */
    protected ObjectVisitor() {
    	// Class variables are initialized
        builder = new StringBuilder();
        path = new Path();
    }
    
    /**
     * This method clones the visitor object.
     */
    public abstract ObjectVisitor clone();

    /**
     * This method visits an object and returns the resulting JSON script. It is an entry point for object serialization.
     * 
     * @param target Object to serialize
     * @param prettyPrint Flag indicating whether the JSON string should be formatted nicely
     * @return Formatted JSON string
     */
    public String visit(Object target, boolean prettyPrint) {
    	// Class variables are set
        this.prettyPrint = prettyPrint;
    	
        // The JSON string is created and returned
        json(target);
        return builder.toString();
    }

    /**
     * This method visits an object and returns the resulting JSON script. It is an entry point for object serialization.
     * It adds a root name to the value
     * 
     * @param rootName Root name
     * @param target Object to serialize
     * @param prettyPrint Flag indicating whether the JSON string should be formatted nicely
     * @return Formatted JSON string
     */
    public String visit(String rootName, Object target, boolean prettyPrint) {
    	// Class variables are set
        this.prettyPrint = prettyPrint;
    	
        // The root object serialization is started
    	beginObject();
    	// The root name is added to the JSON string
        string(rootName);
        add(':');
        // The object is serialized
        json(target);
        // The root object serialization is started
        endObject();
        
        // The JSON string is returned
        return builder.toString();
    }
    
    /**
     * This method serializes the given object to its JSON string and saves this string.
     * 
     * @param object Object to serialize
     */
    @SuppressWarnings("unchecked")
	protected void json(Object object) {
    	// The JSON string for the object is determined
        if (object == null) {
        	// Null -> "null"
        	add("null");
        } else if (object instanceof Class) {
        	// Class -> Classname (String)
            string(((Class)object).getName());
        } else if (object instanceof Boolean) {
        	// Boolean -> "true"/"false"
            bool(((Boolean) object));
        } else if (object instanceof Number) {
        	// Number -> String
            add(object);
        } else if (object instanceof String) {
        	// String -> String
            string(object);
        } else if (object instanceof Character) {
        	// Character -> String
        	string(object);
        } else if (object instanceof Map) {
        	// Map -> Further JSON mapping
            map((Map)object);
        } else if (object.getClass().isArray()) {
        	// Array -> Further JSON mapping
            array(object);
        } else if (object instanceof Iterable) {
        	// Iterator (array) -> Further JSON mapping
            array(((Iterable) object).iterator());
        } else if(object instanceof Date) {
        	// Date -> DateString
            date((Date)object);
        } else if(object instanceof Enum) {
        	// Enumerable -> Enum string
            enumerate((Enum)object);
        } else {
        	// Bean -> Further JSON mapping
            bean(object);
        }
    }    
    
    /**
     * This method transforms an enum to its JSON string value.
     * 
     * @param value Enum object
     */
    @SuppressWarnings("unchecked")
	protected void enumerate(Enum value) {
    	// The value is determined from the name of the enum object
        string(value.name());
    }

    /**
     * This method transforms a map to its JSON string value.
     * 
     * @param map Map object
     */
    @SuppressWarnings("unchecked")
	protected void map(Map map) {
    	// The object serialization is started
        beginObject();
        // An iterator is created from the map
        Iterator it = map.keySet().iterator();
        boolean firstField = true;
        // For each element in the map
        while (it.hasNext()) {
        	// The element is added to the JSON string
            Object key = it.next();
            int len = builder.length();
            add(key, map.get(key), firstField);
            if(len < builder.length()) {
                firstField = false;
            }
        }
        // The object serialization is ended
        endObject();
    }

    /**
     * This method transforms an array (in iterator form) to its JSON string value.
     * 
     * @param it Iterator object
     */
    @SuppressWarnings("unchecked")
	protected void array(Iterator it) {
    	// The array serialization is started
        beginArray();
        // For each element in the array
        while (it.hasNext()) {
        	// The object is printed nicely (if this is preferred)
            if (prettyPrint) {
                addNewline();
            }
        	// The element JSON string is added to the JSON string (possibly recursive)
            addArrayElement(it.next(), it.hasNext());
        }
        // The array serialization is ended
        endArray();
    }

    /**
     * This method transforms an array (in iterator form) to its JSON string value.
     * 
     * @param object Array object
     */
    protected void array(Object object) {
    	// The array serialization is started
        beginArray();
        int length = Array.getLength(object);
        // For each element in the array
        for (int i = 0; i < length; ++i) {
        	// The object is printed nicely (if this is preferred)
            if (prettyPrint) {
                addNewline();
            }
        	// The element JSON string is added to the JSON string (possibly recursive)
            addArrayElement(Array.get(object, i), i < length - 1);
        }
        // The array serialization is ended
        endArray();
    }
    
    /**
     * This method adds an array element to the JSON string.
     * 
     * @param object Array element
     * @param isLast Flag indicating whether the array element is the last element within the array
     */
    protected void addArrayElement(Object object, boolean isLast) {
        int len = builder.length();
        // The JSON string for the array element is determined
        json(object);
        // The element JSON string is added to the complete JSON string (using commas as separators - possibly recursive)
        if(len < builder.length()) { // make sure we at least added an element.
            if (isLast) add(',');
        }
    }
    
    /**
     * This method adds a boolean object to the JSON string.
     * 
     * @param b Boolean object
     */
    protected void bool(Boolean b) {
    	// The object is added to the JSON string
        add(b ? "true" : "false");
    }

    /**
     * This method adds a string object to the JSON string.
     * 
     * @param obj String object
     */
    protected void string(Object obj) {
    	// The object is transformed to a string value
        String value = obj.toString();
        // The string is enclosed in parantheses
        add('\"');
        int last = 0;
        int len = value.length();
        // For all the characters in the string
        for(int i = 0; i < len; i++) {
        	
        	// Special characters are encoded
        	// (", \, whitespace and line change characters)
        	
            char c = value.charAt(i);
            if (c == '"') {
                last = add(value, last, i, "\\\"");
            } else if (c == '\\') {
                last = add(value, last, i, "\\\\");
            } else if (c == '\b') {
                last = add(value, last, i, "\\b");
            } else if (c == '\f') {
                last = add(value, last, i, "\\f");
            } else if (c == '\n') {
                last = add(value, last, i, "\\n");
            } else if (c == '\r') {
                last = add(value, last, i, "\\r");
            } else if (c == '\t') {
                last = add(value, last, i, "\\t");
            } else if (Character.isISOControl(c)) {
            	// Any other character is added to the JSON string (as a unicode encoded character)
                last = add(value, last, i) + 1;
                unicode(c);
            }
        }
        // The last character is added to the string
        if(last < value.length()) {
            add(value, last, value.length());
        }
        
        // The string is enclosed in parantheses
        add('\"');
    }
    
    /**
     * This method adds a part of a string object to the current JSON string.
     * 
     * @param value JSON string value
     * @param begin Index of the character to start with
     * @param end Index of the character to end with
     * @return Index of the character to end with
     */
    protected int add(String value, int begin, int end) {
    	// The string is added to the string builder
        builder.append(value, begin, end);
        // The new end index value is returned
        return end;
    }

    /**
     * This method adds a part of a string object to the current JSON string.
     * 
     * @param value JSON string value
     * @param begin Index of the character to start with
     * @param end Index of the character to end with
     * @param append Additionally appended string value
     * @return Index of the character to end with
     */
    protected int add(String value, int begin, int end, String append) {
    	// The string and the appended string is added to the string builder
        builder.append(value, begin, end);
        builder.append(append);
        // The new end index value is returned
        return end + 1;
    }

    /**
     * This method adds a data object to the JSON string.
     * 
     * @param date Date object
     */
    protected void date(Date date) {
    	// The date object is added to the JSON string
        builder.append(date.getTime());
    }

    /**
     * This method adds a bean object to the JSON string.
     * 
     * @param object Bean object
     */
    @SuppressWarnings({"unchecked"})
    protected void bean(Object object) {
    	// The object is only serialized if it hasn't been touched before
        if(!visits.contains(object)) {
        	// The visits object is recreated and the current object is added to it
            visits = new ChainedSet(visits);
            visits.add(object);
            // The object serialization is started
            beginObject();
            try {
            	// Object properties are determined first
            	
            	// The object properties are determined
                BeanInfo info = Introspector.getBeanInfo(findBeanClass(object));
                PropertyDescriptor[] props = info.getPropertyDescriptors();
                boolean firstField = true;
                
                // For each property
                for (PropertyDescriptor prop : props) {
                	// The name of the property is determined
                    String name = prop.getName();
                    // The name of the property is added to the current path
                    path.enqueue(name);
                    Method accessor = prop.getReadMethod();
                    if (accessor != null && isIncluded(prop)) {
                        // If the accessor should be included, its value is retrieved
                        Object value = accessor.invoke(object, (Object[]) null);
                       
                        if(!visits.contains(value)) {
                        	 // If the value hasn't been touched before, its property value is added to the JSON string (possibly recursive)
                            add(name, value, firstField);
                            firstField = false;
                        }
                    }
                    // The name of the property is removed from the current path
                    path.pop();
                }
                
                // Then, remaining elements are retrieved using reflection
                
                // For all the classes and superclasses of the object
                for(Class current = object.getClass(); current != null; current = current.getSuperclass()) {
                	// All fields are retrieved
                    Field[] ff = current.getDeclaredFields();
                    // For all the fields
                    for (Field field : ff) {
                    	// The name of the field is added to the current path
                        path.enqueue(field.getName());
                        if (isValidField(field)) {
                            if(!visits.contains(field.get(object))) {
                           	 	// If the value hasn't been touched before, its property value is added to the JSON string (possibly recursive)
                                add(field.getName(), field.get(object), firstField);
                                firstField = false;
                            }
                        }
                        // The name of the property is removed from the current path
                        path.pop();
                    }
                }
            }
            // JSON exceptions are thrown, every other exception is transformed to a JSON exception
            catch(JSONException e) {
                throw e;
            } catch(Exception e) {
                throw new JSONException("Error trying to serialize path: " + path.toString(), e);
            }
            // The object serialization is started
            endObject();
            // The visits are reset to the parent element
            visits = (ChainedSet) visits.getParent();
        }
    }
    
    @SuppressWarnings("unchecked")
	protected Class<?> findBeanClass(Object object) {
        try {
            Class[] classes = object.getClass().getInterfaces();
            for(Class clazz : classes) {
                if(clazz.getName().equals("org.hibernate.proxy.HibernateProxy")) {
                    Method method = object.getClass().getMethod("getHibernateLazyInitializer");
                    Object initializer = method.invoke(object);
                    Method pmethod = initializer.getClass().getMethod("getPersistentClass");
                    return  (Class<?>)pmethod.invoke(initializer);
                }
            }
        } catch (IllegalAccessException e) {
        } catch (NoSuchMethodException e) {
        } catch (InvocationTargetException e) {
        }
        return object.getClass();
    }

    /**
     * This method returns whether a property should be included within the JSON string.
     * 
     * @param prop Property object
     * @return True if the property should be included, false otherwise
     */
    protected abstract boolean isIncluded(PropertyDescriptor prop);
    
    /**
     * This method determines whether a reflection field should be included in an object JSON string.
     * Only non-static, non-transient and public fields are included within the JSON string.
     * 
     * @param field Field object
     * @return True if the value should be included, false otherwise
     */
    protected boolean isValidField(Field field) {
        return !Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers());
    }

    /**
     * This method add a comma to the JSON string.
     * 
     * @param firstField Flag indicating whether the currently active field is the first field before the operation
     * @return Flag indicating whether the currently active field is the first field after the operation
     */
    protected boolean addComma(boolean firstField) {
    	// The comma is inserted if the field is not the first field (otherwise, the firstField value is flipped)
        if (!firstField) {
            add(',');
        } else {
            firstField = false;
        }
        return firstField;
    }

    /**
     * This method starts the serialization of an object.
     * It is used for pretty print purposes.
     */
    protected void beginObject() {
    	// Pretty print
        if(prettyPrint) {
            if(insideArray) {
                indent(amount);
            }
            amount += 4;
        }
        // The object start character is inserted
        add('{');
    }

    /**
     * This method ends the serialization of an object.
     * It is used for pretty print purposes.
     */
    protected void endObject() {
    	// Pretty print
        if(prettyPrint) {
            addNewline();
            amount -= 4;
            indent(amount);
        }
        // The object end character is inserted
        add('}');
    }

    /**
     * This method starts the serialization of an array.
     * It is used for pretty print purposes.
     */
    protected void beginArray() {
    	// Pretty print
        if(prettyPrint) {
            amount += 4;
            insideArray = true;
        }
        // The array start character is inserted
        add('[');
    }

    /**
     * This method ends the serialization of an array.
     * It is used for pretty print purposes.
     */
    protected void endArray() {
    	// Pretty print
        if(prettyPrint) {
            addNewline();
            amount -= 4;
            insideArray = false;
            indent(amount);
        }
        // The array end character is inserted
        add(']');
    }
    
    /**
     * This method adds a character to the JSON string.
     * 
     * @param c Character to add
     */
    protected void add(char c) {
        builder.append(c);
    }

    /**
     * This method adds an indent to the JSON string.
     * 
     * @param c Character to add
     */
    protected void indent(int amount) {
    	// The indent is set
        for(int i = 0; i < amount; i++) {
            builder.append(" ");
        }
    }

    /**
     * This method adds a new line to the JSON string.
     */
    protected void addNewline() {
        builder.append("\n");
    }

    /**
     * This method adds a JSON object value to the JSON string.
     * 
     * @param value Object value to add
     */
    protected void add(Object value) {
        builder.append(value);
    }

    /**
     * This method adds a JSON object value to the JSON string.
     * 
     * @param key Key that is serialized to the JSON string
     * @param value Value that is serialized to the JSON string
     * @param prependComma Flag indicating whether a comma should be inserted before the object
     */
    protected void add(Object key, Object value, boolean prependComma) {
        int start = builder.length();
        
        // Comma and key are inserted into the JSON string
        addComma(prependComma);
        addAttribute(key);

        int len = builder.length();
        
        // The value is inserted into the JSON string
        json(value);
        
        // Erase the attribute key if we didn't output anything.
        if(len == builder.length()) {
            builder.delete(start, len);
        }
    }

    /**
     * This method adds an attribute key value to the JSON string
     * 
     * @param key Key value
     */
    protected void addAttribute(Object key) {
    	// Pretty print
        if(prettyPrint) {
            addNewline();
            indent(amount);
        }
        
        // The builder is appended by the key value '"${key}": '
        builder.append("\"");
        builder.append(key);
        builder.append("\"");
        builder.append(":");
        
    	// Pretty print
        if(prettyPrint) {
            builder.append(" ");
        }
    }
    
    /**
     * This method adds a unicode character to the JSON string.
     * 
     * @param c Character to be encoded
     */
    protected void unicode(char c) {
    	// The character is unicode encoded and added to the JSON string
        add("\\u");
        int n = c;
        for (int i = 0; i < 4; ++i) {
            int digit = (n & 0xf000) >> 12;
            add(ObjectVisitor.HEX[digit]);
            n <<= 4;
        }
    }

    /**
     * This method checks whether a path expression matches the current path
     * 
     * @param prop Current property (unneccesary)
     * @param expressions Path expressions that should be matched with the current path
     * @return Path expression that matches the current path or null
     */
    protected PathExpression matches(PropertyDescriptor prop, List<PathExpression> expressions) {
    	// All the path expressions are checked
        for(PathExpression expr : expressions) {
            if(expr.matches(path)) {
            	// If an expression matches the current path, it is returned
                return expr;
            }
        }
    	// If no expression matches the current path, null is returned
        return null;
    }
}
