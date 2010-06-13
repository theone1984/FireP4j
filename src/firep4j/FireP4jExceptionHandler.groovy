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
package firep4j

import java.lang.Thread.UncaughtExceptionHandler

/**
 * This class is used for exception handling in FireP4j.
 */
public class FireP4jExceptionHandler implements UncaughtExceptionHandler {
	/**
	 * Flag indicating whether exceptions should be logged to FireP4j
	 */
	boolean captureExceptions = false
	/**
	 * Flag indicating whether errors should be logged to FireP4j
	 */
	boolean captureErrors = false
	/**
	 * Flag indicating whether assertion errors should be logged to FireP4j
	 */
	boolean captureAssertions = false
	
	/**
	 * The FireP4j instance to log to
	 */
	private FireP4j fireP4j = null
	
	/**
	 * Dummy constructor setting the FireP4j instance
	 * 
	 * @param fireP4j FireP4j instance
	 */
	public FireP4jExceptionHandler(FireP4j fireP4j) {
		// Class variables are set
		this.fireP4j = fireP4j
	}
	
	/**
	 * This method catches uncaught exceptions. It is used for logging to FireP4j.
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		// According to the exception type, the exceptions are logged
		// If the throwable should not be logged, it is thrown again
		
		if (throwable instanceof AssertionError) {
			// Exceptions
			if (!captureAssertions) {
				throw throwable
			}
			fireP4j.error(throwable)
		} else if (throwable instanceof Error) {
			// Errors
			if (!captureErrors) {
				throw throwable
			}
			fireP4j.error(throwable)
		} else if (throwable instanceof Exception) {
			// Assertion errors
			if (!captureExceptions) {
				throw throwable
			}
			fireP4j.error(throwable)
		} else {
			// Something else
			throw throwable
		}
	}
	
	/**
	 * This method returns whether one of the handlers is defined.
	 * 
	 * @return Flag indicating whether one of the handlers is defined
	 */
	public boolean isHandlerDefined() {
		captureExceptions || captureErrors || captureAssertions
	}
}
