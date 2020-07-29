package excelHandling;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import domain.ExcelBook;

/** Naver Book Search API + Jsoup API(Crawling) */
public class P04_NaverSearchApi_JsoupApi {
	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			System.out.print("책 제목 >> ");
			String title = br.readLine();
			
			System.out.print("저자 >> ");
			String author = br.readLine();
			
			System.out.print("출판사 >> ");
			String company = br.readLine();
			
			ExcelBook book = new ExcelBook(title, author, company);
			getIsbnImage(book);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void getIsbnImage(ExcelBook book) {
		try {
			String openApi = "https://openapi.naver.com/v1/search/book_adv.xml?d_titl="
					+ URLEncoder.encode(book.getTitle(), "UTF-8")
					+ "&d_auth=" + URLEncoder.encode(book.getAuthor(), "UTF-8")
					+ "&d_publ=" + URLEncoder.encode(book.getCompany(), "UTF-8");
			
			URL url = new URL(openApi);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Naver-Client-Id", "v6DqXM6ix5K4F6f7c35m");
			con.setRequestProperty("X-Naver-Client-Secret", "NJDTpmttVy");
			int responseCode = con.getResponseCode();
			
			BufferedReader br = null;
			// 통신 성공 => 통신 결과값 가져오기 (con.getInputStream())
			if(responseCode == 200) br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			else br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			
			String inputLine;
			// StringBuffer response = new StringBuffer();
			StringBuilder response = new StringBuilder();
			
			// BufferedReader에 있는 데이터를 1줄씩 읽기 (XML 형태)
			while((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();
			System.out.println(response.toString());
			
			// XML에서 isbn, image 크롤링
			Document doc = Jsoup.parse(response.toString());
			System.out.println(doc);
			
			Element total = doc.select("total").first();
			System.out.println(total.text());
			
			if(!total.text().equals("0")) {
				// ISBN 크롤링
				Element el_isbn = doc.select("isbn").first();
				String result_isbn = el_isbn.text();
				System.out.println(result_isbn);
				
				String isbn = result_isbn.split(" ")[1];
				book.setIsbn(isbn);
				
				// Image Name 크롤링
				String doc_string = doc.toString();
				String img_tag = doc_string.substring(doc_string.indexOf("<img>") + 5);
				System.out.println(img_tag); // http~~
				
				String imgURL = img_tag.substring(0, img_tag.indexOf("?"));
				System.out.println(imgURL);
				
				String imgName = imgURL.substring(imgURL.lastIndexOf("/") + 1);
				System.out.println(imgName);
				
				book.setImgUrl(imgName);
				System.out.println(book);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
