package com.mule.support.handlers;

import org.eclipse.core.commands.*;
import org.eclipse.ui.*;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.window.Window;


public class SampleHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		IWorkbenchPage activePage = window.getActivePage();
		
		SupportTicketDialog dialog = new SupportTicketDialog(window.getShell());
		dialog.create();
		if (dialog.open() == Window.OK) {
		    System.out.println("TEST");
		    System.out.println(dialog.getProjectName());
		    System.out.println(dialog.getDescription());
		}
		
		return null;
	}
	
}
