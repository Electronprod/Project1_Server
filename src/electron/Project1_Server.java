package electron;

import java.io.IOException;

import electron.data.database;
import electron.data.settings;
import electron.net.site;
import electron.utils.Other;
import electron.utils.logger;

public class Project1_Server {
	//version 1.0
	public static final String version = "Project1_Server - 1.1";
	public static void main(String[] args){
		//Enable or Disable debug messages
		if(args.length>0 && args[0].toLowerCase().equals("-debug")) {logger.enDebug=true;}
		logger.log("Loading "+version);		database.load();
		settings.load();
		if(!Other.htmlCheck()) {
			logger.error("Requered files missing. Check your installation. Program shutting down.");
			System.exit(0);
		}
		try {
			site.load();
		} catch (IOException e) {
			logger.error("Error starting server: "+e.getMessage());
			e.printStackTrace();
			logger.log("Shutting down...");
			System.exit(1);
		}
		logger.log("Loading done.");
	}
	
}
