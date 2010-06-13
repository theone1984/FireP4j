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

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.Serializable

import java.util.Iterator
import java.util.HashMap
import java.util.LinkedList
import java.util.concurrent.locks.*

import firep4j.profile.*
import firep4j.tools.*

/**
 * This class represents the main class for logging to FireBug.
 * 
 * @author Thomas Endres
 */
public class FireP4j {
	/**
	 * Different log levels
	 */
	private enum FireP4jLogLevel {
		/**
		 * Simply logging to the console
		 */
		LOG,
		/**
		 * Pointing out interesting logs
		 */
		INFO,
		/**
		 * Output of warnings
		 */
		WARN,
		/**
		 * Output of error variables
		 */
		ERROR,
		/**
		 * Output of table structures
		 */
		TABLE,
		/**
		 * Output of the current stack trace
		 */
		TRACE,
		/**
		 * Exception output
		 */
		EXCEPTION,
		/**
		 * Output to the FireP4j net tab
		 */
		DUMP,
		/**
		 * Indicator for groups of logs (initially collapsed)
		 */
		COLLAPSED_GROUP_START,
		/**
		 * Indicator for groups of logs
		 */
		GROUP_START,
		/**
		 * Indicator for the end of a group operation	
		 */
		GROUP_END
	}
	
	/**
	 * Minimum FirePHP version that is supported by the library
	 */
	private static final String minFirePHPVersion = "0.0.6"
	
	/**
	 * FireBug console version that is supported
	 */
	public static final String version = "0.3"
	
	/**
	 * Max chunk size (max size of a FireP4j message)
	 */
	private static final int chunkSize = 5000
	
	/**
	 * Current FireP4j profile
	 */
	private static FireP4jProfile profile = new JavaProfile()
	
	/**
	 * Counter variable counting the number of instances created
	 */
	private static int instanceCount = 0
	
	/**
	 * The JSON utility class instance
	 */
	private JSONUtils jsonUtils = null
	
	/**
	 * Flag indicating whether the output is enabled 
	 */
	boolean enabled = true
	
	/**
	 * Max depth for debug traces
	 */
	private int maxTraceCount = 10
	
	/**
	 * Max output depth
	 */
	private int maxDepth = 10
	
	/**
	 * Flag indicating whether line numbers should be output
	 */
	boolean includeLineNumbers = true
	
	/**
	 * Filter criteria for trace and class name output
	 */
	private ArrayList<Filter> traceFilters = null
	
	/**
	 * Exception handler for uncaptured exceptions
	 */
	private HashMap<Long, FireP4jExceptionHandler> exceptionHandlers = null
	
	/**
	 * HTTP servlet request object for getting header values
	 */
	private HttpServletRequest request = null
	/**
	 * HTTP servlet repsonse object for setting the header values
	 */
	private HttpServletResponse response = null
	
	/**
	 * Lock needed for JSON serialization
	 */
	private final ReadWriteLock jsonLock = null
	
	/**
	 * Lock needed for setting exception handlers
	 */
	private final Lock exceptionHandlerLock = null
	
	/**
	 * Lock needed for setting the header variables
	 */
	private final Lock setHeaderLock = null
	
	/**
	 * Flag indicating whether the FirePHP logger can be used
	 */
	private boolean correctVersion = false
	
	/**
	 * Current header index
	 */
  	int currentLevel = 0
  	
  	/**
  	 * Number of currently open groups
  	 */
  	int openGroups = 0
	
  	/**
  	 * This method sets the FireP4j profile that is used.
  	 * 
  	 * @param profile FireP4j profile to use
  	 */
	public static void setProfile(FireP4jProfile profile) {
		this.profile = profile		
	}
  	
	/**
	 * This method retrieves or initializes a FireP4j instance.
	 * 
	 * @param request HTTP servlet request object
	 * @param response HTTP servlet response object
	 * @param reinit Flag specifying whether the FireP4j object should be reinitialized if it was already set
	 * @return
	 */
	public static FireP4j getInstance(HttpServletRequest request, HttpServletResponse response, boolean reinit = false) {
		// If no servlet request and response objects are passed as arguments, an exception is thrown
		if (request == null || response == null) {
			throw new Exception("The servlet request and response must be set!")
		} 
		
		FireP4j instance = null
		
		// The instance is retrieved from the request object if it existed
		try {
			if (request.getAttribute("fireP4jInstance") != null) {
				instance = request.getAttribute("fireP4jInstance") as FireP4j
			}
		} catch(Exception e) {
			instance = null
		}
		
		try {
			// If no FireP4j instance existed on the request (or if it should be reinitialized), it is created and added to the request
			if (instance == null || reinit) {
				instanceCount++
				
				instance = new FireP4j(request, response)
				request.setAttribute("fireP4jInstance", instance)
			}
		} catch (Exception e) {
			// If an error occured during initialization, an exception is thrown
			throw new Exception("Error initializing the FireP4j instance: ${e}")
		}	
		
		// The current instance object is returned
		instance
	}
	
	/**
	 * This constructor initializes the FireP4j object.
	 * 
	 * @param response HTTP servlet response object for setting the header values
	 */
	public FireP4j(HttpServletRequest request, HttpServletResponse response) {
		// If no servlet response object is passed as an argument, an exception is thrown
		if (response == null || request == null) {
			throw new Exception("The servlet request and response must be set!")
		}
		
		// Class variables are set
		this.request = request
		this.response = response
		
		// The Current header index is initialized
		currentLevel = 1
		
		// The correct client FirePHP version is checked
		checkForCorrectVersion()
		
		// The JSON utility class is instantiated
		jsonUtils = new JSONUtils();
		
		// Objects are initialized
		traceFilters = new LinkedList<Filter>()
		exceptionHandlers = new HashMap<Long, FireP4jExceptionHandler>()
		
		// Locks are initialized
		jsonLock = new ReentrantReadWriteLock()
		exceptionHandlerLock = new ReentrantLock()
		setHeaderLock = new ReentrantLock()
		
		// The profile settings are set
		if (profile != null) {
			setProfileSettings()			
		}
	}
	
	/**
	 * This method takes over profile settings from the given profile.
	 */
	private void setProfileSettings() {
		// Trace filters are taken over
		if (profile.getTraceFilters() != null) {
			// Each trace filter is added to the trace filter list
			profile.getTraceFilters().each {
				addTraceFilter(it.type, it.value)
			}
		}
		
		// Path filters are overtaken
		if (profile.getPathFilters() != null) {
			// Each path filter is added to the path filter list
			profile.getPathFilters().each {
				jsonUtils.addPathFilter(it)
			}
		}
	}
	
	/**
	 * This method checks if the FirePHP version is correct.
	 */
	private void checkForCorrectVersion() {
		// In the user agent, the FirePHP version is searched for
		String userAgent = request.getHeader("User-Agent")
		def matcher = userAgent =~ /\sFirePHP\/([\.|\d]*)\s?/
		
		// The version number is determined
		String version = null
		try {
			if (matcher[0] != null && matcher[0][1] != null) {
				version = matcher[0][1]
			}
		} catch(Exception e) { }
	
		// The version number is compared with the required version number
		correctVersion = false
		if (version != null) {
			correctVersion = Versioner.compareVersions(version, minFirePHPVersion) >= 0
		} else {
			correctVersion = false
		}		
	}
	
	/**
	 * This method sets the maximum trace element count that is output in trace and exception outputs.
	 * 
	 * @param maxTraceCount Maximum trace element count
	 */
	public void setMaxTraceCount(int maxTraceCount) {
		// Execution is protected by a lock
		jsonLock.writeLock().lock()
		// Max trace count is set
		if (maxTraceCount > 0) {
			this.maxTraceCount = maxTraceCount
		}
		// The lock is removed
		jsonLock.writeLock().unlock()
	}
	
	/**
	 * This method sets the maximum serialization depth for object serialization (-1 for infinite depth).
	 * 
	 * @param maxDepth Maximum serialization depth
	 */
	public void setMaxDepth(int maxDepth) {
		// Execution is protected by a lock
		jsonLock.writeLock().lock()
		// The max depth property is set
		jsonUtils.setMaxDepth(maxDepth)
		// The lock is removed
		jsonLock.writeLock().unlock()
	}
	
	public void setIncludeLineNumbers(includeLineNumbers) {
		// Execution is protected by a lock
		jsonLock.writeLock().lock()
		// The flag indicating whether line numbers and file names should be set is output
		this.includeLineNumbers = includeLineNumbers
		// The lock is removed
		jsonLock.writeLock().unlock()		
	}
	
	/**
	 * This method sets all the options that can be configured.
	 * 
	 * @param maxTraceCount Maximum trace element count (for trace and exception output)
	 * @param maxDepth Maximum serialization depth (for object output)
	 * @param includeLineNumbers True if line numbers and file names should be included in the output, false otherwise
	 */
	public void setOptions(int maxTraceCount, int maxDepth, boolean includeLineNumbers = true) {
		// Execution is protected by a lock
		jsonLock.writeLock().lock()
		// Max trace element count is set
		if (maxTraceCount > 0) {
			this.maxTraceCount = maxTraceCount
		}
		// Max serialization depth is set
		jsonUtils.setMaxDepth(maxDepth)
		// The flag indicating line number output is set
		this.includeLineNumbers = includeLineNumbers
		// The lock is removed
		jsonLock.writeLock().unlock()
	}
	
	/**
	 * This method adds a trace filter to the list of existing trace filters.
	 * 
	 * @param filterType Filter type (@see firep4j.tools.Filter)
	 * @param filterValue Package name to be filtered
	 */
	public void addTraceFilter(Filter.Criteria filterType, String filterValue) {
		try {		
			// Execution is protected by a lock
			jsonLock.writeLock().lock()
			
			Filter filter = null
			
			// For each existing trace filter
			traceFilters.each {
				// If the filter is already present, method execution is aborted
				if (it.type == filterType && it.value == filterValue) {
					return
				}			
			}
			
			// The new filter is added to the list of filters
			filter = new Filter(type: filterType, value: filterValue)
			traceFilters.add(filter)
		} catch (Exception e) {
			// An exception is thrown again
			throw e
		} finally {
			// The lock is removed
			jsonLock.writeLock().unlock()
		}
	}
	
	/**
	 * This method adds a trace filter to the list of existing trace filters.
	 * 
	 * @param filter Trace filter
	 */
	protected void addTraceFilter(Filter filter) {
		addTraceFilter(filter.type, filter.value);
	}
	
	/**
	 * This method removes a trace filter from the list of existing trace filters.
	 * 
	 * @param filterType Filter type (@see firep4j.tools.Filter)
	 * @param filterValue Package name to be filtered
	 */
	public void removeTraceFilter(Filter.Criteria filterType, String filterValue) {
		try {
			// Execution is protected by a lock
			jsonLock.writeLock().lock()
			
			Filter filter = null	
			
			// For each existing trace filter
			def valueToRemove = null		
			traceFilters.each {
				// If the filter is present, it is remembered
				if (it.type == filterType && it.value == filterValue) {
					valueToRemove = it
				}
			}
			
			// The filter is removed from the list of filters
			if (valueToRemove != null) {
				traceFilters.remove(valueToRemove)
			}
		} catch (Exception e) {
			// An exception is thrown again
			throw e
		} finally {
			// The lock is removed
			jsonLock.writeLock().unlock()
		}
	}
	
	/**
	 * This method removes a trace filter from the list of existing trace filters.
	 * 
	 * @param filter Trace filter
	 */
	protected void removeTraceFilter(Filter filter) {
		removeTraceFilter(filter.type, filter.value);
	}
	
	/**
	 * This method tests if the given class should be output in trace or exception outputs.
	 * 
	 * @param className Class (in package notation) name to check
	 * @return True if the class should be output, false otherwise
	 */
	private boolean isValidClassName(String className) {
		def valid = true
		
		// The FireP4j class is always excluded
		if (className == "firep4j.FireP4j") {
			valid = false
		}
		
		// For all the trace filters
		for (filter in traceFilters) {
			// The class name is checked with the trace filter according to its type
			
			if (filter.type == Filter.Criteria.STARTS_WITH) {
				// Class name is checked if it starts with a specific value
				if (className.startsWith(filter.value)) {
					valid = false
				}
			} else if (filter.type == Filter.Criteria.CONTAINS) {
				// Class name is checked if it contains a specific value
				if (className.contains(filter.value)) {
					valid = false
				}
			} else if (filter.type == Filter.Criteria.ENDS_WITH) {
				// Class name is checked if it ends with a specific value
				if (className.endsWith(filter.value)) {
					valid = false
				}
			} else if (filter.type == Filter.Criteria.EQUALS) {
				// Class name is checked if it equals a specific value
				if (className == filter.value) {
					valid = false
				}
			}
		}		
		
		valid
	}
	
	/**
	 * This method adds an object filter to the list of object filters. Filtered fields will not appear in the FireP4j output.
	 * 
	 * @param className Class name for which the fields should be filtered
	 * @param fieldNames List of field names that should be filtered within the class
	 */
	public void addObjectFilter(String className, String... fieldNames) {
		try {
			// Execution is protected by a lock
			jsonLock.writeLock().lock()
			
			// The object filter is added
			jsonUtils.addObjectFilter(className, fieldNames)
		} catch (Exception e) {
			// An exception is thrown again
			throw e
		} finally {
			// The lock is removed
			jsonLock.writeLock().unlock()
		}
	}
	
	/**
	 * This method removes an object filter from the list of object filters.
	 * 
	 * @param className Class name for which the fields should be filtered
	 * @param fieldNames List of field names that should be filtered within the class
	 */
	public void removeObjectFilter(String className, String... fieldNames) {
		try {
			// Execution is protected by a lock
			jsonLock.writeLock().lock()
			
			// The object filter is removed
			jsonUtils.removeObjectFilter(className, fieldNames)
		} catch (Exception e) {
			// An exception is thrown again
			throw e
		} finally {
			// The lock is removed
			jsonLock.writeLock().unlock()
		}
	}
	
	/**
	 * This method registers a FireP4j exception handler for a thread. If set, all uncaught exceptions will be logged to FireP4j.
	 * 
	 * @param thread The thread for which the exception handler should be registered
	 */
	public void registerExceptionHandler(Thread thread = null) {
		initExceptionHandler(thread, 1, 0, 0)
	}
	
	/**
	 * This method resets the FireP4j exception handler for a thread.
	 * 
	 * @param thread The thread for which the exception handler should be reset
	 */
	public void unregisterExceptionHandler(Thread thread = null) {
		initExceptionHandler(thread, -1, 0, 0)
	}
	
	/**
	 * This method registers a FireP4j error handler for a thread. If set, all uncaught errors will be logged to FireP4j.
	 * 
	 * @param thread The thread for which the error handler should be registered
	 */
	public void registerErrorHandler(Thread thread = null) {
		initExceptionHandler(thread, 0, 1, 0)
	}
	
	/**
	 * This method resets the FireP4j error handler for a thread.
	 * 
	 * @param thread The thread for which the error handler should be reset
	 */
	public void unregisterErrorHandler(Thread thread = null) {
		initExceptionHandler(thread, 0, -1, 0)
	}
	
	/**
	 * This method registers a FireP4j assertion error handler for a thread. If set, all uncaught assertion errors will be logged to FireP4j.
	 * 
	 * @param thread The thread for which the assertion error handler should be registered
	 */
	public void registerAssertionHandler(Thread thread = null) {
		initExceptionHandler(thread, 0, 0, 1)
	}
	
	/**
	 * This method resets the FireP4j assertion error handler for a thread.
	 * 
	 * @param thread The thread for which the assertion error handler should be reset
	 */
	public void unregisterAssertionHandler(Thread thread = null) {
		initExceptionHandler(thread, 0, 0, -1)
	}
	
	/**
	 * This method sets or resets FireP4j exception and error handlers for a specific thread.
	 * 
	 * @param thread The thread for which the changes should be made
	 * @param exceptionHandler Integer indicating whether an exception handler should be registered (> 0), reset (< 0) or nothing should be done (0)
	 * @param errorHandler Integer indicating whether an error handler should be registered (> 0), reset (< 0) or nothing should be done (0)
	 * @param assertionHandler Integer indicating whether an assertion error handler should be registered (> 0), reset (< 0) or nothing should be done (0)
	 */
	private void initExceptionHandler(Thread thread, int exceptionHandler, int errorHandler, int assertionHandler) {
		try {
			// Execution is protected by a lock
			exceptionHandlerLock.lock()
			
			// If no thread was specified, the current thread is taken
			if (thread == null) {
				thread = Thread.currentThread()
			}
			
			// The thread ID is determined
			long threadId = thread.getId()
			
			// Flags indicating whether exception, error and assertion error handlers should be registered or reset are set
			boolean setExceptionHandler = exceptionHandler > 0
			boolean setErrorHandler = errorHandler > 0
			boolean setAssertionError = assertionHandler > 0	
			boolean unsetExceptionHandler = exceptionHandler < 0
			boolean unsetErrorHandler = errorHandler < 0
			boolean unsetAssertionHandler = assertionHandler < 0
			
			FireP4jExceptionHandler handler = null
			
			if (exceptionHandlers.containsKey(threadId)) {
				// If the exception handler for that thread already exists, it is determined
				handler = exceptionHandlers[threadId]
			} else if (setExceptionHandler || setErrorHandler || setAssertionError) {
				// If the exception handler for that thread does not exists, it is created
				handler = new FireP4jExceptionHandler(this)
				// The exception handler is registered and put in the exception handler list
				thread.setUncaughtExceptionHandler(handler)			
				exceptionHandlers[threadId] = handler
			} else {
				return
			}
			
			// Handler flags are set or reset for the exception handler
			
			// Exceptions
			if (setExceptionHandler) { handler.captureExceptions = true }
			else if (unsetExceptionHandler) { handler.captureExceptions = false }
			
			// Errors
			if (setErrorHandler) { handler.captureErrors = true }
			else if (unsetErrorHandler) { handler.captureErrors = false }
			
			// Assertion errors
			if (setAssertionError) { handler.captureAssertions = true }
			else if (unsetAssertionHandler) { handler.captureAssertions = false }
			
			// If there is nothing to do anymore for the exception handler, it is deleted
			if (!handler.isHandlerDefined()) {
				thread.setUncaughtExceptionHandler(null)
				exceptionHandlers.remove(handler)
			}
		} catch(Exception e) {
			// An exception is thrown again
			throw e
		} finally {
			// The lock is removed
			exceptionHandlerLock.unlock()
		}
	}
	
	/**
	 * This method returns the instance number of the current FireP4j object.
	 * It is used for debugging and testing purposes only.
	 * 
	 * @return Instance number
	 */
	public int getInstanceCount() {
		instanceCount
	}
	
	/**
	 * This method opens a new group in FireP4j.
	 * 
	 * @param label Group label
	 */
	public void group(String label, boolean collapsed = false) {
		// If no label was given, an exception is thrown
		if (label == null || label == "") {
			throw new Exception("Please specify a group name!")			
		}
		
		// The number of currently open groups is incremented
		openGroups++
		
		// According to the collapsed flag, the group is opened either in a collapsed or in an open way
		if (collapsed) {
			fireBug(null, label, FireP4jLogLevel.COLLAPSED_GROUP_START)
		} else {
			fireBug(null, label, FireP4jLogLevel.GROUP_START)
		}
	}
	
	/**
	 * This method ends an existing group in FireP4j. 
	 */
	public void groupEnd() {
		// Groups can only be closed if they were opened before
		if (openGroups > 0) {
			// The number of currently open groups is decremented
			openGroups--
			// The group is closed
			fireBug(null, null, FireP4jLogLevel.GROUP_END)
		}
	}
	
	/**
	 * This method dumps the given object with the given label to the FireBug net tab.
	 * 
	 * @param label Object label
	 * @param object Object
	 */
	public void dump(String label, Serializable object) {
		fireBug(object, label, FireP4jLogLevel.DUMP)
	}
	
	/**
	 * This method outputs the given object with the given label to the FireBug console with log level LOG.
	 * 
	 * @param object Object to log
	 * @param label Label of the object
	 */
	public void log(Serializable object, String label = null) {
		fireBug(object, label, FireP4jLogLevel.LOG)
	}
	
	/**
	 * This method outputs the given object with the given label to the FireBug console with log level INFO.
	 * 
	 * @param object Object to log
	 * @param label Label of the object
	 */
	public void info(Serializable object, String label = null) {
		fireBug(object, label, FireP4jLogLevel.INFO)
	}
	
	/**
	 * This method outputs the given object with the given label to the FireBug console with log level WARN.
	 * 
	 * @param object Object to log
	 * @param label Label of the object
	 */
	public void warn(Serializable object, String label = null) {
		fireBug(object, label, FireP4jLogLevel.WARN);
	}
	
	/**
	 * This method outputs the given object with the given label to the FireBug console with log level ERROR.
	 * 
	 * @param object Object to log
	 * @param label Label of the object
	 */
	public void error(Serializable object, String label = null) {
		fireBug(object, label, FireP4jLogLevel.ERROR)
	}
	
	/**
	 * This method outputs the given table with the given label to the FireBug console with log level TABLE.
	 * 
	 * @param object Table to log
	 * @param label Label of the table
	 */
	public void table(Table table, String label = null) {
		// If no label was given, an exception is thrown
		if (label == null || label == "") {
			throw new Exception("Please specify a table name!")			
		}
		// The table is output
		fireBug(table, label, FireP4jLogLevel.TABLE)
	}
	
	/**
	 * This method outputs the current stack trace with the given label to the FireBug.
	 * 
	 * @param label Label of the trace output
	 */
	public void trace(String label) {
		fireBug(null, label, FireP4jLogLevel.TRACE)
	}
	
	/**
	 * This is the main method in FireP4j. It sets the HTTP headers that are needed for FireP4j output.
	 * 
	 * @param object Object to log
	 * @param label Label of the object
	 * @param logLevel Output log level
	 */
	private void fireBug(Serializable object, String label, FireP4jLogLevel logLevel) {
		// If the response object was not set, an exception is thrown
		if (response == null) {
			throw new Exception("The object was not initialized properly!")
		}
		
		// If there is no correct FirePHP installation or FireP4j is disabled, method execution is aborted
		if (!enabled || !correctVersion) {
			return
		}
	
		// If the object is an exception or an error, the log level is changed to exception
		if (object instanceof Throwable) {
			logLevel = FireP4jLogLevel.EXCEPTION
		}
		
	  	String headerBody = ""
		
		try {
			// Execution is protected by the JSON serialization lock
			jsonLock.readLock().lock()
			
			// The payload (the main output) is determined
		  	String headerBodyPayload = getHeaderBodyPayload(label, object, logLevel)
		  	
		  	// If there is nothing to output for a trace or exception statement, method execution is aborted
		  	if ((logLevel == FireP4jLogLevel.TRACE || logLevel == FireP4jLogLevel.EXCEPTION) && headerBodyPayload == "{}") {
		  		return
		  	}
			
		  	// The protocol HTTP headers are set (stating the protocol versions) 
			setInitialHeaders(logLevel)
			
		  	if (logLevel != FireP4jLogLevel.DUMP) {
		  		// The header is determined for non-dump entries
		  		
		  		// Header body index is built (determining file properties, labels, etc)
		  		HashMap<String, String> headerBodyTitleEntries = getHeaderBodyTitleEntries(label, logLevel)
		  		String headerBodyIndex = buildHeaderBodyIndex(headerBodyTitleEntries)	
		  		
		  		// The complete header body is determined
		  		headerBody = "[" + headerBodyIndex + "," + headerBodyPayload + "]"
		  	} else {
		  		// The header is determined for dump entries	  		
		  		headerBody = getDumpHeaderBody(label, headerBodyPayload)  		
		  	}
		} catch (Exception e) {
			// If an exception occurs, it is thrown again
			throw e
		} finally {
			// The lock is always removed
			jsonLock.readLock().unlock()
		}
		
		// Header body is chunked if got too big
	  	LinkedList<String> parts = chunkHeaderBody(headerBody)
	  	
	  	// The different header chunks are rendered
	  	renderHeaderChunks(parts, headerBody.length(), logLevel)
	}
	
	/**
	 * This method sets default protocol headers.
	 * 
	 * @param logLevel Log level that is used
	 */
	private void setInitialHeaders(FireP4jLogLevel logLevel) {
		// Headers for protocols that are needed for every log level
	  	response.setHeader("X-Wf-Protocol-1", "http://meta.wildfirehq.org/Protocol/JsonStream/0.2")
	  	response.setHeader("X-Wf-1-Plugin-1", "http://meta.firephp.org/Wildfire/Plugin/FirePHP/Library-FirePHPCore/" + version)
    	
	  	if (logLevel != FireP4jLogLevel.DUMP) {
	  		// Non-dump protocol header
	  		response.setHeader("X-Wf-1-Structure-1", "http://meta.firephp.org/Wildfire/Structure/FirePHP/FirebugConsole/0.1")
	  	} else {
	  		// Dump protocol header
	  		response.setHeader("X-Wf-1-Structure-2", "http://meta.firephp.org/Wildfire/Structure/FirePHP/Dump/0.1")
	  	}
	}
	
	/**
	 * This method returns all the entries that are to be rendered in the header body index.
	 * 
	 * @param label Label of the output
	 * @param logLevel Log level that is used
	 * @return HashMap containing all the header body entries as key value pairs
	 */
	private HashMap<String, String> getHeaderBodyTitleEntries(String label, FireP4jLogLevel logLevel) {
		String fileName = ""
		int lineNumber = 0
		
		// If line numbers and file names should be included
		def foundFile = false
		if (includeLineNumbers) {
			String className = "";
			int i = 4;
			def currentElement = null;
			// For all the stack trace elements
			for (; i < Thread.currentThread().getStackTrace().length; i++) {
				// Class name, file name and line numbers are determined for the current stack trace element
				currentElement = Thread.currentThread().getStackTrace()[i]
				className = currentElement.getClassName()				
				lineNumber = currentElement.getLineNumber()
				fileName = currentElement.getFileName()
				
				// If the class name is not excluded by trace path excludes, it is taken
				if (isValidClassName(className) && lineNumber != -1) {
					foundFile = true
					break
				}
			}
		}
		
		// The "real" log level is determined (the collapsed group start level is transformed to a "normal" group start level)
		def realLogLevel = logLevel
		if (logLevel == FireP4jLogLevel.COLLAPSED_GROUP_START) {
			realLogLevel = FireP4jLogLevel.GROUP_START
		}
		
		HashMap<String, String> headerTitleEntries = new HashMap<String, String>()
	  	
		// The header body indexes are set
		
		// log level
		headerTitleEntries["Type"] = realLogLevel as String
	  	
		// group collapse state
		if (logLevel == FireP4jLogLevel.COLLAPSED_GROUP_START) {
			headerTitleEntries["Collapsed"] = "true"
		}
		
		// Line numbers and file names
	  	if (includeLineNumbers && foundFile) {
	  		headerTitleEntries["File"] = fileName
	  		headerTitleEntries["Line"] = lineNumber as String
	  	}
	  	
	  	// Label of the output
	  	if (label != null && logLevel != FireP4jLogLevel.TRACE && logLevel != FireP4jLogLevel.EXCEPTION) {
	  		headerTitleEntries["Label"] = label
	  	}
	  	
	  	headerTitleEntries		
	}
	
	/**
	 * This method builds the header body index from a HashMap containing the required values as key value pairs.
	 * 
	 * @param entries HashMap containing the required values
	 * @return Header body index
	 */
	private String buildHeaderBodyIndex(HashMap<String, String> entries) {
		Iterator<String> iterator = entries.keySet().iterator()
		
		// The header body index is initialized
		String headerBodyIndex = "{";	
		
		// For each key value pair
		entries.each { key, value ->
			// Key and value are JSON encoded
			key = jsonUtils.serialize(key)
			value = jsonUtils.serialize(value)	
			
			// Number values are converted to "real" numbers
			if (value.matches(/^"[0-9]+"?/)) {
				value = value.replace(/"/, "")
			}
			
			// The entry is added to the header body index
			headerBodyIndex +=  "${key}:${value},"
		}
		// The last comma is removed from the header body index
		headerBodyIndex = headerBodyIndex.substring(0, headerBodyIndex.size() - 1)
		
		// The header body index is ended
		headerBodyIndex += "}"
		
		headerBodyIndex
	}
	
	/**
	 * This method determines the header body payload (main output).
	 * 
	 * @param label Label of the object
	 * @param object Object that should be output
	 * @param logLevel Log level that is used
	 * @return Header body payload
	 */
	private String getHeaderBodyPayload(String label, Object object, FireP4jLogLevel logLevel) {
		String payload = "";
		
		if (logLevel == FireP4jLogLevel.LOG ||
		    logLevel == FireP4jLogLevel.INFO ||
		    logLevel == FireP4jLogLevel.WARN ||
		    logLevel == FireP4jLogLevel.ERROR ||
		    logLevel == FireP4jLogLevel.DUMP) {
			// For LOG, INFO, WARN, ERROR and DUMP, the object is serialized using JSON utils			
	  		payload = jsonUtils.serialize(object)
	  	} else if (logLevel == FireP4jLogLevel.GROUP_START || logLevel == FireP4jLogLevel.COLLAPSED_GROUP_START || logLevel == FireP4jLogLevel.GROUP_END) {
	  		// For GROUP log levels, no payload is specified
	  		payload = "null"
		} else if (logLevel == FireP4jLogLevel.TABLE) {
			// For the TABLE log level, the given table is serialized
	  		Table table = (Table)object	
	  		payload = table.serialize(jsonUtils)
	  	} else if (logLevel == FireP4jLogLevel.TRACE || logLevel == FireP4jLogLevel.EXCEPTION) {
	  		// For trace and exception log levels, the current trace is determined
	  		payload = getTrace(label, object, logLevel)
	  	}
		
		payload
	}
	
	/**
	 * This method renders the output for exception and trace log levels.
	 * 
	 * @param label Label of the output (not used for exception output)
	 * @param object Exception to output (not used for trace output)
	 * @param logLevel log level to use
	 * @return
	 */
	private String getTrace(String label, Object object, FireP4jLogLevel logLevel) {
		def stackTrace = null
		StackTraceElement traceElement = null
		      
		String fileName = ""
		String className = ""
		String methodName = ""
		int lineNumber = 0
		String message = ""
		String type = ""
		Throwable raisedThrowable = null
		
		// Stack trace and label are determined
		if (logLevel == FireP4jLogLevel.EXCEPTION) {
			// For an exception output, the exception stack trace and exception message are taken
			raisedThrowable = (Throwable)object	
			
			stackTrace = raisedThrowable.getStackTrace()
			message = raisedThrowable.getMessage()
		} else {
			// For a trace output, the current stack trace and the label given are taken
			stackTrace = Thread.currentThread().getStackTrace()
			message = label
		}
		
		int i = 1
		int stackTraceCount = 0
		
		// The output is initialized
		String trace = "{"
		
		// For all the elements in the stack trace
		while (i < stackTrace.length && stackTraceCount < maxTraceCount) {
			// Current file name, class name, method name and line number are determined
			traceElement = stackTrace[i]			                          
			fileName = traceElement.getFileName()
			className = traceElement.getClassName()
			methodName = traceElement.getMethodName()
			lineNumber = traceElement.getLineNumber()
			type = "."
			i++
			
			// If the trace path is excluded for that class, it is not integrated in the output
			if (!isValidClassName(className) || lineNumber == -1) {
				continue;
			}
			
			// The element is integrated into the output
			
			stackTraceCount++
			
			if (stackTraceCount == 1) {
				// The first stack trace element looks different than the others
				
				// For exceptions, the first element is a throw statement (and the class name is refined)
				if (logLevel == FireP4jLogLevel.EXCEPTION) {
					className = raisedThrowable.getClass().toString().replace("class ", "").replace("java.lang.", "")
					type = "throw"
				}
				
				// The output is set
				trace +=  "\"Class\":\"${className}\","
				trace += "\"Type\":\"${type}\","
				trace += "\"Function\":\"${methodName}\","
				trace += "\"Message\":\"${message}\","
				trace += "\"File\":\"${fileName}\","
				trace += "\"Line\":${lineNumber},"
				trace += "\"Args\":[],"
				trace += "\"Trace\":["
			} else {
				// Commas are added as stack trace separators
				if (stackTraceCount > 2) {
					trace += ","
				}
				// The output is set
				trace += "{"		
				trace += "\"file\":\"${fileName}\","
				trace += "\"line\":${lineNumber},"
				trace += "\"function\":\"${methodName}\","
				trace += "\"class\":\"${className}\","
				trace += "\"object\": \"\","
				trace += "\"type\":\".\","
				trace += "\"args\":[]"
				trace += "}"
			}
		}
		// The stack trace end character is set (if there was a stack trace)
		if (stackTraceCount > 0) {
			trace += "]"
		}
		
		// The output is ended
		trace += "}"
			
		trace
	}
	
	/**
	 * This method creates the header body for a dump output.
	 * 
	 * @param label Label of the output
	 * @param payloadString Payload string
	 * @return Complete header body
	 */
	private String getDumpHeaderBody(String label, String payloadString) {
		// The label of the output is serialized
		String labelString = jsonUtils.serialize(label)
		// The complete header body is determined
		String headerBody = "{${labelString}:${payloadString}}"
		
		headerBody
	}
	
	
	/**
	 * This method chunks the complete header body into parts.
	 * 
	 * @param headerBody Complete header body
	 * @return List of the header chunks
	 */
	private LinkedList<String> chunkHeaderBody(String headerBody) {
	  	LinkedList<String> parts = new LinkedList<String>()		
	  	// At first, the complete header body remains
	  	String remainingPart = headerBody
	  	
	  	// While there is still something in the remaining header body
	  	while (remainingPart.length() > 0) {	  		
	  		if (remainingPart.length() > chunkSize) {
	  			// If there are more characters in the remaining header body than are allowed
	  			
	  			// A new header chunk is added to the chunks and the remaining part is calculated
	  			parts.add(remainingPart.substring(0, chunkSize))
	  			remainingPart = remainingPart.substring(chunkSize)
	  		} else {
	  			// If there are less characters in the remaining header body than are allowed
	  			
	  			// The remaining header body is rendered as one chunk
	  			parts.add(remainingPart)
	  			remainingPart = ""
	  		}
	  	}
	  	
	  	parts
	}
	
	/**
	 * This method renders every header body chunk.
	 * 
	 * @param headerTitle Title of the header body
	 * @param parts Header body chunks
	 * @param messageLength Length of the complete header body
	 * @param logLevel Log level to use
	 */
	private void renderHeaderChunks(LinkedList<String> parts, int messageLength, FireP4jLogLevel logLevel) {
		// Execution is protected by a lock
		setHeaderLock.lock()
		
	  	String completeHeader = ""
		
	  	// The first chunk header title and body is set
		String headerTitle = buildHeaderTitle(logLevel)
	  	completeHeader = "${messageLength}|${parts.get(0)}|" + ((parts.size() > 1) ? "\\" : "")
	  	// The header is added to the HttpServletResponse
	  	response.setHeader(headerTitle, completeHeader)
	  	// The current header index is increased
	  	increaseCurrentLevel()

	  	// For all the remaining chunks
	  	for (int i = 1; i < parts.size(); i++) {
		  	// The chunk header title and body is set
	  		headerTitle = buildHeaderTitle()
	  		completeHeader = "|${parts.get(i)}|" + ((parts.size() > i + 1) ? "\\" : "")
	  		// The header is added to the HttpServletResponse
	  		response.setHeader(headerTitle,  completeHeader)
	  		// The current header index is increased
	  		increaseCurrentLevel()
	  	}
	  	
	  	// The latest header index is set
		response.setHeader("X-Wf-1-Index", (currentLevel - 1) as String)
		
		// The lock is removed
		setHeaderLock.unlock()
	}
	
	/**
	 * This method returns the current header title.
	 * 
	 * @param level Log level that is used
	 * @return Header title
	 */
	private String buildHeaderTitle(FireP4jLogLevel level) {
		def logLevelIndex = 1
		// For dump outputs, the header title needs to be different
		if (level == FireP4jLogLevel.DUMP) {
			logLevelIndex = 2
		}
		
		// The header title is determined
		String title = "X-Wf-1-${logLevelIndex}-1-${currentLevel}"
		
		title
	}
	
	/**
	 * This method increases the current header title index.
	 */
	private void increaseCurrentLevel() {
		// The current index is increased
		int level = currentLevel + 1
		
		// If the index reaches 100000, an exception is thrown
		if (level > 99999) {
			throw new Exception("Max log level exceeded!")	
		}
		
		// The current index is set
		currentLevel = level
	}
}