package geocoding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/** 주소를 입력하여 위도, 경도를 추출 및 지도 이미지 생성 */
public class P05_Geocoding {
	static String clientId = "ID";
	static String clientSecret = "Secret";
	
	public static void main(String[] args) {
		String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=";
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
			
			String x = ""; String y = ""; String z = ""; // 추가 부분 : 경도, 위도, 주소
			JSONArray array = object.getJSONArray("addresses");
			for(int i = 0; i < array.length(); i++) { // 해당 주소는 1개이므로 1번 반복
				JSONObject obj = (JSONObject) array.get(i);
				System.out.println("roadAddress : " + obj.get("roadAddress"));
				System.out.println("jibunAddress : " + obj.get("jibunAddress"));
				System.out.println("경도 : " + obj.get("x"));
				System.out.println("위도 : " + obj.get("y"));
				// 추가 부분
				x = (String) obj.get("x");
				y = obj.getString("y");
				z = obj.getString("roadAddress");
			}
			
			// 추가 부분
			map_service(x, y, z);
		} catch (Exception e) {
			e.printStackTrace();
		} // try-catch
	} // main

	/**
	 *  
	 * 지도 이미지 생성 메소드
	 * point_x : 경도
	 * point_y : 위도
	 * address : 도로명 주소
	 * 
	 *  */
	private static void map_service(String point_x, String point_y, String address) {
		String URL_STATICMAP = "https://naveropenapi.apigw.ntruss.com/map-static/v2/raster?";
		try {
			String pos = URLEncoder.encode(point_x + " " + point_y, "UTF-8"); 	// 인코딩 처리 필수 : 공백 때문에
																				// 또는 String에 공백을 %20으로(?)
			String url = URL_STATICMAP;
			url += "center=" + point_x + "," + point_y;
			url += "&level=16&w=700&h=500";
			url += "&markers=type:t|size:mid|pos:" + pos + "|label:" + URLEncoder.encode(address, "UTF-8");
			
			// Naver Static Map Api와 통신
			URL u = new URL(url);
			HttpURLConnection con = (HttpURLConnection) u.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
			con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
			
			int responseCode = con.getResponseCode();
			BufferedReader br;
			
			if(responseCode == 200) { // 통신 성공
				InputStream is = con.getInputStream();
				int read = 0;
				byte[] bytes = new byte[1024];

				// 랜덤한 이름으로 파일 생성
				String fileName = Long.valueOf(new Date().getTime()).toString();
				File file = new File(fileName + ".jpg");
				file.createNewFile();
				OutputStream outputStream = new FileOutputStream(file);
				while((read = is.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read); // 0부터 bytes 크기만큼 read한 것을 쓴다
				}
				is.close();
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while((inputLine = br.readLine()) != null) {
					response.append(inputLine);
				}
				br.close();
				System.out.println(response.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
} // class
