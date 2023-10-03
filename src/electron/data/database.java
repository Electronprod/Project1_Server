package electron.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import electron.utils.logger;

/**
 * Class, that controls config.json file and data in.
 * @version 1.0
 */
public class database {
	private static File confFile = new File("config.json");
	public static JSONObject info;
	
	/**
	 * Load method.
	 * Must be called before using other methods from this class.
	 */
	public static void load() {
		FileIteractor.loadFile(confFile);
	    if (FileIteractor.getFileLines(confFile.getPath().toString()).isEmpty()) {
	    	logger.error("[DATABASE]: database isn't configured. Please, use Project1_Generator application.");
	    	logger.error("Shutting down...");
	    	System.exit(0);
	    }
	    info = (JSONObject) FileIteractor.ParseJs(FileIteractor.getFileLine(confFile));
	    logger.debug("[DATABASE]: file loaded in memory.");
	}
	/**
	 * Get port from configuration method.
	 * @return port
	 */
	public static int getPort() {
		return Integer.parseInt(String.valueOf(info.get("port")));
	}
	/**
	 * Get data for day method.
	 * @param day - day name
	 * @return array of lessons for this day
	 */
	public static JSONArray getDay(String day) {
		JSONArray arr = (JSONArray) info.get(day);
    	return arr;
	}
	/**
	 * Get lessons for class method
	 * @param dayarr - day array
	 * @param classname - class name
	 * @return array of lessons for this class
	 */
	public static JSONArray getLessonsForClass(JSONArray dayarr,String classname) {
		if(dayarr==null) {return null;}
    	JSONArray toShow =  new JSONArray();
    	for(int i = 0; i < dayarr.size();i++) {
    		JSONObject obj = (JSONObject) dayarr.get(i);
    		if(String.valueOf(obj.get("class")).equals(classname)) {
    			toShow.add(obj);
    		}
    	}
    	return toShow;	
	}
	/**
	 * Get lessons for teacher method
	 * @param day - day
	 * @param teacher - teacher name
	 * @return teacher lessons for day in HTML
	 */
	public static String getTeacherLessons(String day,String teacher) {
		JSONArray arr = getDay(day);
		String emptyresponse= "<th>No data</th><th>No data</th><th>No data</th>";
		if(arr==null) {return emptyresponse;}
		String anser="";
		int matches=0;
		for(int i=0;i<arr.size();i++) {
			JSONObject obj = (JSONObject) arr.get(i);
			if(teacher.toLowerCase().equals(String.valueOf(obj.get("teacher")).toLowerCase())) {
				anser=anser+"<tr><th>"+String.valueOf(obj.get("time"))+"</th><th>"+String.valueOf(obj.get("lesson"))+"</th><th>"+String.valueOf(obj.get("class"))+"</th></tr>";
				matches++;
			}
		}
		if(matches!=0)
			return anser;
		
		return emptyresponse;
	}
}
