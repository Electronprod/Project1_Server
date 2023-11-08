package electron.generators;

import java.io.File;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import electron.data.FileIteractor;
import electron.data.database;
import electron.data.settings;
/**
 * HTML generator class
 */
public class HTMLGenerator {
	private static String[] day = {"Monday", "Tuesday", "Wednesday","Thursday","Friday","Saturday","Sunday"};
	public static String generateTeacher(String teachername) {
		//Loading HTML template from file
		String index = FileIteractor.getFileLine(new File("teacher.html"));
		index=index.replaceAll("%teacher%", teachername);
		index=index.replaceAll("%monday%", database.getTeacherLessons(day[0],teachername));
		index=index.replaceAll("%tuesday%", database.getTeacherLessons(day[1],teachername));
		index=index.replaceAll("%wednesday%", database.getTeacherLessons(day[2],teachername));
		index=index.replaceAll("%thursday%", database.getTeacherLessons(day[3],teachername));
		index=index.replaceAll("%friday%", database.getTeacherLessons(day[4],teachername));
		index=index.replaceAll("%saturday%", database.getTeacherLessons(day[5],teachername));
		index=index.replaceAll("%sunday%", database.getTeacherLessons(day[6],teachername));
		return index;		
	}
	public static String generateStudent(String classname,String studentname) {
		//Loading HTML template from file
		String student = FileIteractor.getFileLine(new File("student.html"));
		student=student.replaceAll("%student%", studentname);
		String table = generateTable(classname);
		student=student.replaceAll("%body%", table);
		student=student.replace("null", "");
		return student;
	}
	public static String generateFind() {
		//Loading HTML template from file
		String table = FileIteractor.getFileLine(new File("find.html"));
		return table;
	}
	public static String generateSearchResults(String found) {
		//Loading HTML template from file
		String table = FileIteractor.getFileLine(new File("searchresults.html"));
		table=table.replaceAll("%found%", found);
		return table;
	}
	public static String generateIndex() {
		//Loading HTML template from file
		String index = FileIteractor.getFileLine(new File("index.html"));
		String gen="";
		for(int i=0;i<settings.getListClasses().size();i++) {
			gen=gen+"<div>";
			gen=gen+generateTable(settings.getListClasses().get(i));
			gen=gen+"</div>";
		}
		index=index.replaceAll("%body%",gen);
		return index;
	}
	/**
	 * Generates HTML table for class
	 * @param classname - class
	 * @return generated HTML
	 */
	private static String generateTable(String classname) {
		//Loading HTML template from file
		String table = FileIteractor.getFileLine(new File("table.html"));
		//change placeholders
		table=table.replaceAll("%classname%", classname);
		table=table.replaceAll("%monday%", compile(day[0],classname));
		table=table.replaceAll("%tuesday%", compile(day[1],classname));
		table=table.replaceAll("%wednesday%", compile(day[2],classname));
		table=table.replaceAll("%thursday%", compile(day[3],classname));
		table=table.replaceAll("%friday%", compile(day[4],classname));
		table=table.replaceAll("%saturday%", compile(day[5],classname));
		table=table.replaceAll("%sunday%", compile(day[6],classname));
		return table;
		
	}
	/**
	 * Data parser
	 * @param day - day
	 * @param classname - class
	 * @return
	 */
	private static String compile(String day,String classname) {
		JSONArray arr = database.getLessonsForClass(database.getDay(day), classname);
		if(arr==null) {
			return "<th>No data</th><th>No data</th><th>No data</th><th>No data</th>";
		}
		String lessons=null;
		for(int i=0;i<arr.size();i++) {
			lessons=lessons+generateTableElement((JSONObject) arr.get(i),i+1);
		}
		if(lessons==null) {return "<th>No data</th><th>No data</th><th>No data</th><th>No data</th>";}
		return lessons;
	}
	/**
	 * Data parser
	 * @param lesson - lesson
	 * @param num - number of lesson
	 * @return
	 */
	private static String generateTableElement(JSONObject lesson,int num) {
		String time = String.valueOf(lesson.get("time"));
		String name = String.valueOf(lesson.get("lesson"));
		String teacher = String.valueOf(lesson.get("teacher"));
		//Adding to search option
		database.allnames.add(teacher);
		String btn="<form action=\"/teacher:"+teacher+" \">\r\n"
				+ "            <button type=\"submit\">"+teacher+"</button>\r\n"
				+ "        </form>";
		return "<tr><th>"+num+"</th><th>"+time+"</th><th>"+name+"</th><th>"+btn+"</th></tr>";
	}
}
