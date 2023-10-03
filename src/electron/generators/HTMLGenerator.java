package electron.generators;

import java.io.File;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import electron.data.FileIteractor;
import electron.data.database;
import electron.data.settings;

public class HTMLGenerator {
	private static String[] day = {"Monday", "Tuesday", "Wednesday","Thursday","Friday","Saturday","Sunday"};
	public static String generateTeacher(String teachername) {
		//Подгружаем html из файлов
		String index = FileIteractor.getFileLine(new File("teacher.html"));
		index=index.replaceAll("%teacher%", teachername);
		index=index.replace("%monday%", database.getTeacherLessons(day[0],teachername));
		index=index.replace("%tuesday%", database.getTeacherLessons(day[1],teachername));
		index=index.replace("%wednesday%", database.getTeacherLessons(day[2],teachername));
		index=index.replace("%thursday%", database.getTeacherLessons(day[3],teachername));
		index=index.replace("%friday%", database.getTeacherLessons(day[4],teachername));
		index=index.replace("%saturday%", database.getTeacherLessons(day[5],teachername));
		index=index.replace("%sunday%", database.getTeacherLessons(day[6],teachername));
		return index;		
	}
	
	public static String generateIndex() {
		//Подгружаем html из файлов
		String index = FileIteractor.getFileLine(new File("index.html"));
		String gen="";
		for(int i=0;i<settings.getListClasses().size();i++) {
			gen=gen+"<div>";
			gen=gen+generateTable(settings.getListClasses().get(i));
			gen=gen+"</div>";
		}
		index=index.replace("%body%",gen);
		return index;
	}
	private static String generateTable(String classname) {
		//Шаблон таблицы
		String table = FileIteractor.getFileLine(new File("table.html"));
		//Заменяем плейсхолдеры
		table=table.replace("%classname%", classname);
		table=table.replace("%monday%", compile(day[0],classname));
		table=table.replace("%tuesday%", compile(day[1],classname));
		table=table.replace("%wednesday%", compile(day[2],classname));
		table=table.replace("%thursday%", compile(day[3],classname));
		table=table.replace("%friday%", compile(day[4],classname));
		table=table.replace("%saturday%", compile(day[5],classname));
		table=table.replace("%sunday%", compile(day[6],classname));
		return table;
		
	}
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
	private static String generateTableElement(JSONObject lesson,int num) {
		String time = String.valueOf(lesson.get("time"));
		String name = String.valueOf(lesson.get("lesson"));
		String teacher = String.valueOf(lesson.get("teacher"));
		String btn="<form action=\"/teacher:"+teacher+" \">\r\n"
				+ "            <button type=\"submit\">"+teacher+"</button>\r\n"
				+ "        </form>";
		return "<tr><th>"+num+"</th><th>"+time+"</th><th>"+name+"</th><th>"+btn+"</th></tr>";
	}
}
