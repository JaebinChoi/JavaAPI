package excelHandling;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/** Excel에 Image 데이터 입력 및 파일 저장 */
public class P02_POIApi {
	public static void main(String[] args) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();									// 엑셀 생성
			Sheet sheet = workbook.createSheet("My Sample Excel"); 						// sheet 생성
			InputStream is = new FileInputStream("data/pic.jpg"); 						// 이미지 읽기
			byte[] bytes = IOUtils.toByteArray(is); 									// 이미지 파일을 byte 단위로 저장
			int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);	// 이미지를 엑셀에 추가 (메모리에)
			is.close();
			
			// 엑셀에 이미지를 드로잉으로 그리기 위한 작업
			CreationHelper helper = workbook.getCreationHelper();
			Drawing drawing = sheet.createDrawingPatriarch();
			ClientAnchor anchor = helper.createClientAnchor();
			
			// 이미지 시작 좌표
			anchor.setRow1(2);
			anchor.setCol1(1);
			
			// 이미지 끝 좌표
			anchor.setRow2(3);
			anchor.setCol2(2);
			
			drawing.createPicture(anchor, pictureIdx); // 필요하면 Picture 타입으로 선언 가능
			// Picture picture = drawing.createPicture(anchor, pictureIdx);
			
			// 이미지 출력할 셀 선택
			Cell cell = sheet.createRow(2).createCell(1);
			
			// 셀 넓이 지정 (넓이 1 => 1 / 256)
			// setColumnWidth(컬럼 위치, 넓이)
			int width = 20 * 256;
			sheet.setColumnWidth(1, width);
			
			// 셀 높이 지정 (높이 1 => 1 / 20)
			// setHright(short 높이)
			short height = 120 * 20;
			cell.getRow().setHeight(height);
			
			// 파일에 쓰기
			FileOutputStream fos = new FileOutputStream("data/myFile.xlsx");
			workbook.write(fos);
			fos.close();
			
			System.out.println("이미지 생성 성공!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
