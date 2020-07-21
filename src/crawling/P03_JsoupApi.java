package crawling;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/** Jsoup API */
/** P02_JsoupApi의 연장선 */
public class P03_JsoupApi {
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
			
			// MP3 리소스 다운로드
//			Element audio = doc.select("source").first();
//			String download_path = audio.attr("src").trim();
//			System.out.println(download_path);
//			String fileName = download_path.substring(download_path.lastIndexOf('/') + 1);
			
			// Image 리소스 다운로드
			Element image = doc.select(".img > img").first();
			String download_path = "https://sum.su.or.kr:8888" + image.attr("src").trim(); // 경로
			System.out.println(download_path);
			String fileName = download_path.substring(download_path.lastIndexOf('/') + 1); // 파일 이름
			
			// 다운로드는 DownloadBroker에서
			Runnable runnable = new DownloadBroker(download_path, fileName);
			Thread download = new Thread(runnable);
			download.start();
			for (int i = 0; i < 10; i++) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("" + (i + 1));
			}
			System.out.println();
			System.out.println("===============================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
