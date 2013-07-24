 
package me.bahadir.bsemantix.handlers;

import javax.inject.Named;

import me.bahadir.bsemantix.S;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;

public class ShowHandler extends HandlerBase {


	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {

		S.broker.post("PRINT_ONTOLOGY", "RDF/XML");
	}
	
		
}