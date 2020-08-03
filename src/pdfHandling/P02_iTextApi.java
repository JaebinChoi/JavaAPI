package pdfHandling;

import java.io.FileOutputStream;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

/** PDF Handle : iText API => Paragraph */
public class P02_iTextApi {
	public static void main(String[] args) {
		Document doc = new Document();
		try {
			FileOutputStream fos = new FileOutputStream("data/P02_paragraph.pdf");
			PdfWriter.getInstance(doc, fos);
			doc.open();

			String content = "My name is jaebin Choi. Nict to meet you! "; 	// 내용
			Paragraph par1 = new Paragraph(32); 							// 문단
			par1.setSpacingBefore(50); 										// 해당 문단 전 여백
			par1.setSpacingAfter(50); 										// 해당 문단 후 여백

			for (int i = 0; i < 20; i++) {
				Chunk chunk = new Chunk(content); 							// 문장의 저장소 느낌
				par1.add(chunk);
			}
			doc.add(par1);

			Paragraph par2 = new Paragraph();
			for (int i = 0; i < 10; i++) {
				par2.add(new Chunk("Here is 2nd paragraph!! "));
			}
			doc.add(par2);

			System.out.println("P02_paragrap 생성");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			doc.close();
		}
	}
}