package excelHandling;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import domain.ExcelBook;

/** 
 * 
 *	POI API
 *	Exel File -> Data Read
 *
 */

public class P01_POIApi {
	public static void main(String[] args) {
		String fileName = "data/bookList.xlsx";
		List<ExcelBook> books = new ArrayList<ExcelBook>();
		
		try(FileInputStream fis = new FileInputStream(fileName)) {
			// HSSFWorkbook workbook = new HSSFWorkbook(fis); 		// Workbook -> 엑셀 파일
			// HSSFSheet sheet = workbook.getSheetAt(0); 			// 첫번째 시트
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			Iterator<Row> rows = sheet.rowIterator(); 			// 행 단위 데이터들
			rows.next(); 										// 첫번째 row : 열 제목 -> 제거
			
			String[] domain = new String[5];					// 객체에 넣을 임시 배열
			while(rows.hasNext()) {
				XSSFRow row = (XSSFRow) rows.next(); 			// 한개의 행
				Iterator<Cell> cells = row.cellIterator();		// 한 행에서 열 단위 데이터들
				
				int i = 0;
				while(cells.hasNext()) {
					XSSFCell cell = (XSSFCell) cells.next();	// 한개의 셀(열)
					domain[i++] = cell.toString();
				}
				
				ExcelBook book = new ExcelBook(domain[0], domain[1], domain[2], domain[3], domain[4]);
				books.add(book);
			}
			
			showExcelData(books);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void showExcelData(List<ExcelBook> data) {
		for(ExcelBook book : data)
			System.out.println(book);
	}
}
