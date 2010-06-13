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

import firep4j.tools.JSONUtils;
import firep4j.FireP4j;
import firep4j.Table;

import java.lang.reflect.*
import java.util.regex.*

import servlet.http.HttpServletRequestMock
import servlet.http.HttpServletResponseMock
import groovy.util.GroovyTestCase

import firep4j.objects.*
import firep4j.tools.*
import firep4j.profile.*

/**
 * This class tests core FireP4j methods.
 * 
 * @author Thomas Endres
 */
class FireP4jTests extends GroovyTestCase {
	
	/**
	 * Default message pattern (maximum 2 depth levels)
	 */
	private static final String pattern = /^[0-9]*\|\[\{(\"[a-zA-Z_]+\"\:(\"[^"]*\"|[0-9]+)(,)?)*\},(\"[^"]*\"|[0-9]+|null|\{((\"([a-zA-Z_]+:?[a-zA-Z_]*)*\"\:(\"[^"]*\"|[0-9]+|\[\]|((\{((\"([a-zA-Z_]+:?[a-zA-Z_]*)*\"\:(\"[^"]*\"|[0-9]+|\[\]))(,)?)*\})(,)?)*))(,)?)*\})\]\|?/

	/**
	 * The filename of the class
	 */
	private static final def fileName = "FireP4jTests.groovy"
	
	/**
	 * HTTP servlet response mock
	 */
	private static HttpServletRequestMock mockRequest = null
		
	/**
	 * HTTP servlet response mock
	 */
	private static HttpServletResponseMock mockResponse = null
	
	/**
	 * The FireP4j object
	 */
	private static FireP4j fireP4j = null
	
	/**
	 * The Groovy profile being used
	 */
	private static GroovyProfile profile = new GroovyProfile()
	
	/**
	 * This method sets up test variables.
	 */
    protected void setUp() {
        super.setUp()     
        
        // The HTTP servlet request and response mocks are created if they were not created before
        if (mockRequest == null) {
        	mockRequest = new HttpServletRequestMock()
        }
        if (mockResponse == null) {
        	mockResponse = new HttpServletResponseMock()
        }
        
        mockRequest.clear()
        mockResponse.clear()
        
        // The request header is set
        mockRequest.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; de; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3 FirePHP/0.4Mozilla/5.0 (Windows; U; Windows NT 6.1; de; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3 FirePHP/0.4Mozilla/5.0 (Windows; U; Windows NT 6.1; de; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3 FirePHP/0.4Mozilla/5.0 (Windows; U; Windows NT 6.1; de; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3 FirePHP/0.4Mozilla/5.0 (Windows; U; Windows NT 6.1; de; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3 FirePHP/0.4")
        
        // The FireP4j instance is initialized and refreshed
        initFireP4j()
    }
    
    /**
     * This method tears down a test after performing it
     */
    protected void tearDown() {
        super.tearDown()
    }
    
    /**
     * This method initializes the FireP4j object and refreshes its properties.
     */
    private void initFireP4j(boolean reinit = false) {
        // The FireP4j instance is created using the servlet response mock if it was not created before
        if (fireP4j == null || reinit) {
            try {
            	FireP4j.setProfile(profile)
    			fireP4j = FireP4j.getInstance(mockRequest, mockResponse, true)
    		} catch (Exception e) {
    			throw new Exception("FireP4j object could not be initialized: " + e.getMessage())
    		}
        }
        
        // Standard options are set
        fireP4j.setOptions(100, -1, true)
        
    	// The current local level is manually set
    	int currentLevel = 1
        
        // Using reflection, the FireP4j instance level is reset with these values
        try {
            Class c = fireP4j.getClass();
            Field field = c.getDeclaredField("currentLevel")
            field.setAccessible(true) 
            field.set(fireP4j, currentLevel)
        } catch (Exception e) {
        	// If there was an error, an exception is thrown
        	throw new Exception("Test could not be initialized")
        }
        
        // Using reflection, the FireP4j instance is refreshed
        try {
            Class c = fireP4j.getClass();
            Method method = c.getDeclaredMethod("checkForCorrectVersion", null)
            method.setAccessible(true) 
            method.invoke(fireP4j)
        } catch (Exception e) {
        	// If there was an error, an exception is thrown
        	throw new Exception("Test could not be initialized")
        }     
    }
    
    /**
     * This method tests output to a client with a compatible user agent request header.
     */
    void testCorrectUserAgent() {
        try {
        	// Output log
    		fireP4j.log("correct user agent")
    	} catch (Exception e) {
    		// Fail on error
			fail "error writing to fireP4j: " + e.getMessage()
		}
		
    	// The message header is retrieved and asserted (it should be present)
		def messageHeader = mockResponse.getHeader("X-Wf-1-1-1-1").toString()
		assertNotNull("write to header test", messageHeader)
    }
    
    /**
     * This method tests output to a client with an incompatible user agent request header.
     */
    void testWrongUserAgent() {
    	// Another user agent is set and FireP4j is refreshed
    	mockRequest.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/532.5 (KHTML, like Gecko) Chrome/4.1.249.1064 Safari/532.5Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/532.5 (KHTML, like Gecko) Chrome/4.1.249.1064 Safari/532.5Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/532.5 (KHTML, like Gecko) Chrome/4.1.249.1064 Safari/532.5Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/532.5 (KHTML, like Gecko) Chrome/4.1.249.1064 Safari/532.5Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/532.5 (KHTML, like Gecko) Chrome/4.1.249.1064 Safari/532.5")    	
    	initFireP4j()
    	
        try {
        	// Output log
    		fireP4j.log("incorrect user agent")
    	} catch (Exception e) {
    		// Fail on error
			fail "error writing to fireP4j: " + e.getMessage()
		}
    	
    	// The message header is retrieved and asserted (it should not be present)
		def messageHeader = mockResponse.getHeader("X-Wf-1-1-1-1")
		assertNull("write to header test", messageHeader)
    }

    /**
     * This method tests log output.
     * (File properties are set, the title is set)
     */
    void testLog() {
        try {
        	// Log output
    		fireP4j.log("logtest", "title")
		} catch (Exception e) {
			// Fail on error
			fail "error writing to fireP4j: " + e.getMessage()
		}
		
		// Standard protocol headers are asserted
    	assertProtocolHeaders(false)
    	// The header element is retrieved and asserted
    	def messageHeader = this.mockResponse.getHeader("X-Wf-1-1-1-1").toString()
		assertStandardProperties(messageHeader, FireP4j.FireP4jLogLevel.LOG, "title", "logtest")
		// The header index is asserted
		assertMessageIndexHeader(1)
	}
    
    /**
     * This method tests info output.
     * (File properties are set, the title is set)
     */
    void testInfo() {
        try {
        	// Info output
    		fireP4j.info("infotest", "title")
		} catch (Exception e) {
			// Fail on error
			fail "error writing to fireP4j: " + e.getMessage()
		}
		
		// Standard protocol headers are asserted
    	assertProtocolHeaders(false)  
    	// The header element is retrieved and asserted
		def messageHeader = mockResponse.getHeader("X-Wf-1-1-1-1").toString()
		assertStandardProperties(messageHeader, FireP4j.FireP4jLogLevel.INFO, "title", "infotest")
		// The header index is asserted
		assertMessageIndexHeader(1)		
	}
    
    /**
     * This method tests warn output.
     * (File properties are set, the title is set)
     */
    void testWarn() {
        try {
        	// Warn output
    		fireP4j.warn("warntest", "title")
		} catch (Exception e) {
			// Fail on error
			fail "error writing to fireP4j: " + e.getMessage()
		}
		
		// Standard protocol headers are asserted
    	assertProtocolHeaders(false)
    	// The header element is retrieved and asserted
		def messageHeader = mockResponse.getHeader("X-Wf-1-1-1-1").toString()
		assertStandardProperties(messageHeader, FireP4j.FireP4jLogLevel.WARN, "title", "warntest")
		// The header index is asserted
		assertMessageIndexHeader(1)
    }
    
    /**
     * This method tests error output.
     * (File properties are set, the title is set)
     */    
    void testError() {
        try {
        	// Error output
    		fireP4j.error("errortest", "title")
		} catch (Exception e) {
			// Fail on error
			fail "error writing to fireP4j: " + e.getMessage()
		}
		
		// Standard protocol headers are asserted
    	assertProtocolHeaders(false)
    	// The header element is retrieved and asserted
		def messageHeader = mockResponse.getHeader("X-Wf-1-1-1-1").toString()
		assertStandardProperties(messageHeader, FireP4j.FireP4jLogLevel.ERROR, "title", "errortest")
		// The header index is asserted
		assertMessageIndexHeader(1)	
    }
    
    /**
     * This method tests output of a table.
     * (File properties are set)
     */
    void testTable() {
    	// The table structure is created
       Table table = new Table(2);
       table.setHeaders("H1", "H2")
       table.addRow("0", "1");
       
       try {
   	    	// Table output without title (should result in an exception)
       		fireP4j.table(table, "")
   			// Fail if there was no exception
			fail "intentional exception was not fired"
   		} catch (Exception e) {	}
       
       try {
    	    // Table output
        	fireP4j.table(table, "title")
    	} catch (Exception e) {
    		// Fail on error
			fail "error writing to fireP4j: " + e.getMessage()
		}
    	
		// Standard protocol headers are asserted
    	assertProtocolHeaders(false)
    	
    	// The message header is retrieved
		def messageHeader = mockResponse.getHeader("X-Wf-1-1-1-1").toString()
		// The serialized table is removed from the message header (the output of its serializer is tested elsewhere)
		messageHeader = messageHeader.replace(table.serialize(new JSONUtils()), "null")
		
    	// The message header is retrieved and asserted
		assertStandardProperties(messageHeader, FireP4j.FireP4jLogLevel.TABLE,"title", null, true, false)
		// The header index is asserted
		assertMessageIndexHeader(1)	
    }
    
    /**
     * This method tests output of a debug trace.
     * (File properties are not set)
     */
    void testTrace() {
    	try {
    		// File properties are disabled
	    	fireP4j.setOptions(100, -1, false)
    	    // Table output
        	fireP4j.trace("trace")
    	} catch (Exception e) {
    		// Fail on error
			fail "error writing to fireP4j: " + e.getMessage()
		}    	

    	// Standard protocol headers are asserted
		assertProtocolHeaders(false)
    	// The message header is retrieved
		def messageHeader = mockResponse.getHeader("X-Wf-1-1-1-1").toString()		
    	// The desired pattern is matched with the header
    	assertTrue("regex test on header ${messageHeader}", messageHeader.matches(pattern))
    	// The log level is tested
		assertTrue("correct type on header ${messageHeader}", messageHeader.contains("\"Type\":\"${FireP4j.FireP4jLogLevel.TRACE}\""))
		// The occurence of the some classes, methods, ... is asserted
		assertTrue("caller on header ${messageHeader}", messageHeader.contains("\"File\":\"${fileName}\""))
		assertTrue("occurence of the test class on header ${messageHeader}", messageHeader.contains("\"Class\":\"firep4j.FireP4jTests\""))
		assertTrue("occurence of the testException() function on header ${messageHeader}", messageHeader.contains("\"Function\":\"testTrace\""))
		assertTrue("occurence of the exception message on header ${messageHeader}", messageHeader.contains("\"Message\":\"trace\""))
		assertTrue("occurence of an empty stack trace on header ${messageHeader}", messageHeader.contains("\"Args\":[]") && messageHeader.contains("\"Trace\":[]"))
		assertTrue("correct number of stack trace elements (1) on header ${messageHeader}", messageHeader.replaceAll("[^{]","").length() == 2)
		
		// The header index is asserted
    	assertMessageIndexHeader(1)
    }
    
    /**
     * This method tests output of a debug trace when the output is filtered
     * (File properties are not set)
     */
    void testFilteredTrace() {
    	try {
    		// File properties are disabled and the trace filter is set
	    	fireP4j.setOptions(100, -1, false)
	    	fireP4j.addTraceFilter(Filter.Criteria.STARTS_WITH, "firep4j.")
    	    // Table output
        	fireP4j.trace("trace") 	
    	} catch (Exception e) {
    		// Fail on error
			fail "error writing to fireP4j: " + e.getMessage()
		}  finally {
        	// The trace filter is removed
        	fireP4j.removeTraceFilter(Filter.Criteria.STARTS_WITH, "firep4j.")
		}

		// All the trace paths should have been removed
		// When there are no classes to trace, nothing is output
				
		// No headers are expected (so this is asserted)
        
    	// Protocol header
    	def header = mockResponse.getHeader("X-Wf-Protocol-1")
    	assertNull("no JSON header", header)    	
    	// Plugin header
    	header = mockResponse.getHeader("X-Wf-1-Plugin-1")    	
    	assertNull("no FireP4j header", header)    	
  		// Console header (if current loglevel isn't dump
    	header = mockResponse.getHeader("X-Wf-1-Structure-1")    	
    	assertNull("no FireBug header", header)
    	
    	// message header
		header = mockResponse.getHeader("X-Wf-1-1-1-1")
		assertNull("no message header", header);
    	
    	// message index header
		header = mockResponse.getHeader("X-Wf-1-Index")
		assertNull("no message index header", header)
    }
    	
    /**
     * This method tests exception output.
     * (File properties are set, the title is set)
     */
    void testException() {
    	try {
    		// File properties are disabled
	    	fireP4j.setOptions(100, -1, false)
    		try {
    			// An intentional exception is thrown
    			throw new Exception("intentional exception")   			
    		} catch (Exception e) {
            	// The exception is caught and logged
    			fireP4j.log(e, "exception message")
    		}
    	} catch (Exception e) {
    		// Fail on error
			fail "error writing to fireP4j: " + e.getMessage()
		}
    	
    	// Standard protocol headers are asserted
		assertProtocolHeaders(false)
		
		// The message header is retrieved
		String messageHeader = mockResponse.getHeader("X-Wf-1-1-1-1").toString()
    	// The log level is tested
		assertTrue("correct type on header ${messageHeader}", messageHeader.contains("\"Type\":\"${FireP4j.FireP4jLogLevel.EXCEPTION}\""))
		// The occurence of the some classes, methods, ... is asserted
		assertTrue("caller on header ${messageHeader}", messageHeader.contains("\"File\":\"${fileName}\""))
		assertTrue("occurence of the exception class on header ${messageHeader}", messageHeader.contains("\"Class\":\"Exception\""))
		assertTrue("occurence of the testException() function on header ${messageHeader}", messageHeader.contains("\"Function\":\"testException\""))
		assertTrue("occurence of the exception message on header ${messageHeader}", messageHeader.contains("\"Message\":\"intentional exception\""))
		assertTrue("occurence of an empty stack trace on header ${messageHeader}", messageHeader.contains("\"Args\":[]") && messageHeader.contains("\"Trace\":[]"))
		assertTrue("correct number of stack trace elements (1) on header ${messageHeader}", messageHeader.replaceAll("[^{]","").length() == 2)
		assertTrue("no occurence of the label within the exception message", !messageHeader.contains("\"Label\":\"exception message\""))
		
		// The header index is asserted
    	assertMessageIndexHeader(1)
	}
    
    /**
     * This method tests variable dumps to the Firebug server tab.
     */
    void testDump() {
    	try {
    		// Dump output
    		fireP4j.dump("title", "dump")
    	} catch (Exception e) {
    		// Fail on error
			fail "error writing to fireP4j: " + e.getMessage()
		}
    	
    	// Standard protocol headers are asserted
		assertProtocolHeaders(true)
    	
		// The message header is retrieved and compared to the correct header string value
		def messageHeader = mockResponse.getHeader("X-Wf-1-2-1-1").toString()
		assertTrue("dump header test on header ${messageHeader}", messageHeader == "16|{'title':'dump'}|" || messageHeader == "16|{\"title\":\"dump\"}|")
		
		// The header index is asserted
		assertMessageIndexHeader(1)	
    }
    
    /**
     * This method tests output of a complete group.
     * (File properties are set, the title is set)
     */
    void testGroup() {
        try {
   	    	// Table output without title (should result in an exception)
       		fireP4j.group("")
   			// Fail if there was no exception
			fail "intentional exception was not fired"
   		} catch (Exception e) {	}
   		
    	try {
        	// Group start
        	fireP4j.group("group")
        	// Inner error output
    		fireP4j.error("grouptest", "title")
    		// Group end
    		fireP4j.groupEnd()
    	} catch (Exception e) {
    		// Fail on error
			fail "error writing to fireP4j: " + e.getMessage()
		}
    	
    	// Standard protocol headers are asserted
    	assertProtocolHeaders(false)
    	// Header elements 1 to 3 are retrieved and asserted
		def messageHeader1 = mockResponse.getHeader("X-Wf-1-1-1-1").toString()
		assertStandardProperties(messageHeader1, FireP4j.FireP4jLogLevel.GROUP_START, "group")
		def messageHeader2 = mockResponse.getHeader("X-Wf-1-1-1-2").toString()
		assertStandardProperties(messageHeader2, FireP4j.FireP4jLogLevel.ERROR, "title", "grouptest")
		def messageHeader3 = mockResponse.getHeader("X-Wf-1-1-1-3").toString()
		assertStandardProperties(messageHeader3, FireP4j.FireP4jLogLevel.GROUP_END)
		// The header index is asserted
		assertMessageIndexHeader(3)
    }
    
    /**
     * This method tests output of a complete group.
     * (File properties are set, the title is set)
     */
    void testCollapsedGroup() {
        try {
        	// Group start
        	fireP4j.group("group", true)
        	// Inner error output
    		fireP4j.error("grouptest", "title")
    		// Group end
    		fireP4j.groupEnd()
    	} catch (Exception e) {
    		// Fail on error
			fail "error writing to fireP4j: " + e.getMessage()
		}
    	
    	// Standard protocol headers are asserted
    	assertProtocolHeaders(false)
    	// Header elements 1 to 3 are retrieved and asserted
		def messageHeader1 = mockResponse.getHeader("X-Wf-1-1-1-1").toString()
		assertStandardProperties(messageHeader1, FireP4j.FireP4jLogLevel.GROUP_START, "group")
		// The keyword "Collapsed" must appear somewhere in the header
		assertTrue("", messageHeader1.contains("\"Collapsed\":\"true\""))		
		def messageHeader2 = mockResponse.getHeader("X-Wf-1-1-1-2").toString()
		assertStandardProperties(messageHeader2, FireP4j.FireP4jLogLevel.ERROR, "title", "grouptest")
		def messageHeader3 = mockResponse.getHeader("X-Wf-1-1-1-3").toString()
		assertStandardProperties(messageHeader3, FireP4j.FireP4jLogLevel.GROUP_END)
		// The header index is asserted
		assertMessageIndexHeader(3)
    }
    
    /**
     * This method tests output of a nested group.
     * (File properties are set, the title is set)
     */
    void testNestedGroup() {
        try {
        	// Group start
        	fireP4j.group("group")
        	fireP4j.group("group")
        	// Inner log output
    		fireP4j.log("nested grouptest")
    		// Group end
    		fireP4j.groupEnd()
    		fireP4j.groupEnd()
    	} catch (Exception e) {
    		// Fail on error
			fail "error writing to fireP4j: " + e.getMessage()
		}
		
		// Standard protocol headers are asserted
    	assertProtocolHeaders(false)
    	// Header elements 1 to 5 are retrieved and asserted
		def messageHeader1 = mockResponse.getHeader("X-Wf-1-1-1-1").toString()
		assertStandardProperties(messageHeader1, FireP4j.FireP4jLogLevel.GROUP_START, "group")
		def messageHeader2 = mockResponse.getHeader("X-Wf-1-1-1-2").toString()
		assertStandardProperties(messageHeader2, FireP4j.FireP4jLogLevel.GROUP_START, "group")
		def messageHeader3 = mockResponse.getHeader("X-Wf-1-1-1-3").toString()
		assertStandardProperties(messageHeader3, FireP4j.FireP4jLogLevel.LOG, null, "nested grouptest")
		def messageHeader4 = mockResponse.getHeader("X-Wf-1-1-1-4").toString()
		assertStandardProperties(messageHeader4, FireP4j.FireP4jLogLevel.GROUP_END)
		def messageHeader5 = mockResponse.getHeader("X-Wf-1-1-1-5").toString()
		assertStandardProperties(messageHeader5, FireP4j.FireP4jLogLevel.GROUP_END)
		// The header index is asserted
		assertMessageIndexHeader(5)
    }
    
    /**
     * This method tests ending a group that has not been started.
     */
    void testCloseNotExistingGroup() {
        try {
    		// Group end
    		fireP4j.groupEnd()
        } catch (Exception e) {
    		// Fail on error
			fail "error writing to fireP4j: " + e.getMessage()
		}
        
		// No headers are expected (so this is asserted)
        
    	// Protocol header
    	def header = mockResponse.getHeader("X-Wf-Protocol-1")
    	assertNull("no JSON header", header)    	
    	// Plugin header
    	header = mockResponse.getHeader("X-Wf-1-Plugin-1")    	
    	assertNull("no FireP4j header", header)    	
  		// Console header (if current loglevel isn't dump
    	header = mockResponse.getHeader("X-Wf-1-Structure-1")    	
    	assertNull("no FireBug header", header)
    	
    	// message header
		header = mockResponse.getHeader("X-Wf-1-1-1-1")
		assertNull("no message header", header);
    	
    	// message index header
		header = mockResponse.getHeader("X-Wf-1-Index")
		assertNull("no message index header", header)
    }
    
    /**
     * This method tests output of a long message.
     * (File properties are set, the title is not set)
     */
    void testLongMessage() {
    	// A long message is created
    	String foobar = "foobar"
    	foobar = foobar + foobar + foobar + foobar + foobar
    	foobar = foobar + foobar + foobar + foobar + foobar
    	foobar = foobar + foobar + foobar + foobar + foobar
    	foobar = foobar + foobar + foobar + foobar + foobar
    	foobar = foobar + foobar
    	
        try {
        	// Log output
    		fireP4j.log(foobar)
    	} catch (Exception e) {
    		// Fail on error
			fail "error writing to fireP4j: " + e.getMessage()
		}
    	
		// Standard protocol headers are asserted
    	assertProtocolHeaders(false)
    	
    	// Message headers 1 and 2 are retrieved
		String messageHeader1 = mockResponse.getHeader("X-Wf-1-1-1-1").toString()
		String messageHeader2 = mockResponse.getHeader("X-Wf-1-1-1-2").toString()
		
		// The message headers are concatenated (they then form a "normal" header)
		String messageHeader = messageHeader1.substring(0, messageHeader1.size() - 2)+ messageHeader2.substring(1)
		
		// The message header is asserted
		assertStandardProperties(messageHeader, FireP4j.FireP4jLogLevel.LOG, null, foobar)
		
		// The header index is asserted
		assertMessageIndexHeader(2)
    }
    
    /**
     * This method tests output of a message without a title.
     * (File properties are set, the title is not set)
     */
    void testWithoutTitle() {
        try {
        	// Log output without a title
    		fireP4j.log("logtest titleless")
    	} catch (Exception e) {
    		// Fail on error
			fail "error writing to fireP4j: " + e.getMessage()
		}
		
		// Standard protocol headers are asserted
    	assertProtocolHeaders(false)
    	// The message header is retrieved and asserted
		def messageHeader = mockResponse.getHeader("X-Wf-1-1-1-1").toString()
		assertStandardProperties(messageHeader, FireP4j.FireP4jLogLevel.LOG, null, "logtest titleless")
		// The header index is asserted
		assertMessageIndexHeader(1)
    }
    
    /**
     * This method tests output of a message without file properties.
     * (File properties are not set, the title is not set)
     */
    void testWithoutFileProperties() {
        try {
        	// File properties are deactivated
        	fireP4j.setOptions(100, -1, false)
        	// Warn output without file properties
    		fireP4j.warn("warntest titleless")
    	} catch (Exception e) {
    		// Fail on error
			fail "error writing to fireP4j: " + e.getMessage()
		}
    	
		// Standard protocol headers are asserted
    	assertProtocolHeaders(false)
    	// The message header is retrieved and asserted
		def messageHeader = mockResponse.getHeader("X-Wf-1-1-1-1").toString()
		assertStandardProperties(messageHeader, FireP4j.FireP4jLogLevel.WARN, null, "warntest titleless", false)
		// The header index is asserted
		assertMessageIndexHeader(1)	
    }
    
    /**
     * This method tests output of an object.
     * (File properties are set, the title is not set)
     */
    void testObjectOutput() {
    	def dog = createDog()
        try {
        	// Log output
    		fireP4j.log(dog)
    	} catch (Exception e) {
    		// Fail on error
			fail "error writing to fireP4j: " + e.getMessage()
		}
    	
		// Standard protocol headers are asserted
    	assertProtocolHeaders(false)
    	// The message header is retrieved
		def messageHeader = mockResponse.getHeader("X-Wf-1-1-1-1").toString()
		
		// The occurence of the some properties is asserted
		assertTrue("occurence of class name on header ${messageHeader}", messageHeader.contains("\"__className\":\"firep4j.objects.Dog\""))
		assertTrue("occurence of name property on header ${messageHeader}", messageHeader.contains("\"private:name\":\"Snoopy\""))
		assertTrue("occurence of race property on header ${messageHeader}", messageHeader.contains("\"public:race\":\"Beagle\""))
		assertTrue("occurence of tag ID property on header ${messageHeader}", messageHeader.contains("\"protected:tagId\":\"RFID_452163\""))
		assertTrue("occurence of most popular name property on header ${messageHeader}", messageHeader.contains("\"public:static:mostPopularName\":\"Puppy\""))
		
		// The header index is asserted
		assertMessageIndexHeader(1)
    }
    
    /**
     * This method tests depth filtering on an object that is logged to FirePHP.
     * (File properties are not set, the title is not set)
     */
    void testDepthRestrictions() {
    	def person = createPerson()
        
        try {
        	// File properties are deactivated
        	fireP4j.setOptions(100, 1, false)
        	// Log output without file properties
    		fireP4j.log(person)
    	} catch (Exception e) {
    		// Fail on error
			fail "error writing to fireP4j: " + e.getMessage()
		}
    	
		// Standard protocol headers are asserted
    	assertProtocolHeaders(false)
    	
    	// The message header is retrieved and asserted
		def messageHeader = mockResponse.getHeader("X-Wf-1-1-1-1").toString()
		
    	// The desired pattern is matched with the header
    	assertTrue("regex test on header ${messageHeader}", messageHeader.matches(pattern))
    	// The loglevel is tested
		assertTrue("correct type on header ${messageHeader}", messageHeader.contains("\"Type\":\"" + FireP4j.FireP4jLogLevel.LOG.toString() + "\""))
		// The occurence of the caller class is asserted
		assertTrue("occurence of the max depth restriction string on header ${messageHeader}", messageHeader.contains("\"** Max Depth (1) **\""))
		// The occurence of the class attribute is asserted
		assertTrue("occurence of the max depth restriction string on header ${messageHeader}", messageHeader.contains("\"__className\":\"firep4j.objects.Person\""))
		
		// The header index is asserted
		assertMessageIndexHeader(1)	
    }
    
    /**
     * This method tests object filtering on an object that is logged to FirePHP.
     * (File properties are not set, the title is not set)
     */
    void testObjectFiltering() {
    	def person = createPerson()
        
        try {
        	// Some fields are filtered
        	fireP4j.addObjectFilter("firep4j.objects.Person", "firstName", "lastName")
        	// Log output without file properties
        	fireP4j.log(person)
    	} catch (Exception e) {
    		// Fail on error
			fail "error writing to fireP4j: " + e.getMessage()
		}
    	
		// Standard protocol headers are asserted
    	assertProtocolHeaders(false)
    	
    	// The message header is retrieved and asserted
		def messageHeader = mockResponse.getHeader("X-Wf-1-1-1-1").toString()
    	// The desired pattern is matched with the header
    	assertTrue("regex test on header ${messageHeader}", messageHeader.matches(pattern))
    	// The log level is tested
		assertTrue("correct type on header ${messageHeader}", messageHeader.contains("\"Type\":\"" + FireP4j.FireP4jLogLevel.LOG.toString() + "\""))
		// The occurence of firstName and lastName is prohibited
		assertTrue("no occurence of firstName on header ${messageHeader}", !messageHeader.contains("firstName") && !messageHeader.contains(person.firstName))
		assertTrue("no occurence of lastName on header ${messageHeader}", !messageHeader.contains("lastName") && !messageHeader.contains(person.lastName))
		// The occurence of the class attributes is asserted
		assertTrue("occurence of the max depth restriction string on header ${messageHeader}", messageHeader.contains("\"__className\":\"firep4j.objects.House\""))
		assertTrue("occurence of the max depth restriction string on header ${messageHeader}", messageHeader.contains("\"__className\":\"firep4j.objects.Person\""))
		
		// The header index is asserted
		assertMessageIndexHeader(1)	
    }
    
    /**
     * This method tests the FireP4j enabling and disabling functionality.
     */
    void testEnablingAndDisabling() {
        try {
        	// FireP4j is disabled
        	fireP4j.enabled = false
        	// Log output
        	fireP4j.log("should not be output")
        	
			// FireP4j is enabled again
			fireP4j.enabled = true
        	// Log output
        	fireP4j.info("should be output")
    	} catch (Exception e) {
    		// Fail on error
			fail "error writing to fireP4j: " + e.getMessage()
		} finally {
			// FireP4j is enabled again
			fireP4j.enabled = true
		}
		
		// Standard protocol headers are asserted
    	assertProtocolHeaders(false)
    	
    	// Only the second log operation should have been executed, so standard properties are asserted for this operation
    	
    	// The message header is retrieved and asserted
		def messageHeader = mockResponse.getHeader("X-Wf-1-1-1-1").toString()
    	assertStandardProperties(messageHeader, FireP4j.FireP4jLogLevel.INFO, null, "should be output")

		// The header index is asserted
		assertMessageIndexHeader(1)	
    }
    
    /**
     * This method tests setting multiple options for locks.
     */
    void testLocks() {
    	try {
    		// Multitple options are set (causing lock failures if there are any)
	    	fireP4j.maxDepth = 1
	    	fireP4j.maxTraceCount = 10
	    	fireP4j.setOptions(1, 10, true)
	    	
	    	fireP4j.addObjectFilter("foo", "b", "a", "r")
	    	fireP4j.addTraceFilter(Filter.Criteria.STARTS_WITH, "foo.bar")
	    	
	    	fireP4j.removeObjectFilter("foo", "b", "a", "r")
	    	fireP4j.removeTraceFilter(Filter.Criteria.STARTS_WITH, "foo.bar")
	    	
    		// Multitple exception handlers are registered or unregistered  (causing lock failures if there are any)
	    	fireP4j.registerExceptionHandler()
	    	fireP4j.registerErrorHandler()
	    	
	    	fireP4j.unregisterErrorHandler()
	    	fireP4j.unregisterExceptionHandler()
	    	fireP4j.unregisterExceptionHandler()	    	
	    	
	    	// Multiple outputs are done (causing to lock failures if there are any)
	    	fireP4j.log("foobar")
	    	fireP4j.log("foobar")
    	} catch (Exception e) {
    		fail("Exception while setting FireP4j options: ${e.getMessage()}")
    	}
    }
    
    /**
     * This method tests a different FireP4j profile.
     * (File properties are set, the title is not set)
     */
    void testProfile() {
    	def dog = createDog()
    	
    	// The current FireP4j profile is saved
    	FireP4jProfile backupProfile = profile
    	
        try {
        	// A new FireP4j profile is set
        	profile = new TestProfile()
        	initFireP4j(true)

        	// Log output without a title
        	fireP4j.log(dog)
    	} catch (Exception e) {
    		// Fail on error
			fail "error writing to fireP4j: " + e.getMessage()
		} finally {
			// The FireP4j profile is restored
			profile = backupProfile
        	initFireP4j(true)
		} 	
    	
		// Standard protocol headers are asserted
    	assertProtocolHeaders(false)
    	// The message header is retrieved
		def messageHeader = mockResponse.getHeader("X-Wf-1-1-1-1").toString()
		
		// Some properties of the message header are asserted
		assertTrue("no occurence of race on header ${messageHeader}", !messageHeader.contains("race") && !messageHeader.contains(dog.race))
		assertTrue("no occurence of mostPopularName on header ${messageHeader}", !messageHeader.contains("mostPopularName") && !messageHeader.contains(dog.mostPopularName))
		assertTrue("occurence of name on header ${messageHeader}", messageHeader.contains("name"))
		assertTrue("occurence of tag ID on header ${messageHeader}", messageHeader.contains("tagId"))
		assertTrue("occurence of class name on header ${messageHeader}", messageHeader.contains("__className"))

		// The header index is asserted
		assertMessageIndexHeader(1)
    }
    
    /**
     * This method tests the ability of FireP4j to do global exception handling.
     * Exception handling cannot be tested itself (JUnit catches all exceptions beforehand).
     * (File properties are set)
     */
    void testExceptionHandler() {
        try {
        	// The exception handler is registered
        	fireP4j.registerExceptionHandler()
        } catch (Exception e) {
    		// Fail on error
			fail "Error while registering exception handler: " + e.getMessage()
		}
        
        // The exception handler is asserted (it should be set) - NOT POSSIBLE BY NOW
        //String exceptionHandler =  Thread.getDefaultUncaughtExceptionHandler().toString()
        //assertTrue("exception handler set", exceptionHandler.contains("firep4j.FireP4jExceptionHandler"))
        
        try {
        	// The exception handler is unregistered again
        	fireP4j.unregisterExceptionHandler()
        } catch (Exception e) {
    		// Fail on error
			fail "Error while unregistering exception handler: " + e.getMessage()
		}
        
        /*

		// NOT POSSIBLE BY NOW

        // An intentional exception is thrown
        throw new Exception("intentional exception")
        
		// The message header is retrieved
		String messageHeader = mockResponse.getHeader("X-Wf-1-1-1-1").toString()
    	// The log level is tested
		assertTrue("correct type on header ${messageHeader}", messageHeader.contains("\"Type\":\"${FireP4j.FireP4jLogLevel.EXCEPTION}\""))
		// The occurence of the some classes, methods, ... is asserted
		assertTrue("caller on header ${messageHeader}", messageHeader.contains("\"File\":\"${fileName}\""))
		assertTrue("occurence of the exception class on header ${messageHeader}", messageHeader.contains("\"Class\":\"Exception\""))
		assertTrue("occurence of the testException() function on header ${messageHeader}", messageHeader.contains("\"Function\":\"testExceptionHandler\""))
		assertTrue("occurence of the exception message on header ${messageHeader}", messageHeader.contains("\"Message\":\"intentional exception\""))
		assertTrue("occurence of an empty stack trace on header ${messageHeader}", messageHeader.contains("\"Args\":[]") && messageHeader.contains("\"Trace\":[]"))
		assertTrue("correct number of stack trace elements (1) on header ${messageHeader}", messageHeader.replaceAll("[^{]","").length() == 2)
		assertTrue("no occurence of the label within the exception message", !messageHeader.contains("\"Label\":\"exception message\""))
    	
		// The header index is asserted
		assertMessageIndexHeader(1)  
		*/      
        
        // The exception handler is asserted (it should not be set anymore) - NOT POSSIBE BY NOW
        //exceptionHandler =  Thread.getDefaultUncaughtExceptionHandler().toString()
        //assertTrue("exception handler unset", exceptionHandler.equals("null"))
    }
    
    /**
     * This method tests whether a new FireP4j instance is created on different HTTP objects.
     */
    void testRequestScope() {
    	// The current instance number is retrieved
    	int currentCount = fireP4j.instanceCount
    	
    	// A local FireP4j instance is created using different HTTP object mocks
    	HttpServletRequestMock localMockRequest = new HttpServletRequestMock()
    	HttpServletResponseMock localMockResponse = new HttpServletResponseMock()
		FireP4j localFireP4j = FireP4j.getInstance(localMockRequest, localMockResponse)
		
    	// The instance number of the new instance is retrieved
		int localCount1 = localFireP4j.instanceCount
		
		// Another FireP4j instance is created using the same HTTP object mocks as the last init
		localFireP4j = FireP4j.getInstance(localMockRequest, localMockResponse)
		
    	// The instance number of the new instance is retrieved
		int localCount2 = localFireP4j.instanceCount
		
		// The instance numbers are asserted (the first local number must equal the old number + 1)
		assertTrue("new FireP4j instance created for local mocks", localCount1 == currentCount + 1)
		// The instance numbers are asserted (the first local number must equal the second local number)
		assertTrue("local instances stay the same", localCount1 == localCount2)
    }
    
    /**
     * This method creates a standard person object.
     * 
     * @return Person object
     */
    private Person createPerson() {
    	// Test objects are created
        def house = new House("123 Fake St.")
    	def person = new Person("Bart", "Simpson", house)
        person
    }
    
    /**
     * This method creates a standard dog object.
     * 
     * @return Dog object
     */
    private Dog createDog() {
    	// Test objects are created
    	Dog dog = new Dog("Snoopy", "Beagle", "RFID_452163")
    	dog
    }
    
    /**
     * This method asserts that all protocol headers are set correctly.
     * 
     * @param dump Flag indicating that the dump log level was used
     */
    private void assertProtocolHeaders(boolean dump) {
    	def header = ""
    	
    	// Protocol header
    	header = mockResponse.getHeader("X-Wf-Protocol-1")    	
    	assertEquals("JSON header", "http://meta.wildfirehq.org/Protocol/JsonStream/0.2", header)
    	
    	// Plugin header
    	header = mockResponse.getHeader("X-Wf-1-Plugin-1")    	
    	assertEquals("FireP4j header", "http://meta.firephp.org/Wildfire/Plugin/FirePHP/Library-FirePHPCore/" + FireP4j.version, header)
    	
    	// Console header
    	if (!dump) {
    		// If current loglevel isn't dump
    		header = mockResponse.getHeader("X-Wf-1-Structure-1")    	
    		assertEquals("FireBug header", "http://meta.firephp.org/Wildfire/Structure/FirePHP/FirebugConsole/0.1", header)
    	} else {
    		// If current loglevel is dump
    		header = mockResponse.getHeader("X-Wf-1-Structure-2")    	
    		assertEquals("FireBug dump header", "http://meta.firephp.org/Wildfire/Structure/FirePHP/Dump/0.1", header)
    	}
    }
    
    /**
     * This method asserts standard (string) properties for the header set
     * 
     * @param header HTTP Header string
     * @param logLevel Loglevel used
     * @param label Label set (null if no label was set)
     * @param content Content set (null if no content was set)
     * @param assertFileProperties Flag indicating whether file properties should be tested
     */
    private void assertStandardProperties(String header, FireP4j.FireP4jLogLevel logLevel, String label = null, String content = null, boolean assertFileProperties = true, boolean assertMessageLength = true) {
    	// The desired pattern is matched with the header
    	assertTrue("regex test on header ${header}", header.matches(pattern))
    	
    	// If a label was set, it is tested
    	if (label != null) {
    		assertTrue("correct label on header ${header}", header.contains("\"Label\":\"${label}\""));
    	}
    	
    	// The header content is tested
    	if (content != null) {
    		// The content was set
    		assertTrue("correct output on header ${header}", header.endsWith(",\"${content}\"]|"));
    	} else {
    		// The content was not set
    		assertTrue("correct output on header ${header}", header.endsWith(",null]|"));
    	}
		
    	// The loglevel is tested
		assertTrue("correct type on header ${header}", header.contains("\"Type\":\"${logLevel.toString()}\""));
    	
		// File properties are tested
    	if (assertFileProperties) {
    		// File properties were set
    		assertTrue("correct file name on header ${header}", header.contains("\"File\":\"${fileName}\""))
    		
    		Pattern linePattern = Pattern.compile("\\\"Line\\\":[0-9]+(,|})");
    		Matcher lineMatcher = linePattern.matcher(header);
    		
    		assertTrue("line occurence on header ${header}", lineMatcher.find(0))
    	} else {
    		// File properties were not set
    		assertFalse("file name not present on header ${header}", header.contains("\"File\":"));
    		assertFalse("line not present on header ${header}", header.contains("\"Line\":"));
    	}
    	
    	// The header body payload is extracted
    	String[] parts = header.split(/\|/);
    	
    	// The message length is asserted using the header body payload
    	if (assertMessageLength) {
	    	int length = parts[0] as int
	    	String payload = parts[1]
	    	               
	    	assertTrue("message length on header ${header}", length == payload.size())
    	}
    }
    
    /**
     * This method asserts that the message index header contains the right index.
     * 
     * @param index Index to be checked
     */
    private void assertMessageIndexHeader(def index) {
    	// The message index header is asserted
		def messageIndexHeader = mockResponse.getHeader("X-Wf-1-Index").toString()
		assertEquals("message index header", index.toString(), messageIndexHeader)
    }
}