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
package firep4j.tools


/**
 * This class provides static methods for comparing different version strings.
 */
class Versioner {
	/**
	 * This method compares to version strings.
	 * It works similar to the PHP version_compare function.
	 * 
	 * @see http://php.net/manual/de/function.version-compare.php
	 * @param version1 Version string 1
	 * @param version2 Version string 2
	 * @return 1 if version 1 is newer than version 2, -1 if version2 is newer, 0 if they have the same version number
	 */
	private static int compareVersions(String version1, String version2) {
		// If one of the version strings is null, an exception is thrown
		if (version1 == null || version2 == null) {
			throw new Exception("One of the version strings was not definde")
		}
	
		// The values are standardized and splitted (getting all version entries)
		String[] values1 = standardizeVersion(version1).split(/\./)
		String[] values2 = standardizeVersion(version2).split(/\./)
		
		// The max length of the version entries is determined
		int length = Math.max(values1.size() , values2.size())
		
		int versionValue1, versionValue2, compareValue = 0
		
		// For all the version entries
		for (int i = 0; i < length; i++) {
			// Version entry values are determined (not existing values correspond to "0"
			versionValue1 = mapVersionEntry(((i < values1.size()) ? values1[i] : "0"))
			versionValue2 = mapVersionEntry(((i < values2.size()) ? values2[i] : "0"))
			
			// The values are compared
			compareValue = versionValue1 - versionValue2
			
			// If the values differ, the difference sign is determined (and the loop is ended)
			if (compareValue != 0) {
				compareValue =  compareValue > 0 ? 1 : -1
				break;
			}
		}
		
		compareValue
	}

	/**
	 * This method maps one version string entry (separated by dots) to an integer value.
	 * Priorities (strings are case-sensitive):
	 * 0: String value "pl"
	 * 1: Any number
	 * 2: String "RC" for release candidate
	 * 3: String "beta" or "b" for beta test version
	 * 4: String "alpha" or "a" for alpha test version
	 * 5: String "dev" for development version
	 * 6: Any other string value
	 * 
	 * @see http://php.net/manual/de/function.version-compare.php
	 * @param entry Version string entry
	 * @return The integer representation of the version string entry
	 */
	private static int mapVersionEntry(String entry) {
		int value = 0
		
		if (entry == "dev") {
			// Dev version -> priority 5
			value = -4
		} else if (entry == "alpha" || entry == "a") {
			// Alpha test -> priority 4
			value = -3
		} else if (entry == "beta" || entry == "b") {
			// Beta test -> priority 3
			value = -2
		} else if (entry == "RC" || entry == "rc") {
			// Release candidate -> priority 2
			value = -1
		} else if (entry == "pl" || entry == "p") {
			// Post release -> priority 0
			value = Integer.MAX_VALUE
		} else if (entry.matches(/^[0-9]+?/)) {
			// Version -> priority 1
			value = entry as int
		} else {
			// Other -> priority 6
			value = -5
		}
		
		value	
	}
	
	/**
	 * This method standardizes a version string.<br>
	 * - All occurences of "-", "+", "_" and " " are transformed into dots<br>
	 * - Before and after each alpha string value, dots are inserted if there are none (so version 4.2RC1 becomes 4.2.RC.1)<br>
	 * - All occurences of ".." are replaced with ".0."<br>
	 * - Occurences of "." at the beginning or the end of the string are replaced with "0." or ".0"<br>
	 * - All characters other than alphanumeric characters and dots are removed
	 * 
	 * @see http://php.net/manual/de/function.version-compare.php
	 * @param version Version string
	 * @return Standardized version string
	 */
	private static String standardizeVersion(String version) {
		// All occurences of "-", "+", "_" and " " are transformed into dots
		String replacedVersion = version.replaceAll(/[-+_ ]/, ".")
		// All characters other than alphanumeric characters and dots are removed
		replacedVersion = replacedVersion.replaceAll(/[^0-9a-zA-Z.]/, "")
		String resultingVersion = ""
		
		boolean lastIsNumber = false
		boolean currentIsNumber = false
		
		boolean lastIsDot = true
		boolean currentIsDot = false
		

		// For all the characters in the version string
		for (int i = 0; i < replacedVersion.size(); i++) {
			// Flags are determined (is the current character a number, a dot, or an alpha character)
			currentIsNumber = replacedVersion[i].matches(/[0-9]/)
			currentIsDot = replacedVersion[i] == "."
			
			if (!lastIsDot && !currentIsDot && ((lastIsNumber && !currentIsNumber) || (!lastIsNumber && currentIsNumber))) {
				// Before and after each alpha string value, dots are inserted if there are none
				resultingVersion += "."
			} else if (lastIsDot && currentIsDot) {
				// All occurences of ".." are replaced with ".0."
				// Occurences of "." at the beginning of the string are replaced with "0."
				resultingVersion += "0"
			}
			
			// The current character is copied to the new version string
			resultingVersion += replacedVersion[i]
			           
			// Occurences of "." at the end of the string are replaced with ".0"
			if (currentIsDot && i == replacedVersion.size() - 1) {
				resultingVersion += "0"
			}                               
			
			// The flags for the last iteration are set
			lastIsNumber = currentIsNumber
			lastIsDot = currentIsDot
		}
		
		resultingVersion
	}
}
