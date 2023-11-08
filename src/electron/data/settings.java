package electron.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import electron.utils.logger;

public class settings {
	private static File clFile = new File("settings.txt");
	private static List<String> ls = null;
	/**
	 * Load method. Must be called before using other functions from this class.
	 */
	public static void load() {
	    FileIteractor.loadFile(clFile);
	    if (FileIteractor.getFileLines(clFile.getPath().toString()).isEmpty()) {
	    	logger.error("Settings.txt file is empty! Fill it with data from Project1_Generator's setting.txt. Program shutting down...");
	    	System.exit(0);
	    }
	}
	/**
	 * Get list of classes
	 * @return classes String list
	 */
	public static List<String> getListClasses(){
		if(ls == null) {
		List<String> strs = FileIteractor.getFileLines(clFile.getPath());
		Collections.sort(strs);
		ls = strs;
		return ls;
		}else {
			return ls;
		}
	}
}
