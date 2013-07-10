package me.bahadir.bsemantix.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

public class ConsolePart {
	public static final String TOPIC_CONSOLE_INFO = "CONSOLE_INFO";
	private StyledText consoleText;
	
	@Inject @Optional
	void out(@UIEventTopic(TOPIC_CONSOLE_INFO) String s) {
		consoleText.append(s);
		
		consoleText.selectAll();
		consoleText.setSelection(consoleText.getCharCount());
	}
	
	@Inject
	public ConsolePart() {

		
	}
	
	@PostConstruct
	public void createComposite(Composite parent) {
		Font font = new Font(parent.getDisplay(), "Courier New", 10, SWT.NORMAL);
		
		consoleText = new StyledText(parent, SWT.FLAT | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		consoleText.setFont(font);
		consoleText.setEditable(false);
		consoleText.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		
		
	}
	
	
	
	@Focus
	public void onFocus() {
		//TODO Your code here
	}
}
