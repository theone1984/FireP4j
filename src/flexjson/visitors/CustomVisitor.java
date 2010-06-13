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
package flexjson.visitors;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import flexjson.JSONException;
import flexjson.filters.ObjectFilters;
import flexjson.filters.PathExpression;
import flexjson.tools.ChainedSet;
import flexjson.visitors.ObjectVisitor;

/**
 * This class extends the basic includes visitor object and is used for deep serialization.
 * It also employs FireP4j functionality like max depth restriction and object filters.
 * 
 * @author Thomas Endres
 */
public class CustomVisitor extends BasicIncludesVisitor {
	/**
	 * Current serialization depth
	 */
	private int currentDepth = 0;
	/**
	 * Max serialization depth
	 */
	private int maxDepth = -1;
	
	/**
	 * String that is output when max serialization depth is reached
	 */
	private String maxDepthString = "";
	
	/**
	 * Object filters (object fields that will be excluded from the JSON string)
	 */
	private ObjectFilters objectFilters = null;
	
	/**
	 * Class name list along the path to the current object
	 */
	private ArrayList<String> classNames = null;
	
	/**
	 * Dummy constructor
	 */
    public CustomVisitor() {
    	// The parent constructor is called
    	super();
    	
    	// Class variables are set
    	objectFilters = new ObjectFilters();  
    	build();
    }
    /**
     * This constructor creates a new shallow visitor object using the given values.
     * 
     * @param pathExpressions Path expression list
     * @param objectFilters Object filters
     * @param maxDepth Max serialization depth
     */
    protected CustomVisitor(ArrayList<PathExpression> pathExpressions, ObjectFilters objectFilters, int maxDepth) {
    	// The parent constructor is called
    	super(pathExpressions);
    	
    	// Class variables are set
    	this.objectFilters = objectFilters;
    	this.maxDepth = maxDepth;
    	build();
    }
    
    /**
     * This method creates necessary class variables.
     */
    private void build() {
    	// Class variables are set
    	maxDepthString = "\"** Max Depth (" + maxDepth + ") **\"";    	
    	classNames = new ArrayList<String>();
    }
    
    /**
     * This method clones the visitor object.
     */
	@Override
	public ObjectVisitor clone() {
		// A new shallow visitor object using the same path expression list is created
		return new CustomVisitor(pathExpressions, objectFilters, maxDepth);
	}
	
	/**
	 * This method adds an object filter to the object filter list.
	 * 
	 * @param className Class name containing the filtered fields
	 * @param fieldNames Field names that should be filtered
	 */
	public void addObjectFilter(String className, String... fieldNames) {
		// The object filters are set
		objectFilters.addObjectFilter(className, fieldNames);
	}
	
	/**
	 * This method removes an object filter from the object filter list.
	 * 
	 * @param className Class name containing the fields
	 * @param fieldNames Field names that should not be filtered anymore
	 */
	public void removeObjectFilter(String className, String... fieldNames) {
		// The object filters are removed
		objectFilters.removeObjectFilter(className, fieldNames);
	}
	
	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}
    
	/**
	 * This method checks whether the given property should be included in the JSON string.
	 * 
	 * @param prop Property to be checked.
	 * @return True if the property should be included, false otherwise
	 */
	protected boolean isIncluded(PropertyDescriptor prop) {
    	// Path expressions are checked (if the field is in a path expression its include status is returned)
        PathExpression expression = matches(null, pathExpressions);
        if( expression != null ) {
            return expression.isIncluded();
        }
        
        // The object filters are checked
        if (classNames.size() != 0 && path.getPath().size() != 0) {
        	// Class and field name are determined        	
	        String className = classNames.get(classNames.size() - 1);
	        String fieldName = path.getPath().get(path.getPath().size() - 1);
	        
	        // If the object filter is set for the given class and field names, the field is excluded
	        if (objectFilters.isFieldExcluded(className, fieldName)) {
	        	return false;
	        }
        }
        
        // Every property that has come this far is included
        return true;
    }
	
	/**
	 * This method checks whether the field at the current path should be included in the JSON string.
	 * 
	 * @return True if the property should be included, false otherwise
	 */
	protected boolean isIncluded() {
		return isIncluded(null);
    }
	
	/**
	 * This method checks whether the current depth exceeds max serialization depth.
	 * 
	 * @return True if max depth is not exceeded, false otherwise
	 */
	protected boolean isCurrentDepthIncluded() {
		// If max depth was set, it is checked
		if (maxDepth != -1) {
			return currentDepth <= maxDepth;
		}		
		// If max depth was not set, the depth restriction was not exceeded
		return true;
	}
	
    /**
     * This method transforms a map to its JSON string value.
     * 
     * @param map Map object
     */
    @SuppressWarnings("unchecked")
	@Override
	protected void map(Map map) {
    	// Current depth is incremented
    	currentDepth++;
    	if (isCurrentDepthIncluded()) {
        	// If current depth is included, the map is added to the JSON string
    		super.map(map);
    	} else {
    		// If current depth is not included, the max depth string is added to the JSON string
    		addMaxDepthRestriction();
    	}
    	// Current depth is decremented
		currentDepth--;
    }

    /**
     * This method transforms an array (in iterator form) to its JSON string value.
     * 
     * @param it Iterator object
     */
    @SuppressWarnings("unchecked")
	@Override
    protected void array(Iterator it) {
    	// Current depth is incremented
    	currentDepth++;
    	if (isCurrentDepthIncluded()) {
    		// If current depth is included, the array is added to the JSON string
    		super.array(it);
    	} else {
    		// If current depth is not included, the max depth string is added to the JSON string
    		addMaxDepthRestriction();
    	}
    	// Current depth is decremented
		currentDepth--;
    }
    
    /**
     * This method transforms an array (in iterator form) to its JSON string value.
     * 
     * @param object Array object
     */
    @Override
    protected void array(Object object) {
    	// Current depth is incremented
    	currentDepth++;
    	if (isCurrentDepthIncluded()) {
    		// If current depth is included, the array is added to the JSON string
    		super.array(object);
    	} else {
    		// If current depth is not included, the max depth string is added to the JSON string
    		addMaxDepthRestriction();
    	}
    	// Current depth is decremented
		currentDepth--;
    }
    
    /**
     * This method adds a bean object to the JSON string.
     * 
     * @param object Bean object
     */
    @Override
    protected void bean(Object object) {
    	// The class name is added to the class name list
    	classNames.add(object.getClass().getName());
    	// Current depth is incremented
    	currentDepth++;
    	
    	// A field name list controlling duplicate values is initialized
    	ArrayList<String> nameList = new ArrayList<String>();
    	
    	if (!isCurrentDepthIncluded()) {
    		// If current depth is not included, the max depth string is added to the JSON string
     		addMaxDepthRestriction();
     	} else if(!visits.contains(object)) {
     		// If current depth is included
     		
     		// The object is added to the visits list
            visits = new ChainedSet(visits);
            visits.add(object);
            // Object serialization is started
            beginObject();
            try {
            	int modifiers = 0;
            	String modifierString = "";            	
            	String name = "";
            	Object value = null;            	
            	boolean firstField = true;
	    		
            	// For all the classes and superclasses of the object
                for(Class current = object.getClass(); current != null; current = current.getSuperclass()) {
                	// All fields are retrieved
                    Field[] ff = current.getDeclaredFields();
                    // For all the fields
                    for (Field field : ff) {
                        field.setAccessible(true);
                        // The name of the field is retrieved
                        name = field.getName(); 
                        
                    	// The name of the field is added to the current path and the field name list
                        nameList.add(name);
                        path.enqueue(name);
                        
                        boolean isIncluded = isIncluded();                        
	                    if (!isIncluded) {
	                    	// If the field is not included, its name is removed from the current path and execution is set to the next field
		                    path.pop();	                    	
	                    	continue;
	                    }                  
                        
                       	try {
                       		// Modifiers are retrieved
                        	modifiers = field.getModifiers();
                        	modifierString = "";
                        	
                        	// Private, protected, public and static modifiers are combined to a modifier string
	                		if (Modifier.isPrivate(modifiers)) 	{ modifierString = "private:"; }
	                		else if (Modifier.isProtected(modifiers)) { modifierString = "protected:"; }
	                		else if (Modifier.isPublic(modifiers)) { modifierString = "public:"; }
	                		if (Modifier.isStatic(modifiers)) { modifierString += "static:"; }
                       	} catch (Exception e) {	}
	                    
                       	// Resulting name and value of the field (also containing the modifiers) are retrieved
	                    name = modifierString + name;
	                    value = field.get(object);

	                    // If the field was not visited before, it is added to the JSON string
	                    if(!visits.contains(field.get(object))) {
	                       	add(name, value, firstField);
	                       	firstField = false;
	                    }
	                        
	                    // The field name is removed from the path again
	                    path.pop();
                    }
                }
                
            	// The object properties are determined
                BeanInfo info = Introspector.getBeanInfo(findBeanClass(object));
                PropertyDescriptor[] props = info.getPropertyDescriptors();
                
                // For each property
                for (PropertyDescriptor prop : props) {
                	// The name of the property is determined
                    name = prop.getName();
                    // The name of the property is added to the current path
                    path.enqueue(name);
                    
                    Method accessor = prop.getReadMethod();
                    
                    if (nameList.contains(name) || accessor == null || !isIncluded(prop)) {
                    	// If the field is not included, was already included before or is not accessible,
                    	// its name is removed from the current path and execution is set to the next field
                        path.pop();
                        continue;
                    }
                    
                    // The value of the property is determined
                    value = accessor.invoke(object, (Object[]) null);
                    
                 // If the field was not visited before
                    if(!visits.contains(value)) {
                      	if (name.equals("class")) {
                      		// Property "class" is transformed to "__className"
                       		name = "__className";
                       	} else {
                       		// All the other properties are public
                       		name = "public:" + name;
                       	}
                       	
                      	// The property is added to the JSON string
                    	add(name, value, firstField);
                    	firstField = false;
                    }
                    // The name of the property is removed from the current path
                    path.pop();
                }
            }
            // JSON exceptions are thrown, every other exception is transformed to a JSON exception
            catch(JSONException e) {
            	throw e;
            } catch(Exception e) {
            	throw new JSONException("Error trying to serialize path: " + path.toString(), e);
            }
            
            // Object serialization is ended
            endObject();
            // The visits list is restored
            visits = (ChainedSet) visits.getParent();
     	}
    	
    	// Current depth is decremented
		currentDepth--;
		// The class name is removed from the class name list
		classNames.remove(classNames.size() - 1);
    }
    
    /**
     * This method adds the max depth string to the JSON string.
     */
	protected void addMaxDepthRestriction() {
		// The maxDepth property is added to the path
		path.enqueue("maxDepth");
		// The max depth string is added to the JSON string
		add(maxDepthString);
		// The maxDepth property is popped from the path
		path.pop();
	}
	
}
