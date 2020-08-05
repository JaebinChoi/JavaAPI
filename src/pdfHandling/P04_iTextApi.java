package pdfHandling;

import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

/** PDF Handle : iText API => Image Insert & Scaling */
public class P04_iTextApi {
	public static void main(String[] args) {
		Document doc = new Document();

		try {
			PdfWriter.getInstance(doc, new FileOutputStream("data/P04_ImageScaling.pdf"));
			doc.open();

			String fileName = "data/google.png";
			Image image = Image.getInstance(fileName);
			doc.add(image);

			// 첫 번째 방법
			image.scaleAbsolute(200f, 200f);
			doc.add(image);

			String url = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png";
			image = Image.getInstance(url);
			
			// 두 번재 방법
			image.scalePercent(200f);
			doc.add(image);

			// 세 번째 방법
			image.scaleToFit(100f, 200f);
			doc.add(image);

			System.out.println("P04_ImageScaling 생성");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			doc.close();
		}

	}
}