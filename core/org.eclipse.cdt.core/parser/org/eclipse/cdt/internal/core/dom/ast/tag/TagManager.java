/*
 * Copyright (c) 2013 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.cdt.internal.core.dom.ast.tag;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.tag.IBindingTagger;
import org.eclipse.cdt.core.dom.ast.tag.ITag;
import org.eclipse.cdt.core.dom.ast.tag.ITagReader;
import org.eclipse.cdt.core.dom.ast.tag.ITagWriter;
import org.eclipse.cdt.internal.core.pdom.dom.IPDOMBinding;
import org.eclipse.cdt.internal.core.pdom.tag.PDOMTaggable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public class TagManager
{
	private static TagManager INSTANCE;

	private Map<String, TaggerDescriptor> taggers;

	public static TagManager getInstance()
	{
		if( INSTANCE == null )
			INSTANCE = new TagManager();
		return INSTANCE;
	}

	private TagManager()
	{
		taggers = loadExtensions();
	}

	private static final String ExtensionPoint = "tagger"; //$NON-NLS-1$

	private static Map<String, TaggerDescriptor> loadExtensions()
	{
		Map<String, TaggerDescriptor> taggers = new HashMap<String, TaggerDescriptor>();

		// load the extensions
		IConfigurationElement[] elements
			= Platform.getExtensionRegistry().getConfigurationElementsFor( CCorePlugin.PLUGIN_ID, ExtensionPoint );
		for (IConfigurationElement element : elements)
		{
			TaggerDescriptor desc = new TaggerDescriptor( element );
			taggers.put( desc.getId(), desc );
		}

		return taggers;
	}

	/** Provide an opportunity for the specified tagger to process the given values.  The tagger will only
	 *  run if its enablement expression returns true for the arguments. */
	public ITag process( String taggerId, ITagWriter tagWriter, IBinding binding, IASTName ast )
	{
		TaggerDescriptor desc = taggers.get( taggerId );
		if( desc == null )
			return null;

		IBindingTagger tagger = desc.getBindingTaggerFor( binding, ast );
		return tagger == null ? null : tagger.process( tagWriter, binding, ast );
	}

	/** Provide an opportunity for all enabled taggers to process the given values. */
	public Iterable<ITag> process( ITagWriter tagWriter, IBinding binding, IASTName ast )
	{
		List<ITag> tags = new LinkedList<ITag>();
		for( TaggerDescriptor desc : taggers.values() )
		{
			IBindingTagger tagger = desc.getBindingTaggerFor( binding, ast );
			if( tagger != null )
			{
				ITag tag = tagger.process( tagWriter, binding, ast );
				if( tag != null )
					tags.add( tag );
			}
		}

		return tags;
	}

	/** Add or remove tags from the destination to ensure that it has the same tag information as the source. */
	public void syncTags( IPDOMBinding dst, IBinding src )
	{
		if( dst == null )
			return;

		ITagReader tagReader = CCorePlugin.getTagService().findTagReader( src );
		if( tagReader == null )
			return;

		ITagWriter tagWriter = new PDOMTaggable( dst.getPDOM(), dst.getRecord() );
		tagWriter.setTags( tagReader.getTags() );
	}
}
