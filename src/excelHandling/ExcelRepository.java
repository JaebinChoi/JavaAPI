package excelHandling;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import crawling.DownloadBroker;
import domain.ExcelBook;

public class ExcelRepository {
	private List<ExcelBook> list;
	private XSSFWorkbook workbook;
	
	public ExcelRepository() {
		list = new ArrayList<ExcelBook>();
		workbook = new XSSFWorkbook();
	}
	
	public void excel_input() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			XSSFSheet firstSheet = workbook.createSheet("Book Sheet");	// 엑셀 시트 생성
			XSSFRow rowA = firstSheet.createRow(0); 					// 행 생성
			XSSFCell cellA = rowA.createCell(0);						// 열 생성
			cellA.setCellValue(new XSSFRichTextString("책 제목"));
			XSSFCell cellB = rowA.createCell(1);
			cellB.setCellValue(new XSSFRichTextString("저자"));
			XSSFCell cellC = rowA.createCell(2);
			cellC.setCellValue(new XSSFRichTextString("출판사"));
			XSSFCell cellD = rowA.createCell(3);
			cellD.setCellValue(new XSSFRichTextString("ISBN"));
			XSSFCell cellE = rowA.createCell(4);
			cellE.setCellValue(new XSSFRichTextString("이미지 이름"));
			XSSFCell cellF = rowA.createCell(5);
			cellF.setCellValue(new XSSFRichTextString("이미지"));
			
			int idx = 1;
			while(true) {
				System.out.print("책 제목 >> ");
				String title = br.readLine();
				System.out.print("저자 >> ");
				String author = br.readLine();
				System.out.print("출판사 >> ");
				String company = br.readLine();
				
				XSSFRow row = firstSheet.createRow(idx); // 행 입력 : 0(제목) -> 1부터
				XSSFCell cellTitle = row.createCell(0);	 // 열 입력
				cellTitle.setCellValue(new XSSFRichTextString(title));
				XSSFCell cellAuthor = row.createCell(1);
				cellAuthor.setCellValue(new XSSFRichTextString(author));
				XSSFCell cellCompany = row.createCell(2);
				cellCompany.setCellValue(new XSSFRichTextString(company));
				idx++;
				
				ExcelBook book = new ExcelBook(title, author, company);
				// ISBN, Image Search
				book = naver_search(book);
				list.add(book);
				System.out.println("입력 계속(Y) / 입력 종료(N) >> ");
				String key = br.readLine();
				if(key.equals("N")) break;
			}
			System.out.println("데이터 추출 중입니다.");
			excel_save();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ExcelBook naver_search(ExcelBook book) throws Exception {
		try {
			String URL_SEARCH = "https://openapi.naver.com/v1/search/book_adv.xml?d_titl="
					+ URLEncoder.encode(book.getTitle(), "UTF-8")
					+ "&d_auth=" + URLEncoder.encode(book.getAuthor(), "UTF-8")
					+ "&d_publ=" + URLEncoder.encode(book.getCompany(), "UTF-8");
			
			URL url = new URL(URL_SEARCH);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Naver-Client-Id", "ClientID");
			con.setRequestProperty("X-Naver-Client-Secret", "ClientSecret");
			int responseCode = con.getResponseCode();
			
			BufferedReader br;
			// 통신 성공 => 통신 결과값 가져오기 (con.getInputStream())
			if(responseCode == 200) br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			else br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			
			String inputLine;
			StringBuffer response = new StringBuffer();
			while((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();
			
			// XML에서 ISBN 크롤링
			Document doc = Jsoup.parse(response.toString());
			Element isbn = doc.select("isbn").first();
			System.out.println(isbn.text());
			book.setIsbn(isbn.text().split(" ")[1]);
			
			// Image Name 크롤링
			String img = doc.toString();
			String img_tag = img.substring(img.indexOf("<img>") + 5);
			
			img = img_tag.substring(0, img_tag.indexOf("?")); // Image URL
			System.out.println(img);
			
			String imgName = img.substring(img.lastIndexOf("/") + 1); // Image Name
			System.out.println(imgName);
			book.setImgUrl(imgName);
			System.out.println(book);
			
			// DownloadBroker - Image Download
			Runnable download = new DownloadBroker(img, imgName);
			Thread thread = new Thread(download);
			thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return book;
	}
	
	private void excel_save() {
		try {
			XSSFSheet sheet = workbook.getSheetAt(0);
			if(workbook != null && sheet != null) {
				Iterator<Row> rows = sheet.rowIterator();
				rows.next(); // 첫번째 row : 제목 -> 제거
				
				int idx = 0;
				while(rows.hasNext()) {
					XSSFRow row = (XSSFRow) rows.next();
					
					XSSFCell cell = row.createCell(3);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(list.get(idx).getIsbn());
					
					cell = row.createCell(4);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(list.get(idx).getImgUrl());
					
					InputStream inputStream = new FileInputStream(list.get(idx).getImgUrl()); // 이미지 읽기
					byte[] bytes = IOUtils.toByteArray(inputStream); // 이미지 파일을 byte 단위로 저장
					int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG); // 이미지를 엑셀에 추가 (메모리에)
					inputStream.close();
					
					// 엑셀에 이미지를 드로잉으로 그리기 위한 작업
					CreationHelper helper = workbook.getCreationHelper();
					Drawing drawing = sheet.createDrawingPatriarch();
					ClientAnchor anchor = helper.createClientAnchor();
					
					// 이미지 시작 좌표
					anchor.setRow1(idx + 1);
					anchor.setCol1(5);
					
					// 이미지 끝 좌표
					anchor.setRow2(idx + 2);
					anchor.setCol2(6);
					
					Picture picture = drawing.createPicture(anchor, pictureIdx);
					
					// 이미지 출력할 셀 선택
					Cell cellImg = row.createCell(5);
					
					// 셀 넓이 지정 (넓이 1 => 1 / 256)
					// setColumnWidth(컬럼 위치, 넓이)
					int width = 20 * 256;
					sheet.setColumnWidth(5, width);
					
					// 셀 높이 지정 (높이 1 => 1 / 20)
					// setHright(short 높이)
					short height = 120 * 20;
					cell.getRow().setHeight(height);
					
					idx++;
				}
				
				// 파일에 쓰기
				FileOutputStream fos = new FileOutputStream("data/P05.xlsx");
				workbook.write(fos);
				fos.close();
				
				System.out.println("ISBN, ImageURL 저장 성공!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
