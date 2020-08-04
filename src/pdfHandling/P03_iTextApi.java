package pdfHandling;

import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

/** PDF Handle : iText API => Image Insert */
public class P03_iTextApi {
	public static void main(String[] args) {
		Document doc = new Document();

		try {
			PdfWriter.getInstance(doc, new FileOutputStream("data/P03_Image.pdf"));
			doc.open();

			String fileName = "data/pic.jpg";
			Image image = Image.getInstance(fileName); // 이미지 생성
			doc.add(image);

			String url = "https://t1.daumcdn.net/thumb/R720x0/?fname=http://t1.daumcdn.net/brunch/service/user/iDz/image/y3pFBIgnTW_lWufDwO3dPRR78W4.jpg";
			image = Image.getInstance(url);
			doc.add(image);

			System.out.println("P03_Image.pdf 생성");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			doc.close();
		}
	}
}