/*******************************************************************************
 * Copyright (c) 2016 Ericsson.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.cdt.llvm.dsf.lldb.internal.launching;

import java.util.Map;

import org.eclipse.cdt.dsf.concurrent.RequestMonitor;
import org.eclipse.cdt.dsf.concurrent.RequestMonitorWithProgress;
import org.eclipse.cdt.dsf.gdb.launching.FinalLaunchSequence_7_2;
import org.eclipse.cdt.dsf.service.DsfSession;

public class LldbFinalLaunchSequence extends FinalLaunchSequence_7_2 {

	public LldbFinalLaunchSequence(DsfSession session, Map<String, Object> attributes, RequestMonitorWithProgress rm) {
		super(session, attributes, rm);
	}

	@Execute
	@Override
	public void stepSetNonStop(RequestMonitor requestMonitor) {
		// LLDB doesn't support non-stop and target-async cannot be disabled so
		// do not do anything in this step
		requestMonitor.done();
	}

}
