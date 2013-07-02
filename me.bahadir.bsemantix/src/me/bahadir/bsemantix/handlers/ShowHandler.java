 
package me.bahadir.bsemantix.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import me.bahadir.bsemantix.parts.ConsolePart;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class ShowHandler extends HandlerBase {


	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {

		Console.println("laayn");
	}
	
		
}