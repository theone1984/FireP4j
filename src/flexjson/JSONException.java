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

/**
 * This is a general purpose exception thrown whenever {@link flexjson.JSONSerializer}
 * encounters an error. All exceptions coming from the JSONSerializer will be of
 * this type. It can be used to wrap other types of exceptions that happen during
 * that process.
 * 
 * @author Charlie Hubbard, Thomas Endres
 */
public class JSONException extends RuntimeException {
	/**
	 * Version UID
	 */
	private static final long serialVersionUID = -4630897616567606986L;

	/**
	 * This constructor sets the exception message.
	 * 
	 * @param message Exception message
	 */
	public JSONException(String message) {
		// The parent constructor is called
        super(message);
    }

	/**
	 * This constructor sets the exception message and a cause object.
	 * 
	 * @param message Exception message
	 * @param cause Cause object
	 */
    public JSONException(String message, Throwable cause) {
		// The parent constructor is called
        super(message, cause);
    }

	/**
	 * This constructor sets a cause object.
	 *
	 * @param cause Cause object
	 */
    public JSONException(Throwable cause) {
		// The parent constructor is called
        super(cause.getMessage(), cause);
    }
}
