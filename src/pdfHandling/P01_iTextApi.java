package PDFHandling;

import java.io.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/** PDF Handle : iText API => Table */
public class P01_iTextApi {
	public static void main(String[] args) {
		String[] title = new String[] { "제목", "저자", "출판사", "이미지 URL" };
		String[][] rows = new String[][] {
				{ "물리법칙의 이해", "리처드 파인먼", "해나무", "https://bookthumb-phinf.pstatic.net/cover/100/365/10036542.jpg" },
				{ "Java의 정석", "남궁성", "도우출판", "https://bookthumb-phinf.pstatic.net/cover/100/365/10036542.jpg" },
				{ "리눅스 프로그래밍", "창병모", "생능출판", "https://bookthumb-phinf.pstatic.net/cover/100/365/10036542.jpg" } };
		
		Document doc = new Document(PageSize.A4);
		
		try {
			// 사용할 문서, 저장할 파일,경로
			PdfWriter.getInstance(doc, new FileOutputStream(new File("data/P01_book.pdf")));
			doc.open();
			
			// 한글 폰트 (TTF 확장자 사용)
			BaseFont baseFont = BaseFont.createFont("data/H2MKPB.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			Font fontTitle = new Font(baseFont, 12);
			Font fontRows = new Font(baseFont, 10);

			// 테이블 생성
			PdfPTable table = new PdfPTable(title.length); // 테이블 열 개수
			table.setWidthPercentage(100); // 테이블 너비 설정

			// 각 열 너비 설정
			float[] colwidth = new float[] { 20f, 15f, 15f, 30f };
			table.setWidths(colwidth);

			for (String header : title) {
				PdfPCell cell = new PdfPCell(); 					// 하나의 셀
				cell.setHorizontalAlignment(Element.ALIGN_CENTER); 	// 가운데 정렬
				cell.setPadding(10); 								// 여백 설정
				cell.setGrayFill(0.9f); 							// 회색 설정
				cell.setPhrase(new Phrase(header, fontTitle)); 		// 셀(PdfCell) 안의 내용(Phrase)
				table.addCell(cell);								// 테이블에 부착
			}
			table.completeRow(); // 하나의 row 끝

			// 위의 과정 반복
			for (String[] row : rows) {
				for (String data : row) {
					Phrase phrase = new Phrase(data, fontRows);
					PdfPCell cell = new PdfPCell(phrase);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setPaddingTop(20);
					cell.setPaddingRight(30);
					cell.setPaddingBottom(20);
					cell.setPaddingLeft(30);
					table.addCell(cell);
				}
				table.completeRow();
			}

			// 셀 합치기
			PdfPCell cell4 = new PdfPCell(new Phrase("Cell 5"));
			cell4.setColspan(2);

			PdfPCell cell5 = new PdfPCell(new Phrase("Cell 6"));
			cell5.setColspan(2);

			table.addCell(cell4);
			table.addCell(cell5);

			doc.addTitle("PDF Table");
			doc.add(table);
			System.out.println("P01_Table 생성 완료");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			doc.close();
		}
	}
}