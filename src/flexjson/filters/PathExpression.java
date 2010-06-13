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
package flexjson.filters;

import java.util.Arrays;
import java.util.regex.*;

import flexjson.tools.Path;

/**
 * This is an internal class for FlexJSON. It's used to match on fields it encounters while walking
 * the object graph. Every expression is expressed in dot notation like foo.bar.baz. Each term between the
 * dots is a field name in that parent object. All expressions are relative to some parent object
 * within the context in which they are used. Typically it is the object you're serializing. Expressions may
 * also contain wildcards like *.class.
 * 
 * @author Charlie Hubbard, Thomas Endres
 */
public class PathExpression {
	/**
	 * The path expression
	 */
    String[] expression;
    
    /**
     * Flag indicating whether the path expression uses a wild card
     */
    boolean wildcard = false;
    /**
     * Flag indicating whether the path expression should be included
     */
    boolean included = true;

    /**
     * This method creates a new path expression.
     * 
     * @param expr The path expression string.
     * @param included Flag indicating whether the path expression should be included
     */
    public PathExpression(String expr, boolean included) {
    	// Class variables are set
    	
    	// The path expression is set by splitting the expression string using dots
        expression = expr.split("\\.");
        // A wildcard exists if there is an asterisk in it
        wildcard = expr.indexOf('*') >= 0;
        this.included = included;
    }

    /**
     * This method returns a string of the current path (using commas as separators).
     * 
     * @return Dot notation string of the current path
     */
    public String toString() {
    	// The string builder is initialized
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        // For all the path entries
        for( int i = 0; i < expression.length; i++ ) {
        	// The entry is added to the dot notation string (using commas as separators)
            builder.append( expression[i] );
            if( i < expression.length - 1 ) {
                builder.append(",");
            }
        }
        // The build is ended and the resulting string is returned
        builder.append("]");
        return builder.toString();
    }

    /**
     * This method matches the given path with the expression.
     *  
     * @param path The path to match
     * @return Flag indicating whether the expression matches the path 
     */
    public boolean matches( Path path ) {
        int exprCurrentIndex = 0;
        int pathCurrentIndex = 0;
        while( pathCurrentIndex < path.length() ) {
            String current = path.getPath().get(pathCurrentIndex);
            
            // If there is an expression left
            String currentExpression = "";
            if (exprCurrentIndex < expression.length) {
	            currentExpression = expression[exprCurrentIndex];            
	
	            // Every "*" within the expression is matched to wildcard characters
	            
	            // The expression is split at "*" characters
	            String[] expressionParts = currentExpression.split("\\*");
	            boolean isLastCharacterAsterisk = currentExpression.charAt(currentExpression.length() - 1) == '*';
	            
	            if (expressionParts.length == 0) {
	            	// If there was no "*", the expression itself is used and quoted
	            	currentExpression = Pattern.quote(currentExpression);
	            } else {
	            	// For all the expression parts
	            	currentExpression = "";
	                for(int i = 0; i < expressionParts.length; i++) {
	                	// The current substring is quoted
	                	expressionParts[i] = Pattern.quote(expressionParts[i]);
	                	
	                	// The quoted substring is added to the complete expression
	                	currentExpression += expressionParts[i];
	                	
	                	// If there was a "*" before the split, wildcard characters are added to the expression
	                	if (i != expressionParts.length - 1 || isLastCharacterAsterisk) {
	                		currentExpression += "(.)*";
	                	}                	
	                }  
	            }
            }
            

            if(exprCurrentIndex < expression.length && currentExpression.equals("\\Q*\\E") ) {
                // If the path is matched by a "*", the next path entry is used
                exprCurrentIndex++;
            } else if(exprCurrentIndex < expression.length && current.matches(currentExpression)) {
                // If the path is matched by a "*", the next path and expression entries are used
                pathCurrentIndex++;
                exprCurrentIndex++;
            } else if(exprCurrentIndex - 1 >= 0 && expression[exprCurrentIndex-1].equals("*")) {
            	// If there was an asterisk before ("*.class" for example), the next path entry is used
                pathCurrentIndex++;
            } else {
            	// If no match was found, false is returned
                return false;
            }
        }
        
        if( exprCurrentIndex > 0 && expression[exprCurrentIndex-1].equals("*") ) {
        	// A path containing an asterisk is matched
            return pathCurrentIndex >= path.length() && exprCurrentIndex >= expression.length;
        } else {
        	// The complete path is matched
            return pathCurrentIndex >= path.length() && path.length() > 0;
        }
    }
    
    /**
     * This method returns whether there is a wildcard character within the path expression.
     * 
     * @return True if there is a wildcard character within the path expression, false otherwise
     */
    public boolean isWildcard() {
        return wildcard;
    }

    /**
     * This method returns whether the path expression is included.
     * 
     * @return True if the path expression is included, false otherwise
     */
    public boolean isIncluded() {
        return included;
    }

    /**
     * This method checks whether the given path expression equals the current path expression.
     * 
     * @param o Path expression to check
     */
    public boolean equals(Object o) {
    	// If the same path expression is given as an argument, they are equal
        if (this == o) {
        	return true;
        }
        // If the class name of the object is not the same, they are not equal
        if (o == null || getClass() != o.getClass()) {
        	return false;
        }

        PathExpression that = (PathExpression) o;
        // The path expression entries are checked for equality
        if (!Arrays.equals(expression, that.expression)) {
        	return false;
        }

        // If there was no difference, the paths equal each other
        return true;
    }
    
    /**
     * This hashCode method returns the hash code of the path expression.
     * (Needed for DB purposes)
     */
    public int hashCode() {
        return (expression != null ? Arrays.hashCode(expression) : 0);
    }
}
