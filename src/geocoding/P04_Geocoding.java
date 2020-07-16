package geocoding;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/** 주소를 입력하여 위도, 경도를 추출 */
public class P04_Geocoding {
	public static void main(String[] args) {
		String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=";
		String clientId = "ID";
		String clientSecret = "Secret";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			System.out.print("주소를 입력하세요 >> ");
			String address = br.readLine();
			String addr = URLEncoder.encode(address, "UTF-8"); 	// URL에서 공백 == %20
																// 공백은 끝을 나타내므로 이후 데이터가 손실될 수 있음 -> 인코딩
			// String reqURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + addr;
			String reqURL = apiURL + addr; // 요청 URL
			
			// Naver Geocoding Api와 통신
			// connection에 실패하면 catch문으로
			URL url = new URL(reqURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
			con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
			
			int responseCode = con.getResponseCode(); // success : 200
			if(responseCode == 200) br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			else br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
			
			String line;
			StringBuffer response = new StringBuffer(); // JSON
			while((line = br.readLine()) != null) {
				response.append(line);
			}
			br.close();
			
			JSONTokener tokener = new JSONTokener(response.toString());
			JSONObject object = new JSONObject(tokener);
			System.out.println(object.toString(2));

			JSONArray array = object.getJSONArray("addresses");
			for(int i = 0; i < array.length(); i++) { // 해당 주소는 1개이므로 1번 반복
				JSONObject obj = (JSONObject) array.get(i);
				System.out.println("roadAddress : " + obj.get("roadAddress"));
				System.out.println("jibunAddress : " + obj.get("jibunAddress"));
				System.out.println("경도 : " + obj.get("x"));
				System.out.println("위도 : " + obj.get("y"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} // try-catch
	} // main
} // class
