/*******************************************************************************
 * Copyright (c) 2009, 2013 Andrew Gvozdev and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Andrew Gvozdev - initial API and implementation
 *******************************************************************************/

package org.eclipse.cdt.managedbuilder.language.settings.providers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.cdt.core.envvar.IEnvironmentVariable;
import org.eclipse.cdt.managedbuilder.core.IInputType;
import org.eclipse.cdt.managedbuilder.core.ITool;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.cdt.managedbuilder.core.ManagedBuilderCorePlugin;
import org.eclipse.cdt.managedbuilder.internal.envvar.EnvironmentVariableManagerToolChain;

/**
 * Abstract parser capable to execute compiler command printing built-in compiler
 * specs and parse built-in language settings out of it. The compiler to be used
 * is taken from MBS tool-chain definition.
 * <p>
 * <strong>EXPERIMENTAL</strong>. This class interface is not stable yet as
 * it is not currently (CDT 8.1, Juno) clear how it may need to be used in future.
 * There is no guarantee that this API will work or that it will remain the same.
 * Please do not use this API without consulting with the CDT team.
 * </p>
 * @noextend This class is not intended to be subclassed by clients.
 *
 * @since 8.1
 */
public abstract class ToolchainBuiltinSpecsDetector extends AbstractBuiltinSpecsDetector {
	private Map<String, ITool> toolMap = new HashMap<String, ITool>();

	/**
	 * Concrete compiler specs detectors need to supply tool-chain ID.
	 *
	 * Tool-chain id must be supplied for global providers where we don't
	 * have configuration description to figure that out programmatically.
	 * @since 8.2
	 */
	public abstract String getToolchainId();

	/**
	 * Finds a tool handling given language in the tool-chain of the provider.
	 * This returns the first tool found.
	 */
	private ITool getTool(String languageId) {
		ITool langTool = toolMap.get(languageId);
		if (langTool != null) {
			return langTool;
		}

		String toolchainId = getToolchainId();
		for (IToolChain toolchain = ManagedBuildManager.getExtensionToolChain(toolchainId);toolchain != null;toolchain = toolchain.getSuperClass()) {
			ITool tool = getTool(languageId, toolchain);
			if (tool != null) {
				return tool;
			}
		}
		ManagedBuilderCorePlugin.error("Unable to find tool in toolchain=" + toolchainId + " for language=" + languageId); //$NON-NLS-1$ //$NON-NLS-2$
		return null;
	}

	/**
	 * Finds a tool handling given language in the tool-chain.
	 * This returns the first tool found.
	 */
	private ITool getTool(String languageId, IToolChain toolchain) {
		ITool[] tools = toolchain.getTools();
		for (ITool tool : tools) {
			IInputType[] inputTypes = tool.getInputTypes();
			for (IInputType inType : inputTypes) {
				String lang = inType.getLanguageId(tool);
				if (languageId.equals(lang)) {
					toolMap.put(languageId, tool);
					return tool;
				}
			}
		}
		return null;
	}

	@Override
	protected String getCompilerCommand(String languageId) {
		ITool tool = getTool(languageId);
		String compilerCommand = tool.getToolCommand();
		if (compilerCommand.isEmpty()) {
			ManagedBuilderCorePlugin.error("Unable to find compiler command in toolchain=" + getToolchainId()); //$NON-NLS-1$
		}
		return compilerCommand;
	}

	@Override
	protected String getSpecFileExtension(String languageId) {
		String ext = null;
		ITool tool = getTool(languageId);
		String[] srcFileExtensions = tool.getAllInputExtensions();
		if (srcFileExtensions != null && srcFileExtensions.length > 0) {
			ext = srcFileExtensions[0];
		}
		if (ext == null || ext.isEmpty()) {
			ManagedBuilderCorePlugin.error("Unable to find file extension for language " + languageId); //$NON-NLS-1$
		}
		return ext;
	}

	@Override
	protected List<IEnvironmentVariable> getEnvironmentVariables() {
		if (envMngr == null && currentCfgDescription == null) {
			// For global provider need to include toolchain in the equation
			IToolChain toolchain = ManagedBuildManager.getExtensionToolChain(getToolchainId());
			envMngr = new EnvironmentVariableManagerToolChain(toolchain);
		}
		List<IEnvironmentVariable> vars = super.getEnvironmentVariables();

		return vars;
	}
}
