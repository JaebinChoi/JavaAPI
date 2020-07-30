package excelHandling;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/** 
 * P04의 연장선
 * NaverSearchApi + JsoupApi
 * 추가 >> POI API => Excel Handling (ExcelRepository)
*/
public class P05_NaverSearchApi_JsoupApi_POIApi {
	public static void main(String[] args) {
		ExcelRepository repository = new ExcelRepository();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			System.out.print("입력처리(I)/종료(E) >> ");
			String choice = br.readLine();
			switch(choice) {
			case "I":
				repository.excel_input();
				System.exit(0);
				break;
			case "E":
				System.out.println("프로그램 종료");
				System.exit(0);
				break;
			default:
				System.out.println("I or E input");	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
