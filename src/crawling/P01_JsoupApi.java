package crawling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/** Jsoup API */
public class P01_JsoupApi {
	public static void main(String[] args) {
		String url = "https://sports.news.naver.com/index.nhn";
		Document doc = null;

		try {
			doc = Jsoup.connect(url).get(); // GET 방식으로 URL의 HTML의 엘리먼트들을 가져옴
		} catch (Exception e) {
			e.printStackTrace();
		}

		Elements elements = doc.select("div.today_section");
		String title = elements.select("h3.section_title").text().substring(0, 11);
		System.out.println("==================================================");
		System.out.println(title);
		System.out.println("==================================================");
		for (Element el : elements.select("strong.title")) { // li a div.text_area strong.title
			System.out.println(el.text());
		}
		for (Element el : elements.select("li.today_item a")) { // 이동 주소 크롤링
			System.out.println(el.attr("href"));
		}
		System.out.println("==================================================");
	}
}
