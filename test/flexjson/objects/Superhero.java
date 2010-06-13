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
package flexjson.objects;

/**
 * This class represents a super hero and is used for testing FlexJSON.
 * 
 * @author Charlie Hubbard, Thomas Endres
 */
public abstract class Superhero {
	/**
	 * Hero's super power
	 */
    public String superpower;

    /**
     * Dummy constructor setting the super power
     * 
     * @param superpower Hero's super power
     */
    public Superhero(String superpower) {
        this.superpower = superpower;
    }
}
