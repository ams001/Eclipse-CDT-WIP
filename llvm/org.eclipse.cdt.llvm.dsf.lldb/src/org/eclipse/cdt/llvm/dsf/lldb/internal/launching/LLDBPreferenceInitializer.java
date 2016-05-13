/*******************************************************************************
 * Copyright (c) 2016 Ericsson.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.cdt.llvm.dsf.lldb.internal.launching;

import org.eclipse.cdt.debug.core.ICDTLaunchConfigurationConstants;
import org.eclipse.cdt.llvm.dsf.lldb.ILLDBDebugPreferenceConstants;
import org.eclipse.cdt.llvm.dsf.lldb.ILLDBLaunchConfigurationConstants;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

public class LLDBPreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IEclipsePreferences node = DefaultScope.INSTANCE.getNode(LLDBPlugin.PLUGIN_ID);
		node.put(ILLDBDebugPreferenceConstants.PREF_DEFAULT_LLDB_COMMAND, ILLDBLaunchConfigurationConstants.DEBUGGER_DEBUG_NAME_DEFAULT);
		node.putBoolean(ILLDBDebugPreferenceConstants.PREF_DEFAULT_STOP_AT_MAIN, ICDTLaunchConfigurationConstants.DEBUGGER_STOP_AT_MAIN_DEFAULT);
		node.put(ILLDBDebugPreferenceConstants.PREF_DEFAULT_STOP_AT_MAIN_SYMBOL, ICDTLaunchConfigurationConstants.DEBUGGER_STOP_AT_MAIN_SYMBOL_DEFAULT);
	}

}
