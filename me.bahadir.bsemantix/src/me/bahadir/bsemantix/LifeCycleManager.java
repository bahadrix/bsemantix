package me.bahadir.bsemantix;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;

// For a extended example see
// https://bugs.eclipse.org/382224

@SuppressWarnings("restriction")
public class LifeCycleManager {

	@PostContextCreate
	void postContextCreate(final IEventBroker eventBroker, final IWorkspace workspace) {
		
		System.out.println(workspace.getRoot().toString());

		S.broker = eventBroker;

		try {
			BLogger.setup();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}