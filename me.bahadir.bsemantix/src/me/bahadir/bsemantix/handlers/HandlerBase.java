package me.bahadir.bsemantix.handlers;

import javax.inject.Inject;

import me.bahadir.bsemantix.parts.ConsolePart;

import org.eclipse.e4.core.services.events.IEventBroker;

public class HandlerBase {
	@Inject static IEventBroker broker;
	
	public static class Console {
		
		public static void print(String s) {
			broker.send(ConsolePart.TOPIC_CONSOLE_INFO, s);

		}
		
		public static void println(String s) {
			print(s + "\r\n");
		}
	}
	
}
