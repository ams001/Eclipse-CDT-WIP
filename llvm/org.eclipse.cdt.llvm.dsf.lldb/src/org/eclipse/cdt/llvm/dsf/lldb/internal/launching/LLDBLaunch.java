/*******************************************************************************
 * Copyright (c) 2016 Ericsson.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.cdt.llvm.dsf.lldb.internal.launching;

import org.eclipse.cdt.dsf.gdb.IGDBLaunchConfigurationConstants;
import org.eclipse.cdt.dsf.gdb.launching.GdbLaunch;
import org.eclipse.cdt.llvm.dsf.lldb.ILLDBLaunchConfigurationConstants;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ISourceLocator;

public class LLDBLaunch extends GdbLaunch {

	public LLDBLaunch(ILaunchConfiguration launchConfiguration, String mode, ISourceLocator locator) {
		super(launchConfiguration, mode, locator);
	}

	public IPath getGDBPath() {
		try {
			String gdb = getAttribute(IGDBLaunchConfigurationConstants.ATTR_DEBUG_NAME);
			if (gdb == null) {
				gdb = getLaunchConfiguration().getAttribute(ILLDBLaunchConfigurationConstants.ATTR_DEBUG_NAME,
						getDefaultGDBPath());
			}
			if (gdb != null) {
				gdb = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(gdb, false);
				return new Path(gdb);
			} else {
				return null;
			}
		} catch (CoreException e) {
			//FIXME: GdbPlugin.log(e.getStatus());
			return null;
		}
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
