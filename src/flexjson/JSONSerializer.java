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

import flexjson.visitors.*;

/**
 * This is the main class for JSON serialization. It delegates all the serialization calls to the visitor classes that do the main part of the work.
 * 
 * @author Charlie Hubbard, Thomas Endres
 */
public class JSONSerializer {
    /**
     * This method performs a serialization of the target instance. It wraps
     * the resulting JSON in a JSON object that contains a single field
     * named rootName. This is great to use in conjunction with other libraries
     * like EXTJS whose data models require them to be wrapped in a JSON object.
     * 
     * @param visitor Visitor object according to which the object is serialized
     * @param rootName Name of the field to assign the resulting JSON
     * @param target Object to serialize to JSON
     * @return JSON object with one field named rootName and the value being the JSON of target
     */
    public static String serialize(ObjectVisitor visitor, String rootName, Object target) {
        return serialize(visitor, rootName, target, false);
    }
	
    /**
     * This method performs a serialization of the target instance. It wraps
     * the resulting JSON in a JSON object that contains a single field
     * named rootName. This is great to use in conjunction with other libraries
     * like EXTJS whose data models require them to be wrapped in a JSON object.
     * 
     * @param visitor Visitor object according to which the object is serialized
     * @param rootName Name of the field to assign the resulting JSON
     * @param target Object to serialize to JSON
     * @param prettyPrint Flag indicating whether the JSON string should be formatted nicely
     * @return JSON object with one field named rootName and the value being the JSON of target
     */
    public static String serialize(ObjectVisitor visitor, String rootName, Object target, boolean prettyPrint) {
        return visitor.clone().visit(rootName, target, prettyPrint);
    }

    /**
     * This method performs a serialization of the target instance.
     *
     * @param visitor Visitor object according to which the object is serialized
     * @param target Object to serialize to JSON
     * @return JSON representing the target instance
     */
    public static String serialize(ObjectVisitor visitor, Object target) {
        return serialize(visitor, target, false);
    }
    
    /**
     * This method performs a serialization of the target instance.
     *
     * @param visitor Visitor object according to which the object is serialized
     * @param target Object to serialize to JSON
     * @param prettyPrint Flag indicating whether the JSON string should be formatted nicely
     * @return JSON representing the target instance
     */
    public static String serialize(ObjectVisitor visitor, Object target, boolean prettyPrint) {
        return visitor.clone().visit(target, prettyPrint);
    }
}
