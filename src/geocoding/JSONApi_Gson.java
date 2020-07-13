package geocoding;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken; // reflect : 자바 고급 기법(역추적) => 배워볼 것

import kr.jbchoi.domain.Book;

// Gson API
// JSON Data 분석 : https://jsoneditoronline.org
public class JSONApi_Gson {
	public static void main(String[] args) {
		// Object(Book) -> JSON(String) 
		Book book = new Book("자바", 21000, "AAA", 670);
		Gson g = new Gson();
		String json = g.toJson(book);
		System.out.println(json);
		/* {"title":"자바","price":21000,"company":"AAA","page":670} */
		
		// JSON(String) -> Object(Book)
		Book domain = g.fromJson(json, Book.class);
		System.out.println(domain);
		/* BookDTO [title=자바, price=21000, company=AAA, page=670] */
		System.out.println(domain.getTitle() + "\t" + domain.getPrice());
		/* 자바	21000 */
		
		// Object(List<Book>) -> JSON(String) : [{ }, { }, ...]
		List<Book> list = new ArrayList<Book>();
		list.add(new Book("자바", 21000, "AAA1", 670));
		list.add(new Book("파이썬", 31000, "BBB", 570));
		list.add(new Book("자바스크립트", 41000, "CCC", 470));
		
		String lstJson = g.toJson(list);
		System.out.println(lstJson);
		
		// JSON(String) -> Object(List<Book>)
		// fromJson : 하나의 Type 필요 -> List, Book 2개의 Type 존재
		// TypeToken -> List<Book> 타입 정보를 내부적으로 받는다
		List<Book> lstDomain = g.fromJson(lstJson, new TypeToken<List<Book>>(){}.getType()); 
		for(Book vo : lstDomain) System.out.println(vo);
		/* Book [title=자바, price=21000, company=AAA, page=670]
		   Book [title=파이썬, price=31000, company=BBB, page=570]
		   Book [title=자바스크립트, price=41000, company=CCC, page=470] */
	}
}
