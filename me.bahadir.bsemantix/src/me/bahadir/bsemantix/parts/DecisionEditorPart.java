 
package me.bahadir.bsemantix.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import me.bahadir.bsemantix.S;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

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
	    //node1.setHighlightColor(parent.getDisplay().getSystemColor(SWT.COLOR_BLUE));
	    
	    node1.setForegroundColor(S.SWTColor(255, 255, 255));
	    node1.setBackgroundColor(S.SWTColor(7, 114, 161));
	    node1.setHighlightColor(S.SWTColor(92, 204, 204));
	    node1.setBorderHighlightColor(S.SWTColor(51, 204, 204));
	    node1.setBorderColor(S.SWTColor(51, 204, 204));

	
	    GraphNode node4 = new GraphNode(graph, SWT.NONE, "Output: Devin");
	    node4.setForegroundColor(S.SWTColor(255, 255, 255));
	    node4.setBackgroundColor(S.SWTColor(200, 113, 48));
	    node4.setHighlightColor(S.SWTColor(255, 116, 0));
	    node4.setBorderHighlightColor(S.SWTColor(255, 150, 64));
	    GraphConnection graphConnection = new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED,
	        node1, node4);
	   
	    // Also set a text
	    graphConnection.setText("This is a text");
	    //graphConnection.setLineColor(S.SWTColor(0, 99, 99));
	    //graphConnection.setHighlightColor(S.SWTColor(r, g, b));
	    //graphConnection.setLineWidth(2);
	
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