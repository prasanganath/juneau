// ***************************************************************************************************************************
// * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements.  See the NOTICE file *
// * distributed with this work for additional information regarding copyright ownership.  The ASF licenses this file        *
// * to you under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance            *
// * with the License.  You may obtain a copy of the License at                                                              *
// *                                                                                                                         *
// *  http://www.apache.org/licenses/LICENSE-2.0                                                                             *
// *                                                                                                                         *
// * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an  *
// * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the        *
// * specific language governing permissions and limitations under the License.                                              *
// ***************************************************************************************************************************
package org.apache.juneau.rest;

import static org.apache.juneau.internal.StringUtils.*;

import java.lang.reflect.*;
import java.util.*;

import org.apache.juneau.*;
import org.apache.juneau.httppart.*;
import org.apache.juneau.parser.*;

/**
 * @deprecated Unused.
 */
@Deprecated
@SuppressWarnings("unchecked")
public class RequestPathMatch extends TreeMap<String,String> {
	private static final long serialVersionUID = 1L;

	private HttpPartParser parser;
	private BeanSession beanSession;
	private String remainder, pattern;

	RequestPathMatch() {
		super(String.CASE_INSENSITIVE_ORDER);
	}

	RequestPathMatch parser(HttpPartParser parser) {
		this.parser = parser;
		return this;
	}

	RequestPathMatch beanSession(BeanSession beanSession) {
		this.beanSession = beanSession;
		return this;
	}

	RequestPathMatch remainder(String remainder) {
		this.remainder = remainder;
		return this;
	}

	RequestPathMatch pattern(String pattern) {
		this.pattern = pattern;
		return this;
	}

	/**
	 * Sets a request query parameter value.
	 *
	 * @param name The parameter name.
	 * @param value The parameter value.
	 */
	public void put(String name, Object value) {
		super.put(name, value.toString());
	}

	/**
	 * Returns the specified path parameter converted to a String.
	 *
	 * @param name The path variable name.
	 * @return The parameter value.
	 * @throws ParseException
	 */
	public String getString(String name) throws ParseException {
		return parse(parser, name, beanSession.string());
	}

	/**
	 * Returns the specified path parameter converted to an integer.
	 *
	 * @param name The path variable name.
	 * @return The parameter value.
	 * @throws ParseException
	 */
	public int getInt(String name) throws ParseException {
		return parse(parser, name, beanSession.getClassMeta(int.class));
	}

	/**
	 * Returns the specified path parameter converted to a boolean.
	 *
	 * @param name The path variable name.
	 * @return The parameter value.
	 * @throws ParseException
	 */
	public boolean getBoolean(String name) throws ParseException {
		return parse(parser, name, beanSession.getClassMeta(boolean.class));
	}

	/**
	 * Returns the specified path parameter value converted to a POJO using the {@link HttpPartParser} registered with the resource.
	 *
	 * <h5 class='section'>Examples:</h5>
	 * <p class='bcode'>
	 * 	<jc>// Parse into an integer.</jc>
	 * 	<jk>int</jk> myparam = path.get(<js>"myparam"</js>, <jk>int</jk>.<jk>class</jk>);
	 *
	 * 	<jc>// Parse into an int array.</jc>
	 * 	<jk>int</jk>[] myparam = path.get(<js>"myparam"</js>, <jk>int</jk>[].<jk>class</jk>);

	 * 	<jc>// Parse into a bean.</jc>
	 * 	MyBean myparam = path.get(<js>"myparam"</js>, MyBean.<jk>class</jk>);
	 *
	 * 	<jc>// Parse into a linked-list of objects.</jc>
	 * 	List myparam = path.get(<js>"myparam"</js>, LinkedList.<jk>class</jk>);
	 *
	 * 	<jc>// Parse into a map of object keys/values.</jc>
	 * 	Map myparam = path.get(<js>"myparam"</js>, TreeMap.<jk>class</jk>);
	 * </p>
	 *
	 * <h5 class='section'>See Also:</h5>
	 * <ul>
	 * 	<li class='jf'>{@link RestContext#REST_partParser}
	 * </ul>
	 *
	 * @param name The attribute name.
	 * @param type The class type to convert the attribute value to.
	 * @param <T> The class type to convert the attribute value to.
	 * @return The attribute value converted to the specified class type.
	 * @throws ParseException
	 */
	public <T> T get(String name, Class<T> type) throws ParseException {
		return get(parser, name, type);
	}

	/**
	 * Same as {@link #get(String, Class)} but allows you to override the part parser.
	 *
	 * @param parser
	 * 	The parser to use for parsing the string value.
	 * 	<br>If <jk>null</jk>, uses the part parser defined on the resource/method.
	 * @param name The attribute name.
	 * @param type The class type to convert the attribute value to.
	 * @param <T> The class type to convert the attribute value to.
	 * @return The attribute value converted to the specified class type.
	 * @throws ParseException
	 */
	public <T> T get(HttpPartParser parser, String name, Class<T> type) throws ParseException {
		return parse(parser, name, beanSession.getClassMeta(type));
	}

	/**
	 * Returns the specified query parameter value converted to a POJO using the {@link HttpPartParser} registered with the resource.
	 *
	 * <p>
	 * Similar to {@link #get(String,Class)} but allows for complex collections of POJOs to be created.
	 *
	 * <p>
	 * Use this method if you want to parse into a parameterized <code>Map</code>/<code>Collection</code> object.
	 *
	 * <h5 class='section'>Examples:</h5>
	 * <p class='bcode'>
	 * 	<jc>// Parse into a linked-list of strings.</jc>
	 * 	List&lt;String&gt; myparam = req.getPathParameter(<js>"myparam"</js>, LinkedList.<jk>class</jk>, String.<jk>class</jk>);
	 *
	 * 	<jc>// Parse into a linked-list of linked-lists of strings.</jc>
	 * 	List&lt;List&lt;String&gt;&gt; myparam = req.getPathParameter(<js>"myparam"</js>, LinkedList.<jk>class</jk>, LinkedList.<jk>class</jk>, String.<jk>class</jk>);
	 *
	 * 	<jc>// Parse into a map of string keys/values.</jc>
	 * 	Map&lt;String,String&gt; myparam = req.getPathParameter(<js>"myparam"</js>, TreeMap.<jk>class</jk>, String.<jk>class</jk>, String.<jk>class</jk>);
	 *
	 * 	<jc>// Parse into a map containing string keys and values of lists containing beans.</jc>
	 * 	Map&lt;String,List&lt;MyBean&gt;&gt; myparam = req.getPathParameter(<js>"myparam"</js>, TreeMap.<jk>class</jk>, String.<jk>class</jk>, List.<jk>class</jk>, MyBean.<jk>class</jk>);
	 * </p>
	 *
	 * <h5 class='section'>Notes:</h5>
	 * <ul class='spaced-list'>
	 * 	<li>
	 * 		<code>Collections</code> must be followed by zero or one parameter representing the value type.
	 * 	<li>
	 * 		<code>Maps</code> must be followed by zero or two parameters representing the key and value types.
	 * </ul>
	 *
	 * <h5 class='section'>See Also:</h5>
	 * <ul>
	 * 	<li class='jf'>{@link RestContext#REST_partParser}
	 * </ul>
	 *
	 * @param name The attribute name.
	 * @param type
	 * 	The type of object to create.
	 * 	<br>Can be any of the following: {@link ClassMeta}, {@link Class}, {@link ParameterizedType}, {@link GenericArrayType}
	 * @param args
	 * 	The type arguments of the class if it's a collection or map.
	 * 	<br>Can be any of the following: {@link ClassMeta}, {@link Class}, {@link ParameterizedType}, {@link GenericArrayType}
	 * 	<br>Ignored if the main type is not a map or collection.
	 * @param <T> The class type to convert the attribute value to.
	 * @return The attribute value converted to the specified class type.
	 * @throws ParseException
	 */
	public <T> T get(String name, Type type, Type...args) throws ParseException {
		return get(parser, name, type, args);
	}

	/**
	 * Same as {@link #get(String, Type, Type...)} but allows you to override the part parser.
	 *
	 * @param parser
	 * 	The parser to use for parsing the string value.
	 * 	<br>If <jk>null</jk>, uses the part parser defined on the resource/method.
	 * @param name The attribute name.
	 * @param type
	 * 	The type of object to create.
	 * 	<br>Can be any of the following: {@link ClassMeta}, {@link Class}, {@link ParameterizedType}, {@link GenericArrayType}
	 * @param args
	 * 	The type arguments of the class if it's a collection or map.
	 * 	<br>Can be any of the following: {@link ClassMeta}, {@link Class}, {@link ParameterizedType}, {@link GenericArrayType}
	 * 	<br>Ignored if the main type is not a map or collection.
	 * @param <T> The class type to convert the attribute value to.
	 * @return The attribute value converted to the specified class type.
	 * @throws ParseException
	 */
	public <T> T get(HttpPartParser parser, String name, Type type, Type...args) throws ParseException {
		return (T)parse(parser, name, beanSession.getClassMeta(type, args));
	}


	/* Workhorse method */
	<T> T parse(HttpPartParser parser, String name, ClassMeta<T> cm) throws ParseException {
		if (parser == null)
			parser = this.parser;
		Object attr = get(name);
		T t = null;
		if (attr != null)
			t = parser.parse(HttpPartType.PATH, attr.toString(), cm);
		if (t == null && cm.isPrimitive())
			return cm.getPrimitiveDefault();
		return t;
	}

	/**
	 * Returns the decoded remainder of the URL following any path pattern matches.
	 *
	 * <p>
	 * The behavior of path remainder is shown below given the path pattern "/foo/*":
	 * <table class='styled'>
	 * 	<tr>
	 * 		<th>URL</th>
	 * 		<th>Path Remainder</th>
	 * 	</tr>
	 * 	<tr>
	 * 		<td><code>/foo</code></td>
	 * 		<td><jk>null</jk></td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td><code>/foo/</code></td>
	 * 		<td><js>""</js></td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td><code>/foo//</code></td>
	 * 		<td><js>"/"</js></td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td><code>/foo///</code></td>
	 * 		<td><js>"//"</js></td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td><code>/foo/a/b</code></td>
	 * 		<td><js>"a/b"</js></td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td><code>/foo//a/b/</code></td>
	 * 		<td><js>"/a/b/"</js></td>
	 * 	</tr>
	 * 	<tr>
	 * 		<td><code>/foo/a%2Fb</code></td>
	 * 		<td><js>"a/b"</js></td>
	 * 	</tr>
	 * </table>
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode'>
	 * 	<jc>// REST method</jc>
	 * 	<ja>@RestMethod</ja>(name=<jsf>GET</jsf>,path=<js>"/foo/{bar}/*"</js>)
	 * 	<jk>public</jk> String doGetById(RequestPathMatch path, <jk>int</jk> bar) {
	 * 		<jk>return</jk> path.getRemainder();
	 * 	}
	 * </p>
	 *
	 * @return The path remainder string.
	 */
	public String getRemainder() {
		return urlDecode(remainder);
	}

	/**
	 * Same as {@link #getRemainder()} but doesn't decode characters.
	 *
	 * @return The un-decoded path remainder.
	 */
	public String getRemainderUndecoded() {
		return remainder;
	}

	/**
	 * Returns the path pattern that matched this request.
	 *
	 * @return The path pattern that matched this request.
	 */
	public String getPattern() {
		return pattern;
	}
}
