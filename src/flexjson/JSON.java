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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * This annotation is used by {@link flexjson.JSONSerializer} to include
 * or exclude fields from the JSON serialization process. You may
 * annotate fields or methods with this, but be consistent if you start
 * doing one vs. the other. Typically this will be used to exclude
 * fields that should never be included in the serialization.  For
 * example the password of a User.
 * 
 * @author Charlie Hubbard, Thomas Endres
 */
@Retention( value = RetentionPolicy.RUNTIME )
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface JSON {
	/**
	 * This annotation method is used to check whether the field should be included or excluded
	 * 
	 * @return True if the field should be included, false otherwise
	 */
    boolean include() default true;
}
