/*******************************************************************************
 * Copyright (c) 2016 Ericsson and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.cdt.llvm.dsf.lldb.ui.internal;

import java.io.File;

import org.eclipse.cdt.dsf.debug.internal.ui.preferences.StringWithBooleanFieldEditor;
import org.eclipse.cdt.llvm.dsf.lldb.core.ILLDBDebugPreferenceConstants;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * A preference page for settings that are currently supported in LLDB. Based on
 * the GDB equivalent.
 */
public class LLDBDebugPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private StringFieldEditor fStringFieldEditorCommand;
	private StringWithBooleanFieldEditor fEnableStopAtMain;

	public LLDBDebugPreferencePage() {
		super(FLAT);
		IPreferenceStore store = LLDBUIPlugin.getDefault().getCorePreferenceStore();
		// Note that if we don't set it here, it actually never gets flushed. If
		// this page was to use two preference stores, make sure that both are
		// flushed.
		setPreferenceStore(store);
		setDescription(Messages.LLDBDebugPreferencePage_description);
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected void createFieldEditors() {
		final Composite parent = getFieldEditorParent();
		final GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		parent.setLayout(layout);

		final Group group1 = new Group(parent, SWT.NONE);
		group1.setText(Messages.LLDBDebugPreferencePage_defaults_label);
		GridLayout groupLayout = new GridLayout(3, false);
		group1.setLayout(groupLayout);
		group1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		fStringFieldEditorCommand = new StringFieldEditor(ILLDBDebugPreferenceConstants.PREF_DEFAULT_LLDB_COMMAND,
				Messages.LLDBCDebuggerPage_debugger_command, group1);

		fStringFieldEditorCommand.fillIntoGrid(group1, 2);
		addField(fStringFieldEditorCommand);
		Button browsebutton = new Button(group1, SWT.PUSH);
		browsebutton.setText(Messages.LLDBCDebuggerPage_browse);
		browsebutton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleBrowseButtonSelected(Messages.LLDBCDebuggerPage_browse_dialog_title, fStringFieldEditorCommand);
			}
		});
		setButtonLayoutData(browsebutton);

		fEnableStopAtMain = new StringWithBooleanFieldEditor(
				ILLDBDebugPreferenceConstants.PREF_DEFAULT_STOP_AT_MAIN,
				ILLDBDebugPreferenceConstants.PREF_DEFAULT_STOP_AT_MAIN_SYMBOL,
				Messages.LLDBDebugPreferencePage_Stop_on_startup_at,
				group1);
		fEnableStopAtMain.fillIntoGrid(group1, 3);
		addField(fEnableStopAtMain);

		group1.setLayout(groupLayout);
	}

	private void handleBrowseButtonSelected(final String dialogTitle, final StringFieldEditor stringFieldEditor) {
		FileDialog dialog = new FileDialog(getShell(), SWT.NONE);
		dialog.setText(dialogTitle);
		String lldbCommand = stringFieldEditor.getStringValue().trim();
		int lastSeparatorIndex = lldbCommand.lastIndexOf(File.separator);
		if (lastSeparatorIndex != -1) {
			dialog.setFilterPath(lldbCommand.substring(0, lastSeparatorIndex));
		}
		String res = dialog.open();
		if (res == null) {
			return;
		}
		stringFieldEditor.setStringValue(res);
	}

	@Override
	protected void adjustGridLayout() {
		// do nothing
	}
}
