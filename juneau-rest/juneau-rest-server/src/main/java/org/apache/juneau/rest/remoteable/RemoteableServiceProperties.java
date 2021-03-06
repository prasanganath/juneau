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
package org.apache.juneau.rest.remoteable;

import org.apache.juneau.remoteable.*;

/**
 * @deprecated Use {@link org.apache.juneau.rest.remote.RrpcServlet}
 */
@Deprecated
public final class RemoteableServiceProperties {

	/**
	 * Only expose interfaces and methods annotated with {@link Remoteable @Remoteable} ({@link Boolean},
	 * default=<jk>false</jk>).
	 *
	 * <p>
	 * When enabled, the {@link RemoteableServlet} class will only work with annotated remoteable interfaces and methods.
	 * Otherwise, all public methods can be executed through the service.
	 */
	public static final String REMOTEABLE_includeOnlyRemotableMethods = "RemoteableService.includeOnlyRemoteableMethods";
}
