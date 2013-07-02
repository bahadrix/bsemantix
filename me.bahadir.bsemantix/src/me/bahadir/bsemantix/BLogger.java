package me.bahadir.bsemantix;

import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class BLogger {

	

	  static public void setup() throws IOException {

	    // Get the global logger to configure it
	    Logger logger = Logger.getLogger("");

	    logger.setLevel(Level.INFO);

	    logger.addHandler(new Handler() {
			
			@Override
			public void publish(LogRecord record) {
				// TODO Auto-generated method stub
				Console.println(String.format("%s:%s %s> %s", 
						record.getLoggerName(),
						record.getSourceMethodName(),
						record.getLevel().getLocalizedName(),
						record.getMessage()
						));
			}
			
			@Override
			public void flush() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void close() throws SecurityException {
				// TODO Auto-generated method stub
				
			}
		});
	  }
}
