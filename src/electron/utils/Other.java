package electron.utils;

import java.io.File;

public class Other {
	public static boolean htmlCheck() {
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
