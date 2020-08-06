package pdfHandling;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import domain.ExcelBook;

/** PDF Handle : iText API => Excel Read, PDF Write */
public class P05_iTextApi {
	public static void main(String[] args) {
		String fileName = "data/P05.xlsx";
		List<ExcelBook> books = new ArrayList<ExcelBook>();

		try (FileInputStream fis = new FileInputStream(fileName)) {
			// HSSFWorkbook workbook = new HSSFWorkbook(fis); 	// Workbook -> 엑셀 파일
			// HSSFSheet sheet = workbook.getSheetAt(0); 		// 첫번째 시트
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rows = sheet.rowIterator(); 		// 행 단위 데이터들
			rows.next(); 									// 첫번째 row : 열 제목 -> 제거

			String[] domain = new String[5]; 				// 객체에 넣을 임시 배열
			while (rows.hasNext()) {
				XSSFRow row = (XSSFRow) rows.next(); 		// 한개의 행
				Iterator<Cell> cells = row.cellIterator(); 	// 한 행에서 열 단위 데이터들

				int i = 0;
				while (cells.hasNext()) {
					XSSFCell cell = (XSSFCell) cells.next(); // 한개의 셀(열)
					domain[i++] = cell.toString();
					if(i == 5) break;
				}

				ExcelBook book = new ExcelBook(domain[0], domain[1], domain[2], domain[3], domain[4]);
				books.add(book);
			}

			pdf_maker(books);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void pdf_maker(List<ExcelBook> books) {
		String[] headers = new String[] { "제목", "저자", "출판사", "이미지" };
		Document doc = new Document(PageSize.A4);

		try {
			PdfWriter.getInstance(doc, new FileOutputStream(new File("data/P05_BookList.pdf")));
			doc.open();
			
			// 폰트 생성
			BaseFont baseFont = BaseFont.createFont("data/H2MKPB.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			Font fontHeader = new Font(baseFont, 12);
			Font fontRows = new Font(baseFont, 10);

			PdfPTable table = new PdfPTable(headers.length); // 열 개수
			for (String header : headers) {
				PdfPCell cell = new PdfPCell();
				cell.setGrayFill(0.9f);
				cell.setPhrase(new Phrase(header.toUpperCase(), fontHeader));
				table.addCell(cell);
			}
			table.completeRow();

			for (ExcelBook book : books) {
				Phrase phrase = new Phrase(book.getTitle(), fontRows);
				table.addCell(new PdfPCell(phrase));

				phrase = new Phrase(book.getAuthor(), fontRows);
				table.addCell(new PdfPCell(phrase));

				phrase = new Phrase(book.getCompany(), fontRows);
				table.addCell(new PdfPCell(phrase));

//				Image image = Image.getInstance(book.getImgUrl());
//				table.addCell(image);

				table.completeRow();
			}
			
			doc.addTitle("PDF Table");
			doc.add(table);
			
			System.out.println("P05_BookList.pdf 생성");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			doc.close();
		}
	}
}
