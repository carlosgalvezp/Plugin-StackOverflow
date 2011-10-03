                                                                     
                                                                     
                                                                     
                                             
/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package stackoverflow.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.*;

import stackoverflow.BrowserExample;
import stackoverflow.Buscador;
import stackoverflow.Traductor;



/**
 * <code>BrowserView</code> is a simple demonstration
 * of the SWT Browser widget.  It consists of a workbench
 * view and tab folder where each tab in the folder allows the
 * user to interact with a control.
 * 
 * @see ViewPart
 */
public class BrowserView extends ViewPart {
	BrowserExample instance = null;
	Composite parent;

	
	/**
	 * Create the example
	 * 
	 * @see ViewPart#createPartControl
	 */
	public void createPartControl(Composite parent) {
		//instance = new BrowserExample(frame, true);
		
		this.parent = parent;
		parent.setLayout(new FillLayout());
		try {
			Browser browser = new Browser(parent, SWT.NONE);
			browser.dispose();
		} catch (SWTError e) {
			Text text = new Text(parent, SWT.MULTI | SWT.READ_ONLY);
			text.setText("Browser widget cannot be instantiated. The exact error is:\r\n"+e);
			parent.layout(true);
			return;
		}
		TabFolder folder = new TabFolder(parent, SWT.NONE);
		TabItem item = new TabItem(folder, SWT.NONE);
		instance = new BrowserExample(item,true);
		
		item = new TabItem(folder, SWT.NONE);
		new Traductor(item);
		
	}

	/**
	 * Called when we must grab focus.
	 * 
	 * @see org.eclipse.ui.part.ViewPart#setFocus
	 */
	public void setFocus() {
		instance.focus();
	}

	/**
	 * Called when the View is to be disposed
	 */	
	public void dispose() {
		instance.dispose();
		instance = null;
		super.dispose();
	}
}