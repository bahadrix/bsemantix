 
package me.bahadir.bsemantix.handlers;

import me.bahadir.bsemantix.S;
import me.bahadir.bsemantix.parts.ConsolePart;

import org.eclipse.e4.core.di.annotations.Execute;

public class ClearConsole {
	@Execute
	public void execute() {

		S.broker.send(ConsolePart.TOPIC_CONSOLE_CLEAR, "clear");
	}
		
}