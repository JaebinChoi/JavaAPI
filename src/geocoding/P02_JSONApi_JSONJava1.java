package geocoding;

import org.json.JSONArray;
import org.json.JSONObject;

// JSON-Java(org.json) API
public class P02_JSONApi_JSONJava1 {
	public static void main(String[] args) {
		JSONArray students = new JSONArray(); // JSON 배열
		
		JSONObject student = new JSONObject(); // JSON 객체
		student.put("name", "홍길동");
		student.put("phone", "010-1111-1111");
		student.put("address", "서울특별시");
		System.out.println(student);
		/* {"address":"서울특별시","phone":"010-1111-1111","name":"홍길동"} */
		students.put(student);
		
		student = new JSONObject();
		student.put("name", "길라임");
		student.put("phone", "010-2222-2222");
		student.put("address", "경기도");
		System.out.println(student);
		students.put(student);
		
		JSONObject object = new JSONObject();
		object.put("students", students);
		
		System.out.println(object.toString(1));
	}
}
