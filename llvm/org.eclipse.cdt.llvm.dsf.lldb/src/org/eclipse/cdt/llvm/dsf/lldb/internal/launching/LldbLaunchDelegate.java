/*******************************************************************************
 * Copyright (c) 2016 Ericsson.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.cdt.llvm.dsf.lldb.internal.launching;

import org.eclipse.cdt.dsf.debug.service.IDsfDebugServicesFactory;
import org.eclipse.cdt.dsf.gdb.launching.GdbLaunch;
import org.eclipse.cdt.dsf.gdb.launching.GdbLaunchDelegate;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ISourceLocator;

public class LldbLaunchDelegate extends GdbLaunchDelegate {

	@Override
	protected IDsfDebugServicesFactory newServiceFactory(ILaunchConfiguration config, String version) {
		return new LldbServiceFactory(version, config);
	}

    protected GdbLaunch createGdbLaunch(ILaunchConfiguration configuration, String mode, ISourceLocator locator) throws CoreException {
    	return new LLDBLaunch(configuration, mode, locator);
    }
}
