/*******************************************************************************
 * Copyright (c) 2016 Ericsson.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.cdt.llvm.dsf.lldb;

import org.eclipse.cdt.llvm.dsf.lldb.internal.launching.LLDBPlugin;

public class ILLDBLaunchConfigurationConstants {

	/**
	 * Launch configuration attribute key. The value is the name of
	 * the Debuger associated with a C/C++ launch configuration.
	 */
	public static final String ATTR_DEBUG_NAME = LLDBPlugin.PLUGIN_ID + ".DEBUG_NAME"; //$NON-NLS-1$

	/**
	 * Launch configuration attribute value. The key is ATTR_DEBUG_NAME.
	 */
	public static final String DEBUGGER_DEBUG_NAME_DEFAULT = "lldb-mi"; //$NON-NLS-1$
}
