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
package org.eclipse.cdt.dsf.mi.service.command.output;

/**
 * GDB/MI whatis parsing.
 * @since 4.6
 */
public class CLIPTypeInfo extends MIInfo {

	String type;

	public CLIPTypeInfo(MIOutput out) {
		super(out);
		parse();
	}

	public String getType() {
		return type;
	}

	void parse() {
		StringBuffer buffer = new StringBuffer();
		if (isDone()) {
			MIOutput out = getMIOutput();
			MIOOBRecord[] oobs = out.getMIOOBRecords();
			for (int i = 0; i < oobs.length; i++) {
				if (oobs[i] instanceof MIConsoleStreamOutput) {
					MIStreamRecord cons = (MIStreamRecord) oobs[i];
					String str = cons.getString();
					// We are interested in the shared info
					if (str != null) {
						str = str.trim();
						if (str.startsWith ("type")) { //$NON-NLS-1$
							int equal = str.indexOf('=');
							if (equal > 0) {
								str = str.substring(equal + 1);
							}
						}
						buffer.append(str);
					}
				}
			}
		}
		type = buffer.toString().trim();
	}
}
