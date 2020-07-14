package geocoding;

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class P03_JSONApi_JSONJava2 {
	public static void main(String[] args) {
		String src = "../info.json"; // source file
		// IO -> Stream(스트림) 객체 필요
		InputStream is = P03_JSONApi_JSONJava2.class.getResourceAsStream(src); // 현재 클래스가 있는 곳에서 src를 가져온다
		
		if(is == null) throw new NullPointerException("Cannot find resource file");
		
		// JSON 파일의 String 형태의 데이터를 JSON 형태의 구조로 불러옴
		JSONTokener tokener = new JSONTokener(is);
		JSONObject object = new JSONObject(tokener);
		JSONArray students = object.getJSONArray("students");
		
		for(int i = 0; i < students.length(); i++) {
			// JSONObject student = (JSONObject) students.get(i);
			JSONObject student = students.getJSONObject(i);
			
			System.out.print(student.get("name") + "\t");
			System.out.print(student.get("address") + "\t");
			System.out.println(student.get("phone"));
			// System.out.println(students.get(i));
		}
	}
}
