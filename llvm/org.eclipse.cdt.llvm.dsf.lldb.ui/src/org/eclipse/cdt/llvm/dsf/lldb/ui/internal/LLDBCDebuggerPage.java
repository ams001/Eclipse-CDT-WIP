/*******************************************************************************
 * Copyright (c) 2016 Ericsson.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.cdt.llvm.dsf.lldb.ui.internal;

import java.io.File;

import org.eclipse.cdt.debug.ui.AbstractCDebuggerPage;
import org.eclipse.cdt.llvm.dsf.lldb.ILLDBDebugPreferenceConstants;
import org.eclipse.cdt.llvm.dsf.lldb.ILLDBLaunchConfigurationConstants;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class LLDBCDebuggerPage extends AbstractCDebuggerPage {

	protected Text fLLDBCommandText;

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());

		composite.setLayout(new GridLayout(3, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		Label lbl = new Label(composite, SWT.LEFT);
		lbl.setFont(parent.getFont());
		lbl.setText(Messages.LLDBCDebuggerPage_debugger_command);
		fLLDBCommandText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		fLLDBCommandText.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		fLLDBCommandText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent evt) {
					updateLaunchConfigurationDialog();
			}
		});

		Button button = createPushButton(composite, Messages.LLDBCDebuggerPage_browse, null);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				handleGDBButtonSelected();
				updateLaunchConfigurationDialog();
			}

			private void handleGDBButtonSelected() {
				FileDialog dialog = new FileDialog(getShell(), SWT.NONE);
				dialog.setText(Messages.LLDBCDebuggerPage_browse_dialog_title);
				String gdbCommand = fLLDBCommandText.getText().trim();
				int lastSeparatorIndex = gdbCommand.lastIndexOf(File.separator);
				if (lastSeparatorIndex != -1) {
					dialog.setFilterPath(gdbCommand.substring(0, lastSeparatorIndex));
				}
				String res = dialog.open();
				if (res == null) {
					return;
				}
				fLLDBCommandText.setText(res);
			}
		});
		setControl(parent);
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		IPreferenceStore corePreferenceStore = LLDBUIPlugin.getDefault().getCorePreferenceStore();
		configuration.setAttribute(ILLDBLaunchConfigurationConstants.ATTR_DEBUG_NAME,
				corePreferenceStore.getString(ILLDBDebugPreferenceConstants.PREF_DEFAULT_LLDB_COMMAND));
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		IPreferenceStore preferenceStore = LLDBUIPlugin.getDefault().getCorePreferenceStore();
		String lldbCommand = getStringAttr(configuration, ILLDBLaunchConfigurationConstants.ATTR_DEBUG_NAME,
				preferenceStore.getString(ILLDBDebugPreferenceConstants.PREF_DEFAULT_LLDB_COMMAND));
		fLLDBCommandText.setText(lldbCommand);
	}

	/** utility method to cut down on clutter */
	private static String getStringAttr(ILaunchConfiguration config, String attributeName, String defaultValue) {
		try {
			return config.getAttribute(attributeName, defaultValue);
		} catch (CoreException e) {
			return defaultValue;
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(ILLDBLaunchConfigurationConstants.ATTR_DEBUG_NAME,
				fLLDBCommandText.getText().trim());
	}

	@Override
	public String getName() {
		return Messages.LLDBCDebuggerPage_tab_name;
	}

}
