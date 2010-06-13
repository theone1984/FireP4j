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
package servlet.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.HashMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * This class serves as a HTTP Servlet response mock for testing the response object.
 * 
 * @author Thomas Endres
 */
public class HttpServletResponseMock implements HttpServletResponse {
	/**
	 * Header HashMap
	 */
	private HashMap<String, String> headerHashMap = null
	
	/**
	 * Dummy Constructor
	 */
	public HttpServletResponseMock() {
		this.headerHashMap = new HashMap<String, String>()
	}
	
	/**
	 * This method clears the servlet response settings made.
	 */
	public void clear() {
		// The headers are cleared
		this.headerHashMap.clear()
	}
	
	/**
	 * Not implemented method
	 */
	@Override
	public void addCookie(Cookie arg0) { }

	/**
	 * Not implemented method
	 */
	@Override
	public void addDateHeader(String arg0, long arg1) { }

	/**
	 * Not implemented method
	 */
	@Override
	public void addHeader(String arg0, String arg1) { }

	/**
	 * Not implemented method
	 */
	@Override
	public void addIntHeader(String arg0, int arg1) { }

	/**
	 * Not implemented method
	 */
	@Override
	public boolean containsHeader(String arg0) { false }

	/**
	 * Not implemented method
	 */
	@Override
	public String encodeRedirectURL(String arg0) { null }

	/**
	 * Not implemented method
	 */
	@Override
	public String encodeRedirectUrl(String arg0) { null }

	/**
	 * Not implemented method
	 */
	@Override
	public String encodeURL(String arg0) { null }

	/**
	 * Not implemented method
	 */
	@Override
	public String encodeUrl(String arg0) { null }

	/**
	 * Not implemented method
	 */
	@Override
	public void sendError(int arg0) throws IOException { }

	/**
	 * Not implemented method
	 */
	@Override
	public void sendError(int arg0, String arg1) throws IOException { }

	/**
	 * Not implemented method
	 */
	@Override
	public void sendRedirect(String arg0) throws IOException { }

	/**
	 * Not implemented method
	 */
	@Override
	public void setDateHeader(String arg0, long arg1) { }

	/**
	 * This method sets a specific header to the given value.
	 * 
	 * @param arg0 Header name
	 * @param arg1 Header value
	 */
	@Override
	public void setHeader(String arg0, String arg1) {
		this.headerHashMap[arg0] = arg1;
	}
	
	/**
	 * This method returns the header value for a specific header.
	 * 
	 * @param arg0 Header name
	 * @return Header value
	 */
	public String getHeader(String arg0) {
		if (this.headerHashMap.containsKey(arg0)) {
			return this.headerHashMap[arg0]
		} else {
			return null;
		}
	}

	/**
	 * Not implemented method
	 */
	@Override
	public void setIntHeader(String arg0, int arg1) { }

	/**
	 * Not implemented method
	 */
	@Override
	public void setStatus(int arg0) { }

	/**
	 * Not implemented method
	 */
	@Override
	public void setStatus(int arg0, String arg1) { }

	/**
	 * Not implemented method
	 */
	@Override
	public void flushBuffer() throws IOException { }

	/**
	 * Not implemented method
	 */
	@Override
	public int getBufferSize() { 0 }

	/**
	 * Not implemented method
	 */
	@Override
	public String getCharacterEncoding() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public String getContentType() { null }

	/**
	 * Not implemented method
	 */
	@Override
	public Locale getLocale() {	null }

	/**
	 * Not implemented method
	 */
	@Override
	public ServletOutputStream getOutputStream() throws IOException { null	}

	/**
	 * Not implemented method
	 */
	@Override
	public PrintWriter getWriter() throws IOException {	null }

	/**
	 * Not implemented method
	 */
	@Override
	public boolean isCommitted() { false }

	/**
	 * Not implemented method
	 */
	@Override
	public void reset() { }

	/**
	 * Not implemented method
	 */
	@Override
	public void resetBuffer() {	}

	/**
	 * Not implemented method
	 */
	@Override
	public void setBufferSize(int arg0) { }

	/**
	 * Not implemented method
	 */
	@Override
	public void setCharacterEncoding(String arg0) { }

	/**
	 * Not implemented method
	 */
	@Override
	public void setContentLength(int arg0) { }

	/**
	 * Not implemented method
	 */
	@Override
	public void setContentType(String arg0) { }

	/**
	 * Not implemented method
	 */
	@Override
	public void setLocale(Locale arg0) { }

}