package electron;

import java.io.File;
import java.io.IOException;

import electron.data.database;
import electron.data.settings;
import electron.net.site;
import electron.utils.logger;

public class Project1_Server {
	
	public static final String version = "Project1_Server - 1.1";
	
	
	public static void main(String[] args){
		//Enable or disable debug messages
		if(args.length>0 && args[0].toLowerCase().equals("-debug")) {logger.enDebug=true;}
		logger.log("Loading "+version);		
		//Resource system unit
		database.load();
		settings.load();
		if(!checkRequired()) {
			logger.error("[RESOURCE_SYSTEM]: Requered files missing. Check your installation. Program shutting down.");
			System.exit(0);
		}
	    logger.log("[RESOURCE_SYSTEM]: done.");
		//Starting site
		try {
			site.load();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error starting server: "+e.getMessage());
			System.exit(1);
		}
	}
	
	/**
	 * Check required file for site
	 * @return
	 */
	public static boolean checkRequired() {
		if(!new File("index.html").exists()) {
			return false;
		}
		if(!new File("table.html").exists()) {
			return false;
		}
		if(!new File("teacher.html").exists()) {
			return false;
		}
		if(!new File("find.html").exists()) {
			return false;
		}
		if(!new File("searchresults.html").exists()) {
			return false;
		}
		if(!new File("student.html").exists()) {
			return false;
		}
		return true;
	}
}
