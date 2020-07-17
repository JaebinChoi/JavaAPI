package geocoding;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import javax.swing.ImageIcon;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import domain.Address;

public class NaverMap implements ActionListener {
	private String clientId = "clientId";
	private String clientSecret = "clientSercret";
	
	P06_Geocoding_GUI naverMap;
	
	public NaverMap(P06_Geocoding_GUI naverMap) {
		this.naverMap = naverMap;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Address domain = null;
		
		try {
			String text = naverMap.address.getText(); // GUI에서 입력한 주소
			String address = URLEncoder.encode(text, "UTF-8"); // 입력 공백 -> 문자 처리 필수
			String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + address;
			
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
			con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
			
			int responseCode = con.getResponseCode();
			BufferedReader br;
			if(responseCode == 200) br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			else br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			
			String inputLine; // 임시 저장소
			StringBuffer response = new StringBuffer(); // 결과값
			while((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();
			
			// JSON API
			JSONTokener tokener = new JSONTokener(response.toString());
			JSONObject object = new JSONObject(tokener);
			System.out.println(object);
			
			JSONArray array = object.getJSONArray("addresses"); // 1개
			for(int i = 0; i < array.length(); i++) { // 1번 반복
				JSONObject obj = array.getJSONObject(i);
				
				// Address 객체에 Data 저장
				domain = new Address();
				domain.setRoadAddress(obj.getString("roadAddress"));
				domain.setJibunAddress(obj.getString("jibunAddress"));
				domain.setX(obj.getString("x"));
				domain.setY(obj.getString("y"));
				System.out.println(domain);
			}
			map_service(domain);
		} catch (Exception except) {
			except.printStackTrace();
		}
	}

	private void map_service(Address domain) {
		String URL_STATICMAP = "https://naveropenapi.apigw.ntruss.com/map-static/v2/raster?";
		
		try {
			URL_STATICMAP += "center=" + domain.getX() + "," + domain.getY();
			URL_STATICMAP += "&level=16&w=700&h=500";
			String pos = URLEncoder.encode(domain.getX() + " " + domain.getY(), "UTF-8");
			URL_STATICMAP += "&markers=type:t|size:mid|pos:" + pos + "|label:" + URLEncoder.encode(domain.getRoadAddress(), "UTF-8");
			
			URL url = new URL(URL_STATICMAP);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
			con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
			
			int responseCode = con.getResponseCode();
			BufferedReader br;
			
			if(responseCode == 200) { // 통신 성공
				InputStream is = con.getInputStream(); // 이미지 -> Stream으로 받아옴
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
				
				// GUI 이미지 아이콘 생성
				ImageIcon img = new ImageIcon(file.getName());
				naverMap.imageLabel.setIcon(img);
				
				// 결과 부분
				naverMap.resAddress.setText(domain.getRoadAddress());
				naverMap.jibunAddress.setText(domain.getJibunAddress());
				naverMap.resX.setText(domain.getX());
				naverMap.resY.setText(domain.getY());
			} else { // Error
				System.out.println(responseCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
