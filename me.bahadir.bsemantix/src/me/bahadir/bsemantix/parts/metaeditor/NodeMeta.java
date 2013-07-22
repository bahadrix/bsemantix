package me.bahadir.bsemantix.parts.metaeditor;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.ResourceManager;

public class NodeMeta extends Composite {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtNewText;
	private Table table;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public NodeMeta(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ScrolledForm scrldfrmPerson = formToolkit.createScrolledForm(this);
		formToolkit.paintBordersFor(scrldfrmPerson);
		scrldfrmPerson.setText("Person");
		scrldfrmPerson.getBody().setLayout(new GridLayout(1, false));
		
		Section sctnNewSection = formToolkit.createSection(scrldfrmPerson.getBody(), Section.TWISTIE | Section.TITLE_BAR);
		GridData gd_sctnNewSection = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_sctnNewSection.heightHint = 110;
		sctnNewSection.setLayoutData(gd_sctnNewSection);
		formToolkit.paintBordersFor(sctnNewSection);
		sctnNewSection.setText("Literals");
		sctnNewSection.setExpanded(true);
		
		Composite composite = formToolkit.createComposite(sctnNewSection, SWT.NONE);
		formToolkit.paintBordersFor(composite);
		sctnNewSection.setClient(composite);
		composite.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = formToolkit.createLabel(composite, "New Label", SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setBounds(0, 0, 55, 15);
		
		txtNewText = formToolkit.createText(composite, "New Text", SWT.NONE);
		txtNewText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_1 = formToolkit.createLabel(composite, "New Label", SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Section sctnNewSection_1 = formToolkit.createSection(scrldfrmPerson.getBody(), Section.TWISTIE | Section.TITLE_BAR);
		sctnNewSection_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.paintBordersFor(sctnNewSection_1);
		sctnNewSection_1.setText("Relations");
		sctnNewSection_1.setExpanded(true);
		
		Composite composite_1 = formToolkit.createComposite(sctnNewSection_1, SWT.NONE);
		formToolkit.paintBordersFor(composite_1);
		sctnNewSection_1.setClient(composite_1);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		CTabFolder tabFolder = new CTabFolder(composite_1, SWT.BORDER);
		formToolkit.adapt(tabFolder);
		formToolkit.paintBordersFor(tabFolder);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tbtmNewItem = new CTabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setImage(ResourceManager.getPluginImage("me.bahadir.bsemantix", "icons/sweet/arrow-incident-blue-16-ns.png"));
		tbtmNewItem.setText("New Item");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem.setControl(composite_2);
		formToolkit.paintBordersFor(composite_2);
		TableColumnLayout tcl_composite_2 = new TableColumnLayout();
		composite_2.setLayout(tcl_composite_2);
		
		table = new Table(composite_2, SWT.BORDER | SWT.FULL_SELECTION);
		formToolkit.adapt(table);
		formToolkit.paintBordersFor(table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tcl_composite_2.setColumnData(tblclmnNewColumn, new ColumnPixelData(150, true, true));
		tblclmnNewColumn.setText("New Column");
		
		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tcl_composite_2.setColumnData(tblclmnNewColumn_1, new ColumnPixelData(150, true, true));
		tblclmnNewColumn_1.setText("New Column");
		
		TableItem tableItem = new TableItem(table, SWT.NONE);
		tableItem.setText("New TableItem");
		
		TableItem tableItem_1 = new TableItem(table, SWT.NONE);
		tableItem_1.setText("New TableItem");
		
		TableItem tableItem_2 = new TableItem(table, SWT.NONE);
		tableItem_2.setText("New TableItem");
		
		CTabItem tbtmNewItem_1 = new CTabItem(tabFolder, SWT.NONE);
		tbtmNewItem_1.setImage(ResourceManager.getPluginImage("me.bahadir.bsemantix", "icons/sweet/arrow-incident-green-16-ns.png"));
		tbtmNewItem_1.setText("New Item");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
