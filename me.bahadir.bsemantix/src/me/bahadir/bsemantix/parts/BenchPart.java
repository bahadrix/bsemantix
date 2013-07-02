 
package me.bahadir.bsemantix.parts;

import java.awt.Dimension;
import java.awt.Frame;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import me.bahadir.bsemantix.NeuralBench;
import me.bahadir.bsemantix.ngraph.NeuralGraph;
import me.bahadir.bsemantix.physics.Physics;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class BenchPart extends PartBase { 
	protected static Logger log = Logger.getLogger(BenchPart.class.getSimpleName());
	
	private Dimension screenSize;
	private NeuralBench nb;
	
	public static MApplication application;
	
	@Inject
	public BenchPart() {

		screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		
	}
	

	@PostConstruct
	public void createComposite(Composite parent, MApplication application) {
		BenchPart.application = application;
		parent.setLayout(new GridLayout());
		
		ToolBar bar = new ToolBar(parent, SWT.FLAT);
		
		final ToolItem tbShortPath = new ToolItem(bar, SWT.PUSH);
		tbShortPath.setText("Shortest Path");
		
		tbShortPath.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				nb.onShowShortestPath();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		

		
		final ToolItem tbShowGrid = new ToolItem(bar, SWT.CHECK);
		tbShowGrid.setText("Show Grid");
		tbShowGrid.setSelection(false);
		tbShowGrid.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				if(tbShowGrid.getSelection()) {
					nb.showGrid();
				} else {
					nb.hideGrid();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		final ToolItem tbDRepulsive = new ToolItem(bar,  SWT.PUSH);
		tbDRepulsive.setText("D:Repulsive");
		tbDRepulsive.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				nb.disperse(Physics.FORCE_REPULSIVE, 1);
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		final ToolItem tbDLonging = new ToolItem(bar, SWT.PUSH);
		tbDLonging.setText("D:Longing");
		tbDLonging.setSelection(false);
		tbDLonging.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				nb.disperse(Physics.FORCE_LONGING, 1);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		final ToolItem tbDAuto = new ToolItem(bar, SWT.PUSH);
		tbDAuto.setText("D:AUTO");
		tbDAuto.setSelection(false);
		tbDAuto.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				nb.disperse(Physics.FORCE_LONGING | Physics.FORCE_REPULSIVE, 8);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		bar.pack();
		
		Composite awtConatiner = new Composite(parent, SWT.EMBEDDED);
		awtConatiner.setLayoutData(new GridData(GridData.FILL_BOTH));
		Frame frame = SWT_AWT.new_Frame(awtConatiner);

		nb = new NeuralBench(frame);

		//broker = context.get(IEventBroker.class);
		//Console.println("Composite created");
	}
	
	
	@Focus
	public void onFocus() {
		//TODO Your code here
	}


	
}