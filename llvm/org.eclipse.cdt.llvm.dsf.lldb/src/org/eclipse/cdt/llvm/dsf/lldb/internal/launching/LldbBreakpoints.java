/*******************************************************************************
 * Copyright (c) 2016 Ericsson.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.cdt.llvm.dsf.lldb.internal.launching;

import java.util.Map;

import org.eclipse.cdt.dsf.gdb.service.GDBBreakpoints_7_4;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.core.runtime.Path;

public class LldbBreakpoints extends GDBBreakpoints_7_4 {

	public LldbBreakpoints(DsfSession session) {
		super(session);
	}

	@Override
	protected String formatLocation(Map<String, Object> attributes) {
		// FIXME: ***Huge hack*** lldb-mi's -breakpoint-insert doesn't handle
		// locations that look like absolute paths (leading /). This will have
		// to be fixed upstream because the work-around is not ideal: we only
		// use the last segment to insert the breakpoint. This is not good if
		// there are two files of the same name in the inferior.
		String location = super.formatLocation(attributes);
		Path path = new Path(location);
		if (path.isAbsolute()) {
			return path.lastSegment();
		}
		return location;
	}
}
