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
package org.apache.juneau.doc.internal;

import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * Javadoc link checker.
 *
 * <p>
 * Runs against the generated javadocs folder looking for any broken internal links (missing files, invalid anchor tags, etc...).
 */
public class DocLinkTester {

	private static Map<String,Set<String>> ANCHORS = new LinkedHashMap<>();
	private static Pattern p = Pattern.compile("(href|src)\\=['\\\"]([^'\\\"]+)['\\\"]");
	private static Pattern p2 = Pattern.compile("(name|id)\\=['\\\"]([^'\\\"]+)['\\\"]");
	private static int errors, files, directories, links;

	/**
	 * Entry point.
	 *
	 * @param args Not used
	 */
	public static void main(String[] args) {
		try {
			long startTime = System.currentTimeMillis();
			File root = new File("../target/site/apidocs").getCanonicalFile();
			System.out.println("Checking " + root);
			process(root);
			System.out.println("Checked "+links+" links in " + files + " files in " + directories + " directories in "+(System.currentTimeMillis()-startTime)+"ms");
			if (errors == 0)
				System.out.println("No errors");
			else {
				System.out.flush();
				System.err.println(errors + " errors");  // NOT DEBUG
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void process(File dir) throws Exception {
		if (dir.isDirectory()) {
			for (File fc : dir.listFiles()) {
				if (fc.isFile() && fc.getName().endsWith(".html")) {
					files++;
					resolveLinks(fc);
				}
			}
			for (File fc : dir.listFiles()) {
				if (fc.isDirectory() && ! fc.getName().equals("src-html")) {
					directories++;
					process(fc);
				}
			}
		}
	}

	private static boolean hasAnchor(File f, String anchor) throws Exception {
		String key = f.getCanonicalPath();
		if (! ANCHORS.containsKey(key)) {
			Set<String> s = new HashSet<>();
			String c2 = IOUtils.read(f);
			Matcher m2 = p2.matcher(c2);
			while (m2.find()) {
				s.add(m2.group(2));
			}
			ANCHORS.put(key, s);
		}
		return ANCHORS.get(key).contains(anchor);
	}

	private static void resolveLinks(File f) throws Exception {
		String contents = IOUtils.read(f);
		Matcher m = p.matcher(contents);
		while (m.find()) {
			String link = m.group(2);
			String anchor = null;
			if (link.startsWith("https://") || link.startsWith("http://") || link.startsWith("mailto:"))
				continue;
			links++;
			if (link.indexOf('?') != -1)
				link = link.substring(0, link.indexOf('?'));

			if (link.indexOf('#') != -1) {
				anchor = link.substring(link.lastIndexOf('#')+1);
				link = link.substring(0, link.lastIndexOf('#'));
				File f2 = link.isEmpty() ? f : new File(f.getParentFile().getAbsolutePath() + "/" + link);
				if (! f2.exists()) {
					error(f, "missingLink=["+link+"]");
				} else if (anchor != null) {
					if (f2.isFile()) {
						boolean foundAnchor = hasAnchor(f2, anchor);
						if (! foundAnchor)
							error(f, "missingAnchor=["+link+"#"+anchor+"]");
					} else {
						error(f, "invalidAnchor=["+link+"#"+anchor+"]");
					}
				}
			}
		}
	}

	private static void error(File f, String msg) {
		errors++;
		System.out.flush();
		System.err.println("ERROR: " + f.getAbsolutePath() + ", " + msg);  // NOT DEBUG
	}
}