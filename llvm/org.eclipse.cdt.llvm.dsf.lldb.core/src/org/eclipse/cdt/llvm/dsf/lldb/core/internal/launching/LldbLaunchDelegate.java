/*******************************************************************************
 * Copyright (c) 2016 Ericsson.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.cdt.llvm.dsf.lldb.core.internal.launching;

import org.eclipse.cdt.dsf.debug.service.IDsfDebugServicesFactory;
import org.eclipse.cdt.dsf.gdb.launching.GdbLaunch;
import org.eclipse.cdt.dsf.gdb.launching.GdbLaunchDelegate;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ISourceLocator;

public class LldbLaunchDelegate extends GdbLaunchDelegate {

	public LldbLaunchDelegate() {
		super();
	}

	public LldbLaunchDelegate(boolean requireCProject) {
		super(requireCProject);
	}

	@Override
	protected String getCLILabel(ILaunchConfiguration config, String gdbVersion) throws CoreException {
		return LLDBLaunch.getLLDBPath(config).toString().trim() + " (" + Messages.LldbLaunchDelegate_mimicking_gdb + " gdb " + gdbVersion + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	@Override
	protected IDsfDebugServicesFactory newServiceFactory(ILaunchConfiguration config, String version) {
		return new LldbServiceFactory(version, config);
	}

    protected GdbLaunch createGdbLaunch(ILaunchConfiguration configuration, String mode, ISourceLocator locator) throws CoreException {
    	return new LLDBLaunch(configuration, mode, locator);
    }
}
