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
package servlet.http

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

class HttpServletRequestMock implements HttpServletRequest {
	/**
	 * Header HashMap
	 */
	private HashMap<String, String> headerHashMap = null
	
	/**
	 * Attribute HashMap
	 */
	private HashMap<String, String> attributeHashMap = null
	
	/**
	 * Dummy Constructor
	 */
	public HttpServletRequestMock() {
		// Class variables are initialized
		headerHashMap = new HashMap<String, String>()
		attributeHashMap = new HashMap<String, String>()
	}
	
	/**
	 * This method clears the servlet response settings made.
	 */
	public void clear() {
		// The headers are cleared
		this.headerHashMap.clear()
		this.attributeHashMap.clear()
	}
	
	/**
	 * This method returns the header value for a specific header.
	 * 
	 * @param arg0 Header name
	 * @return Header value
	 */
	@Override
	public String getHeader(String arg0) {
		if (headerHashMap.containsKey(arg0)) {
			return headerHashMap[arg0]
		} else {
			return null;
		}
	}
	
	/**
	 * This method sets a specific header to the given value.
	 * 
	 * @param arg0 Header name
	 * @param arg1 Header value
	 */
	public void setHeader(String arg0, String arg1) {
		if (headerHashMap.containsKey(arg0)) {
			headerHashMap.remove(arg0)
		}		
		headerHashMap[arg0] = arg1
	}
	
	/**
	 * This method returns the attribute value for a specific attribute.
	 * 
	 * @param arg0 Attribute name
	 * @return Attribute value
	 */
	@Override
	public Object getAttribute(String arg0) { 
		if (attributeHashMap.containsKey(arg0)) {
			return attributeHashMap[arg0]
		} else {
			return null;
		}		
	}
	
	/**
	 * This method sets a specific attribute to the given value.
	 * 
	 * @param arg0 Attribute name
	 * @param arg1 Attribute value
	 */
	@Override
	public void setAttribute(String arg0, Object arg1) {
		removeAttribute(arg0)	
		attributeHashMap[arg0] = arg1
	}
	
	/**
	 * This method removes a specific attribute.
	 * 
	 * @param arg0 Attribute name
	 */
	@Override
	public void removeAttribute(String arg0) {
		if (attributeHashMap.containsKey(arg0)) {
			attributeHashMap.remove(arg0)
		}
	}
	
	/**
	 * Not implemented method
	 */
	@Override
	public String getAuthType() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public String getContextPath() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public Cookie[] getCookies() { null	}

	/**
	 * Not implemented method
	 */
	@Override
	public long getDateHeader(String arg0) { 0 }

	/**
	 * Not implemented method
	 */
	@Override
	public Enumeration getHeaderNames() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public Enumeration getHeaders(String arg0) { null }

	/**
	 * Not implemented method
	 */
	@Override
	public int getIntHeader(String arg0) { 0 }

	/**
	 * Not implemented method
	 */
	@Override
	public String getMethod() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public String getPathInfo() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public String getPathTranslated() {	null }

	/**
	 * Not implemented method
	 */
	@Override
	public String getQueryString() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public String getRemoteUser() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public String getRequestURI() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public StringBuffer getRequestURL() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public String getRequestedSessionId() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public String getServletPath() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public HttpSession getSession() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public HttpSession getSession(boolean arg0) { null }

	/**
	 * Not implemented method
	 */
	@Override
	public Principal getUserPrincipal() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public boolean isRequestedSessionIdFromCookie() { false }

	/**
	 * Not implemented method
	 */
	@Override
	public boolean isRequestedSessionIdFromURL() { false }

	/**
	 * Not implemented method
	 */
	@Override
	public boolean isRequestedSessionIdFromUrl() { false }

	/**
	 * Not implemented method
	 */
	@Override
	public boolean isRequestedSessionIdValid() { false }

	/**
	 * Not implemented method
	 */
	@Override
	public boolean isUserInRole(String arg0) { false }

	/**
	 * Not implemented method
	 */
	@Override
	public Enumeration getAttributeNames() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public String getCharacterEncoding() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public int getContentLength() { 0 }

	/**
	 * Not implemented method
	 */
	@Override
	public String getContentType() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public ServletInputStream getInputStream() throws IOException { null }

	/**
	 * Not implemented method
	 */
	@Override
	public String getLocalAddr() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public String getLocalName() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public int getLocalPort() { 0 }

	/**
	 * Not implemented method
	 */
	@Override
	public Locale getLocale() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public Enumeration getLocales() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public String getParameter(String arg0) { null }

	/**
	 * Not implemented method
	 */
	@Override
	public Map getParameterMap() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public Enumeration getParameterNames() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public String[] getParameterValues(String arg0) { null }

	/**
	 * Not implemented method
	 */
	@Override
	public String getProtocol() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public BufferedReader getReader() throws IOException { null }

	/**
	 * Not implemented method
	 */
	@Override
	public String getRealPath(String arg0) { null }

	/**
	 * Not implemented method
	 */
	@Override
	public String getRemoteAddr() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public String getRemoteHost() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public int getRemotePort() { 0 }

	/**
	 * Not implemented method
	 */
	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) { null }

	/**
	 * Not implemented method
	 */
	@Override
	public String getScheme() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public String getServerName() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public int getServerPort() { 0 }

	/**
	 * Not implemented method
	 */
	@Override
	public boolean isSecure() { false }

	/**
	 * Not implemented method
	 */
	@Override
	public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException  { }
}
