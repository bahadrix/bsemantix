package me.bahadir.bsemantix;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;

public class Processor {

	@Inject
	MApplication application;
	
	@Execute
	public void exec() {
		S.application = application;

	}
	
	public Processor() {
		
	}

}
