package geocoding;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ReverseGeocoding {

	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String clientId = "ID";
		String clientSecret = "Secret";
		
		String lat = "37.361559"; // 위도
		String lng = "126.9794443"; // 경도
		
		String apiURL = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?coords=";
		try {
			apiURL += URLEncoder.encode(lng, "UTF-8") + "," + URLEncoder.encode(lat, "UTF-8") + "&output=json&orders=addr,admcode,roadaddr";
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
			con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
			
			int responseCode = con.getResponseCode();
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
			
			// 0 : 반환 o, 3 : no result
			int status = (int) object.getJSONObject("status").get("code");
			System.out.println(status);
			
			if(status == 0) {
				JSONArray array = object.getJSONArray("results");
				JSONObject addr = object.getJSONArray("results").getJSONObject(0);
				
				JSONObject region = addr.getJSONObject("region");
				String regionDoSi = region.getJSONObject("area1").getString("name");
				String regionGu = region.getJSONObject("area2").getString("name");
				String regionDong = region.getJSONObject("area3").getString("name");
				
				String address = regionDoSi + " " + regionGu + " " + regionDong;
				System.out.println(address);
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
}
