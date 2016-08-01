/*******************************************************************************
 * Copyright (c) 2016 Ericsson.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.cdt.llvm.dsf.lldb.core.internal.launching;

import org.eclipse.cdt.dsf.gdb.launching.GdbLaunch;
import org.eclipse.cdt.llvm.dsf.lldb.core.ILLDBDebugPreferenceConstants;
import org.eclipse.cdt.llvm.dsf.lldb.core.ILLDBLaunchConfigurationConstants;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ISourceLocator;

public class LLDBLaunch extends GdbLaunch {

	public LLDBLaunch(ILaunchConfiguration launchConfiguration, String mode, ISourceLocator locator) {
		super(launchConfiguration, mode, locator);
	}

	public IPath getGDBPath() {
		String lldbPath = getAttribute(ILLDBLaunchConfigurationConstants.ATTR_DEBUG_NAME);
		if (lldbPath != null) {
			return new Path(lldbPath);
		}

		return getLLDBPath(getLaunchConfiguration());
	}

	public static IPath getLLDBPath(ILaunchConfiguration configuration) {
		String defaultLLdbCommand = getDefaultLLDBPath();

		IPath retVal = new Path(defaultLLdbCommand);
		try {
			String lldbPath = configuration.getAttribute(ILLDBLaunchConfigurationConstants.ATTR_DEBUG_NAME, defaultLLdbCommand);
			lldbPath = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(lldbPath, false);
			retVal = new Path(lldbPath);
		} catch (CoreException e) {
			LLDBPlugin.getDefault().getLog().log(e.getStatus());
		}
		return retVal;
	}

	protected String getDefaultGDBPath() {
		return getDefaultLLDBPath();
	}

	private static String getDefaultLLDBPath() {
		return Platform.getPreferencesService().getString(LLDBPlugin.PLUGIN_ID,
				ILLDBDebugPreferenceConstants.PREF_DEFAULT_LLDB_COMMAND,
				ILLDBLaunchConfigurationConstants.DEBUGGER_DEBUG_NAME_DEFAULT, null);
	}

	@Override
	public String getGDBInitFile() throws CoreException {
		// Not supported by LLDB-MI right now
		return null;
	}

	public void setGDBPath(String path) {
		setAttribute(ILLDBLaunchConfigurationConstants.ATTR_DEBUG_NAME, path);
	}

	@Override
	public String getProgramPath() throws CoreException {
		IPath path = new Path(super.getProgramPath());
		if (!path.isAbsolute()) {
			path = getGDBWorkingDirectory().append(path);
//			File file = new File(path.toOSString());
//			path = new Path(file.getAbsolutePath());
		}
		return path.toOSString();
	}
}
