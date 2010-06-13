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

import junit.framework.TestCase;
import flexjson.filters.PathExpression;
import flexjson.tools.Path;

/**
 * This class tests FlexJSON path expression matching.
 * 
 * @author Thomas Endres
 */
public class PathExpressionTests extends TestCase {
	/**
	 * This method tests various path expression matchings.
	 */
    public void testExpressionMatching() {
    	// Various path expressions are created and matched
        assertTrue("'hello' matches 'hello'", new PathExpression("hello",true).matches(new Path("hello")));
        assertFalse("'hello' doesn't match 'noob'", new PathExpression("hello",true).matches(new Path("noob")));
        assertTrue("'hello.world' matches 'hello,world",new PathExpression("hello.world",true).matches(new Path("hello", "world")));
        assertFalse("'hello' does not match empty path", new PathExpression("hello",true).matches(new Path()));
        assertTrue("'hello.*.world' does match 'hello,cat,world'", new PathExpression("hello.*.world",true).matches(new Path("hello","cat","world")));
        assertTrue("'hello.*.world' does match 'hello, cat, dog, world'", new PathExpression("hello.*.world",true).matches(new Path("hello","cat","dog", "world")));
        assertTrue("'*.class' matches 'cat, class'", new PathExpression("*.class",true).matches(new Path("cat", "class")));
        assertTrue("'*.class' matches 'cat, dog, sheep, class'", new PathExpression("*.class",true).matches(new Path("cat", "dog", "sheep", "class")));
        assertFalse("'*.class' does not match 'cat, dog, sheep, diggums'", new PathExpression("*.class",true).matches(new Path("cat", "dog", "sheep", "diggums")));
        assertTrue("'*' matches 'cat, dog, sheep, cow'", new PathExpression("*",true).matches(new Path("cat", "dog", "sheep", "cow")));
        assertTrue("'*.class.*' matches 'billy.bong.class.yeker'", new PathExpression("*.class.*",true).matches(new Path("billy", "bong", "class", "yeker")));
        assertTrue("'*' will match anything.", new PathExpression("*",true).matches(new Path("123", "8923", "fuggly", "buggly")));
        assertTrue("'*.*' matches 'billy.bong.class.yeker'", new PathExpression("*.*",true).matches(new Path("billy", "bong", "class", "yeker")));
    }
}
