/*******************************************************************************
 * Copyright (c) 2000, 2014 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     QNX Software Systems - Initial API and implementation
 *     Marc-Andre Laperle (Ericsson) - Adapted to DSF-GDB
 *******************************************************************************/

package org.eclipse.cdt.dsf.mi.service.command.commands;

import org.eclipse.cdt.dsf.debug.service.IRunControl.IContainerDMContext;
import org.eclipse.cdt.dsf.mi.service.command.output.CLIPTypeInfo;
import org.eclipse.cdt.dsf.mi.service.command.output.MIInfo;
import org.eclipse.cdt.dsf.mi.service.command.output.MIOutput;

/**
 * 
 *    ptype type
 * @since 4.3
 *
 */
public class CLIPType extends CLICommand<CLIPTypeInfo> 
{
	public CLIPType(IContainerDMContext ctx, String var) {
		super(ctx, "ptype " + var); //$NON-NLS-1$
	}
	
	@Override
	public CLIPTypeInfo getResult(MIOutput output) {
		return (CLIPTypeInfo)getMIInfo(output);
	}

	public MIInfo getMIInfo(MIOutput out) {
		MIInfo info = null;
		if (out != null) {
			info = new CLIPTypeInfo(out);
		}
		return info;
	}
}
