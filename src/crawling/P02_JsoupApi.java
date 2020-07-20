package crawling;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/** Jsoup API */
public class P02_JsoupApi {
	public static void main(String[] args) {
		// /Ajax부터 자바스크립트 ajax 통신 부분 (날짜별 데이터)
		String url = "https://sum.su.or.kr:8888/bible/today/Ajax/Bible/BodyMatter?qt_ty=QT1";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); // 날짜 입력
		
		try {
			// URL Connect
			System.out.print("[입력 -> 년(yyyy)-월(mm)-일(dd)] : ");
			String date = br.readLine();
			url += "&Base_de=" + date + "&bibleType=1";
			Document doc = Jsoup.connect(url).post();
			
			System.out.println("===========================================");
			Element daily = doc.select(".dailybible_info").first();
			System.out.println(daily.text());
			
			Element title = doc.select(".bible_text").first();
			System.out.println(title.text());
			
			Element info = doc.select("#bibleinfo_box").first();
			System.out.println(info.text());
			
			Elements list = doc.select(".body_list li"); // ".body_list > li" 도 가능
			for(Element li : list) {
				System.out.println(li.select(".num").text());
				System.out.println(li.select(".info").text());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
