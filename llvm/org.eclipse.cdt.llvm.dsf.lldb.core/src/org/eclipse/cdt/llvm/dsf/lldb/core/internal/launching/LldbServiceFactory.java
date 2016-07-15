/*******************************************************************************
 * Copyright (c) 2016 Ericsson.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.cdt.llvm.dsf.lldb.core.internal.launching;

import org.eclipse.cdt.dsf.debug.service.IBreakpoints;
import org.eclipse.cdt.dsf.debug.service.IProcesses;
import org.eclipse.cdt.dsf.debug.service.command.ICommandControl;
import org.eclipse.cdt.dsf.gdb.service.GdbDebugServicesFactory;
import org.eclipse.cdt.dsf.mi.service.command.CommandFactory;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.debug.core.ILaunchConfiguration;

public class LldbServiceFactory extends GdbDebugServicesFactory {

	public LldbServiceFactory(String version, ILaunchConfiguration config) {
		super(version, config);
	}

	@Override
	protected ICommandControl createCommandControl(DsfSession session, ILaunchConfiguration config) {
		return new LldbControl(session, config, new CommandFactory());
	}

	@Override
	protected IBreakpoints createBreakpointService(DsfSession session) {
		return new LldbBreakpoints(session);
	}

	@Override
	protected IProcesses createProcessesService(DsfSession session) {
		return new LldbProcesses(session);
	}
}
