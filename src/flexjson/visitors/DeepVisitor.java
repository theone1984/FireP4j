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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;

import flexjson.JSON;
import flexjson.filters.PathExpression;
import flexjson.visitors.ObjectVisitor;

/**
 * This class extends the basic includes visitor object and is used for deep serialization.
 * 
 * @author Charlie Hubbard, Thomas Endres
 */
public class DeepVisitor extends BasicIncludesVisitor {
	/**
	 * Dummy constructor
	 */
    public DeepVisitor() {
    	// The parent constructor is called
        super();
    }

    /**
     * This constructor creates a new shallow visitor object using the path expressions given.
     * 
     * @param pathExpressions Path expression list
     */
    protected DeepVisitor(ArrayList<PathExpression> pathExpressions) {
    	// The parent constructor is called
    	super(pathExpressions);
    }
    
    /**
     * This method clones the visitor object.
     */
	@Override
	public ObjectVisitor clone() {
		// A new shallow visitor object using the same path expression list is created
		return new DeepVisitor(pathExpressions);
	}

	/**
	 * This method checks whether the given property should be included in the JSON string.
	 * 
	 * @param prop Property to be checked.
	 * @return True if the property should be included, false otherwise
	 */
	@Override
    protected boolean isIncluded( PropertyDescriptor prop ) {
    	// Path expressions are checked (if the field is in a path expression its include status is returned)
        PathExpression expression = matches( prop, pathExpressions);
        if( expression != null ) {
            return expression.isIncluded();
        }

        // The field annotations are checked (if the field is annotated with the JSON annotation, its include status is returned)
        Method accessor = prop.getReadMethod();
        if( accessor.isAnnotationPresent( JSON.class ) ) {
            return accessor.getAnnotation(JSON.class).include();
        }

        // Every property that has come this far is included
        return true;
    }


}
