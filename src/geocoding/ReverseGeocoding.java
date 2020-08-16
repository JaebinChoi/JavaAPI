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
		
		String lat = "37.5012308333"; // 위도
		String lng = "127.040511167"; // 경도
		
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
			
			JSONArray array = object.getJSONArray("results");
			JSONObject addr = object.getJSONArray("results").getJSONObject(2);
			String regionRo = addr.getJSONObject("land").getString("name");
			
			JSONObject region = addr.getJSONObject("region");
			String regionDo = region.getJSONObject("area1").getString("name");
			String regionSiGu = region.getJSONObject("area2").getString("name");
			
			String address = regionDo + " " + regionSiGu + " " + regionRo;
			System.out.println(address);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
