package me.bahadir.bsemantix.parts;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import me.bahadir.bsemantix.Activator;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.custom.CCombo;

public class PropertiesPart extends ViewPart {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());


	@Inject
	public PropertiesPart() {
		// TODO Your code here
		
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		
		
		
	}

	@Focus
	public void onFocus() {
		// TODO Your code here
	}
	@PostConstruct
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Form frmNewForm = formToolkit.createForm(parent);
		formToolkit.paintBordersFor(frmNewForm);
		frmNewForm.setText("Node Editor");
		frmNewForm.getBody().setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Section sctnQuestion = formToolkit.createSection(frmNewForm.getBody(), Section.TWISTIE | Section.TITLE_BAR);
		formToolkit.paintBordersFor(sctnQuestion);
		sctnQuestion.setText("Question");
		// TODO Auto-generated method stub
		
		
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
}