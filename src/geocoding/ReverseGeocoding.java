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
		
		String lat = "37.404458"; // 위도
		String lng = "127.115956"; // 경도
		
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
				JSONObject addr = null; JSONObject region = null;
				String address; String regionRo = ""; String regionNum1 = ""; String regionNum2 = "";

				int size = object.getJSONArray("results").length();
				if(size == 3) { // 도로명 주소가 있을 경우
					addr = object.getJSONArray("results").getJSONObject(2);
					
					regionRo = addr.getJSONObject("land").getString("name");
					regionNum1 = addr.getJSONObject("land").getString("number1");
					
					if(addr.getJSONObject("land").getString("number2").length() > 0)
						regionNum2 = addr.getJSONObject("land").getString("number2");
				} else addr = object.getJSONArray("results").getJSONObject(1); // 도로명 주소가 없을 경우
				region = addr.getJSONObject("region");
				
				String regionDo = region.getJSONObject("area1").getString("name");
				String regionSiGu = region.getJSONObject("area2").getString("name");
				String regionDong = region.getJSONObject("area3").getString("name");
				
				address = regionDo + " " + regionSiGu + " " + regionDong + " " + regionRo + " " + regionNum1 + (regionNum2.length() > 0 ? ("-" + regionNum2) : "");
				address = address.trim();
				System.out.println(address);
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
}
