package electron.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
