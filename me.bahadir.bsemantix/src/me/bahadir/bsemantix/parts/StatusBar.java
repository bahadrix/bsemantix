package me.bahadir.bsemantix.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import me.bahadir.bsemantix.S;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.ProgressProvider;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.StatusLineContributionItem;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.SubStatusLineManager;
import org.eclipse.swt.widgets.Composite;

public class StatusBar extends PartBase{
	
	public static final String TOPIC_STATUS_MID = "STATUS_MID";
	
	StatusLineContributionItem ci;
	
	@Inject @Optional
	void out(@UIEventTopic(TOPIC_STATUS_MID) String s) {
		ci.setText(s);
	}
	
	
	@PostConstruct
	public void createComposite(Composite parent) {

		StatusLineManager mgr = new StatusLineManager();
		mgr.createControl(parent);
		mgr.setMessage("koleyle1");
		
		SubStatusLineManager subMan = new SubStatusLineManager(mgr);
		ci = new StatusLineContributionItem("me.bahadir.bsemantix.status.l");
		subMan.add(ci);
		subMan.setVisible(true);
		
		ci.setText("Ready");
		
		
		
		
	}
	
}
