 
package me.bahadir.bsemantix.parts;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.eclipse.e4.ui.di.Focus;

public class DecisionEditorPart {
	
	private Graph graph;
	private int layout = 1;
	
	@Inject
	public DecisionEditorPart() {
		//TODO Your code here
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		graph = new Graph(parent, SWT.NONE);
		
	    // Now a few nodes
	    GraphNode node1 = new GraphNode(graph, SWT.NONE, "Jim");
	    node1.setHighlightColor(parent.getDisplay().getSystemColor(SWT.COLOR_BLUE));
	    GraphNode node4 = new GraphNode(graph, SWT.NONE, "Taraðan Bulahanýk okasdkjan alsj lba lajd");
	   
	    GraphConnection graphConnection = new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED,
	        node1, node4);
	    graphConnection.changeLineColor(parent.getDisplay().getSystemColor(SWT.COLOR_GREEN));
	    // Also set a text
	    graphConnection.setText("This is a text");
	    graphConnection.setHighlightColor(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
	    graphConnection.setLineWidth(3);
	
	    graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
	    // Selection listener on graphConnect or GraphNode is not supported
	    // see https://bugs.eclipse.org/bugs/show_bug.cgi?id=236528
	    graph.addSelectionListener(new SelectionAdapter() {
	      @Override
	      public void widgetSelected(SelectionEvent e) {
	        System.out.println(e);
	      }

	    });
	}
	
	
	
	@Focus
	public void onFocus() {
		//TODO Your code here
	}
	
	
}