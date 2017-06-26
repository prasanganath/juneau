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
package org.apache.juneau.dto.html5;

import org.apache.juneau.annotation.*;

/**
 * DTO for an HTML <a class="doclink"
 * href="https://www.w3.org/TR/html5/tabular-data.html#the-col-element">&lt;col&gt;</a> element.
 * <p>
 *
 * <h6 class='topic'>Additional Information</h6>
 * <ul class='doctree'>
 * 	<li class='link'><a class='doclink' href='../../../../../overview-summary.html#DTOs'>Juneau Data Transfer Objects
 * 		(org.apache.juneau.dto)</a>
 * 	<ul>
 * 		<li class='sublink'><a class='doclink' href='../../../../../overview-summary.html#DTOs.HTML5'>HTML5</a>
 * 	</ul>
 * </ul>
 */
@Bean(typeName="col")
public class Col extends HtmlElementVoid {

	/**
	 * <a class="doclink" href="https://www.w3.org/TR/html5/tabular-data.html#attr-col-span">span</a> attribute.
	 * Number of columns spanned by the element.
	 *
	 * @param span The new value for this attribute.
	 * Typically a {@link Number} or {@link String}.
	 * @return This object (for method chaining).
	 */
	public final Col span(Object span) {
		attr("span", span);
		return this;
	}


	//--------------------------------------------------------------------------------
	// Overridden methods
	//--------------------------------------------------------------------------------

	@Override /* HtmlElement */
	public final Col _class(String _class) {
		super._class(_class);
		return this;
	}

	@Override /* HtmlElement */
	public final Col id(String id) {
		super.id(id);
		return this;
	}

	@Override /* HtmlElement */
	public final Col style(String style) {
		super.style(style);
		return this;
	}
}
