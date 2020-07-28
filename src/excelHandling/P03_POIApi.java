package excelHandling;

import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/** Excel에 저장된 Data의 Type 추출  */
public class P03_POIApi {
	public static void main(String[] args) {
		String fileName = "data/cellDataType.xlsx";
		
		try(FileInputStream fis = new FileInputStream(fileName)) {
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0); // 시트 설정
			Iterator<Row> rows = sheet.rowIterator(); // 행 추출
			
			while(rows.hasNext()) {
				XSSFRow row = (XSSFRow) rows.next(); // 행 1개
				// Row row = rows.next();
				Iterator<Cell> cells = row.cellIterator(); // 열 추출
				
				while(cells.hasNext()) {
					XSSFCell cell = (XSSFCell) cells.next();
					// Cell cell = cells.next();
					CellType type = cell.getCellType();
					
					if(type == CellType.STRING) System.out.println("[" + cell.getRowIndex() + "," + cell.getColumnIndex() + "] = STRING   | Value = " + cell.getRichStringCellValue().toString());
					else if(type == CellType.NUMERIC) System.out.println("[" + cell.getRowIndex() + "," + cell.getColumnIndex() + "] = NUMBERIC | Value = " + cell.getNumericCellValue());
					else if(type == CellType.BOOLEAN) System.out.println("[" + cell.getRowIndex() + "," + cell.getColumnIndex() + "] = BOOLEAN  | Value = " + cell.getBooleanCellValue());
					else if(type == CellType.BLANK) System.out.println("[" + cell.getRowIndex() + "," + cell.getColumnIndex() + "] = BLANK");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}