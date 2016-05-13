/*******************************************************************************
 * Copyright (c) 2016 Ericsson.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.cdt.llvm.dsf.lldb.ui;

import org.eclipse.cdt.debug.ui.ICDebuggerPage;
import org.eclipse.cdt.dsf.gdb.internal.ui.launching.LocalApplicationCDebuggerTab;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

@SuppressWarnings("restriction")
public class LLDBLocalApplicationCDebuggerTab extends LocalApplicationCDebuggerTab {

	private final static String LOCAL_DEBUGGER_ID = "lldb-mi";//$NON-NLS-1$

	protected void initDebuggerTypes(String selection) {
		setDebuggerId(LOCAL_DEBUGGER_ID);
		updateComboFromSelection();
	}

	protected void loadDynamicDebugArea() {
		Composite dynamicTabHolder = getDynamicTabHolder();
		// Dispose of any current child widgets in the tab holder area
		Control[] children = dynamicTabHolder.getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].dispose();
		}

		String debuggerId = getIdForCurrentDebugger();
		if (debuggerId == null) {
			setDynamicTab(null);
		} else {
			if (debuggerId.equals(LOCAL_DEBUGGER_ID)) {
				setDynamicTab(new LLDBCDebuggerPage());
			} else {
				assert false : "Unknown debugger id"; //$NON-NLS-1$
			}
		}
		setDebuggerId(debuggerId);

		ICDebuggerPage debuggerPage = getDynamicTab();
		if (debuggerPage == null) {
			return;
		}
		// Ask the dynamic UI to create its Control
		debuggerPage.setLaunchConfigurationDialog(getLaunchConfigurationDialog());
		debuggerPage.createControl(dynamicTabHolder);
		debuggerPage.getControl().setVisible(true);
		dynamicTabHolder.layout(true);
		contentsChanged();
	}
}
