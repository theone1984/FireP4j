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

import java.util.ArrayList;
import java.util.List;

import flexjson.filters.PathExpression;

/**
 * This visitor class extends the abilities of the basic visitor class by adding path include and exclude methods.
 * 
 * @author Charlie Hubbard, Thomas Endres
 */
public abstract class BasicIncludesVisitor extends ObjectVisitor {
	/**
	 * Path include and exclude expressions
	 */
    protected ArrayList<PathExpression> pathExpressions = null;
	
    /**
     * Dummy constructor for initializing the basic includes and excludes object
     */
    protected BasicIncludesVisitor() {
    	super();
    	
    	// Class variables are initialized
    	pathExpressions = new ArrayList<PathExpression>();
    }
    
    /**
     * This constructor initializes the basic includes and excludes object using the given path expressions
     * 
     * @param pathExpressions Path expressions
     */
    protected BasicIncludesVisitor(ArrayList<PathExpression> pathExpressions) {
    	super();
    	
    	// Class variables are set
    	this.pathExpressions = pathExpressions;
    }
	
    /**
     * This method takes in a dot expression representing fields to
     * include when serialize method is called. You can hand
     * it one or more fields. Examples are: "hobbies",
     * "hobbies.people", "people.emails", or "character.inventory".
     * When using dot notation each field between the dots will
     * be included in the serialization process. The order of
     * evaluation is the order in which you call the include method. 
     * The first call to include will be evaluated before other calls to
     * include or exclude. The field expressions are evaluated in order
     * you pass to this method.
     *
     * @param fields One or more field expressions to include
     */
    public void includePath(String... fields) {
    	if (fields.length > 0) {
    		// All the path expressions are included
	        for(String field : fields) {
	            pathExpressions.add(new PathExpression( field, true));
	        }
    	}
    }
    
    /**
     * This method takes in a dot expression representing fields
     * to exclude when serialize method is called. You
     * can hand it one or more fields. Example are: "password",
     * "bankaccounts.number", "people.socialsecurity", or
     * "people.medicalHistory". In exclude method dot notations
     * will only exclude the final field (i.e. rightmost field).
     * All the fields to the left of the last field will be included.
     * In order to exclude the medicalHistory field we have to
     * include the people field since people would've been excluded
     * anyway since it's a Collection of Person objects. The order of
     * evaluation is the order in which you call the exclude method. 
     * The first call to exclude will be evaluated before other calls to
     * include or exclude. The field expressions are evaluated in order
     * you pass to this method.
     *
     * @param fields One or more field expressions to exclude.
     */
    public void excludePath(String... fields) {
    	if (fields.length > 0) {
    		// All the path expressions are excluded
	        for(String field : fields) {
	            addPathExclude(field);
	        }
    	}
    }
    
    /**
     * This method returns the fields included in serialization. These fields will be in dot notation.
     *
     * @return A List of dot notation fields included in serialization.
     */
    public List<PathExpression> getPathIncludes() {
        List<PathExpression> expressions = new ArrayList<PathExpression>();
        // For all the path expressions
        for( PathExpression expression : pathExpressions ) {
        	// If the path is included, it is added to the includes list
            if( expression.isIncluded()) {
                expressions.add(expression);
            }
        }
        return expressions;
    }
    
    /**
     * Return the fields excluded from serialization. These fields will be in dot notation.
     *
     * @return A List of dot notation fields excluded from serialization.
     */
    public List<PathExpression> getPathExcludes() {
        List<PathExpression> excludes = new ArrayList<PathExpression>();
        // For all the path expressions
        for(PathExpression expression : pathExpressions) {
        	// If the path is included, it is added to the includes list
            if( !expression.isIncluded() ) {
                excludes.add(expression);
            }
        }
        return excludes;
    }

    /**
     * This method sets the fields included in serialization. These fields must be in dot notation. 
     * This is just here so that JSONSerializer can be treated like a bean so it will
     * integrate with Spring or other frameworks.
     * @param fields The list of fields to be included for serialization.  The fields arg should be a
     * list of strings in dot notation.
     */
    @SuppressWarnings("unchecked")
	public void setPathIncludes(List fields) {
		// All the path expressions are included
        for (Object field : fields) {
            pathExpressions.add(new PathExpression(field.toString(), true));
        }
    }

    /**
     * This method sets the fields excluded in serialization. These fields must be in dot notation. 
     * This is just here so that JSONSerializer can be treated like a bean so it will
     * integrate with Spring or other frameworks.
     * @param fields The list of fields to be excluded for serialization.  The fields arg should be a 
     * list of strings in dot notation.
     */
    @SuppressWarnings("unchecked")
	public void setPathExcludes(List fields) {
		// All the path expressions are excluded
        for (Object field : fields) {
            addPathExclude(field);
        }
    }
    
    /**
     * This method adds a path exclude to the path expression list.
     * 
     * @param field The dot notation field to be excluded
     */
    private void addPathExclude(Object field) {
    	// First, the containing object is added to the path includes
        String name = field.toString();
        int index = name.lastIndexOf('.');
        if (index > 0) {
        	// If there is at least one "." in the expression, a path include for the containing object is created
            PathExpression expression = new PathExpression(name.substring(0, index), true);
            if(!expression.isWildcard()) {
            	// If the include field is not a wildcard, a  is added to the expression list
                pathExpressions.add(expression);
            }
        }
        
        // The original path expression is added to the path exclude list
        pathExpressions.add(new PathExpression(name, false));
    }
}
