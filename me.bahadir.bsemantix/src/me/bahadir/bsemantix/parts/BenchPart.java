package me.bahadir.bsemantix.parts;

import java.awt.Frame;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import me.bahadir.bsemantix.Console;
import me.bahadir.bsemantix.NeuralBench;
import me.bahadir.bsemantix.S;
import me.bahadir.bsemantix.physics.Physics;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IPartService;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class BenchPart extends PartBase {
	protected static Logger log = Logger.getLogger(BenchPart.class.getSimpleName());

	@Inject
	private EPartService partService;
	private String ontoText = "";
	private NeuralBench nb;

	public static MApplication application;

	@Inject
	public BenchPart() {

	}

	@Inject @Optional
	void out(@UIEventTopic("PRINT_ONTOLOGY") String s) {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			nb.getOntoAdapter().getModel().write(baos, s);
			
			try {
				String code = new String(baos.toByteArray(), "UTF-8");
				log.info(code);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
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
				if (tbShowGrid.getSelection()) {
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

		final ToolItem tbDRepulsive = new ToolItem(bar, SWT.PUSH);
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
				nb.disperse(Physics.FORCE_LONGING | Physics.FORCE_REPULSIVE | Physics.FORCE_WITH_EDGE, 8);
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

		// broker = context.get(IEventBroker.class);
		// Console.println("Composite created");

	}

	@AboutToShow
	public void onShow() {

	}

	@Focus
	public void onFocus(EPartService service) {

		// burasý 3-4 kere çaðýrýlýyo mok varmýþ gibi
		MPart p = service.findPart("me.bahadir.bsemantix.inputpart.editor");
		if (p.getContext() == null)
			return;

		StyledText text = (StyledText) p.getContext().get("txtOntology");
		final String code = text.getText();

		if (code.equals(ontoText))
			return;
		ontoText = code;

		S.runJob(new Job("visualizing synaptic ontology") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {

				monitor.beginTask(String.format("Parsing process started..", 100), 100);

				Console.midText("Please wait..");
				log.info("Reading RDF..");

				StringReader sr = new StringReader(code);
				OntModel model = ModelFactory.createOntologyModel();
				model.read(sr, null);
				log.info("Loading ontology..");
				// TODO Dafuq is that ns??
				nb.loadOntology(model, "http://bahadir.me/organiclegislation/");
				log.info("Completed!");

				Console.midText("");
				monitor.done();
				return Status.OK_STATUS;

			}
		});

	}

}