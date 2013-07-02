package me.bahadir.bsemantix;

import me.bahadir.bsemantix.parts.ConsolePart;
import me.bahadir.bsemantix.parts.StatusBar;

public class Console {

	private Class<? extends Object> c;
	
	public Console(Class<? extends Object> c) {
		this.c = c;
	}
	
	
	public void objectPrintln(String s) {
		Console.println(c.getSimpleName() + "> " + s);
	}
	
	
	public static void print(String s) {
		if(S.broker == null) {
			System.out.print(s);
		} else {
			S.broker.send(ConsolePart.TOPIC_CONSOLE_INFO, s);
		}
	}
	public static void println(String s) {
		print(s + "\r\n");
	}
	public static void midText(String s) {
		S.broker.send(StatusBar.TOPIC_STATUS_MID, s);
	}

}
