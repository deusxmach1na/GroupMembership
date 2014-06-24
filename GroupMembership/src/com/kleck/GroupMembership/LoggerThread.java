package com.kleck.GroupMembership;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerThread extends Thread {
	private Logger logger; 
    private FileHandler fileHandler;  
    private String log;
    private String processId;
	
	public LoggerThread(String processId, String log) {
		this.logger = Logger.getLogger("MyLog");
		this.processId = processId;
		this.log = log;
	}
	
	public synchronized void run() {
    	
	    try {  
	    	this.fileHandler = new FileHandler(this.processId + ".log", true);
	        logger.addHandler(fileHandler);  
	        //logger.setLevel(Level.ALL);  
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fileHandler.setFormatter(formatter);  
	          
	        logger.info(processId + log);  
	          
	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	}
}
