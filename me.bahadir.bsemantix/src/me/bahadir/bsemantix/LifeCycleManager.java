package me.bahadir.bsemantix;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

// For a extended example see
// https://bugs.eclipse.org/382224

public class LifeCycleManager {

	
  @PostContextCreate
  void postContextCreate(final IEventBroker eventBroker) {

    S.broker = eventBroker;

    try {
		BLogger.setup();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  


  }
  

  
} 