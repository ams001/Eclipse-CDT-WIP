/*******************************************************************************
 * Copyright (c) 2015 Ericsson and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.cdt.managedbuilder.language.settings.providers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.cdt.managedbuilder.core.ManagedBuilderCorePlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

/**
 * @since 8.3
 */
public class PkgConfigGCCBuildCommandParser extends GCCBuildCommandParser {

	private static final int COMMAND_GROUP = 1;
	private final static Pattern PKG_CONFIG_PATTERN = Pattern.compile("`(pkg-config.*--cflags.*)`"); //$NON-NLS-1$

	@Override
	protected List<String> parseOptions(String line) {
		String parsedResourceName = parseResourceName(line);
		if (line == null || (parsedResourceName == null && getResourceScope() != ResourceScope.PROJECT)) {
			return null;
		}

		List<String> options = new ArrayList<String>();
		Matcher optionMatcher = PKG_CONFIG_PATTERN.matcher(line);
		while (optionMatcher.find()) {
			String option = optionMatcher.group(COMMAND_GROUP);

			if (option != null) {
				ProcessBuilder pb = null;
				if (Platform.getOS() == Platform.OS_WIN32) {
					pb = new ProcessBuilder("cmd", "/c", option); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					pb = new ProcessBuilder("bash", "-c", option.toString()); //$NON-NLS-1$ //$NON-NLS-2$
				}
				List<String> runCommand = runCommand(pb);
				for (String pkgline : runCommand) {
					options.addAll(super.parseOptions(pkgline));
				}
			}
		}
		return options;
	}

	/**
	 * Run the process and get the results as string array.
	 * 
	 * @param pb
	 *            Process builder
	 * @return Array of process results
	 */
	private static List<String> runCommand(ProcessBuilder pb) {
		List<String> results = new ArrayList<String>();

		try {
			Process p = pb.start();
			String line;
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			do {
				line = input.readLine();
				if (line != null) {
					results.add(line);
				}
			} while (line != null);
			input.close();
		} catch (IOException e) {
			ManagedBuilderCorePlugin.log(new Status(IStatus.ERROR, ManagedBuilderCorePlugin.PLUGIN_ID,
					"Starting a process (executing a command line script) failed.", e)); //$NON-NLS-1$
		}
		return results;
	}

}
