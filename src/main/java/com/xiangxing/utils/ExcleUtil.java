package com.xiangxing.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;

/**
 * @ClassName: ExcleUtil
 * @Description: (Excle工具类)
 * @author sunhao
 * @date 2017-7-28 下午5:24:08
 * @version V3.0
 */
public class ExcleUtil {
	/**
	 * 
	 * @Description: (导出Excle表格)
	 * @author sunhao
	 * @date 2017-7-28 下午5:01:01
	 * @param response
	 * @param data
	 *            数据源
	 * @param title
	 *            标题
	 * @param columnNames
	 *            列名
	 * @param valueKeys
	 *            值key
	 * @throws Exception
	 */
	public static void doExcle(HttpServletResponse response, List<Map> data, String title, String[] columnNames, String[] valueKeys)
			throws Exception {
		Paragraph p1 = new Paragraph(title + "\n\n", new Font(20, Font.NORMAL));
		// 居中对齐
		p1.setAlignment(Element.ALIGN_CENTER);
		response.setContentType("application/vnd.ms-word");
		response.setHeader("Content-Disposition", "attachment;filename=\"" + new String(title.getBytes("gb2312"), "ISO8859-1") + ".xls\"");
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		response.setDateHeader("Expires", 0);

		// 创建一个EXCEL
		Workbook wb = new HSSFWorkbook();
		// 设置单元格自动换行
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setWrapText(true);

		// 创建一个SHEET
		Sheet sheet1 = wb.createSheet(DateUtil.dateToString(new Date(), DateUtil.patternA) + title);
		sheet1.setColumnWidth(0, 3000);
		for (int i = 1; i <= columnNames.length; i++) {
			sheet1.setColumnWidth(i, 4000);
		}
		Row row6 = sheet1.createRow((short) 0);
		for (int i = 0; i < columnNames.length; i++) {
			Cell cell6 = row6.createCell((short) i);
			cell6.setCellValue(columnNames[i]);
		}
		if (data != null && data.size() > 0) {
			for (int i = 0; i < data.size(); i++) {
				Map<String, Object> map = data.get(i);
				// 创建行
				Row rowI = sheet1.createRow((short) (i + 1));

				for (int j = 0; j < valueKeys.length; j++) {
					Cell cell = rowI.createCell((short) j);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(String.valueOf(map.get(valueKeys[j])));
				}
			}
		}

		wb.write(response.getOutputStream());
		response.getOutputStream().close();

	}

	/**
	 * 
	 * @Description: (创建Excle表格)
	 * @author sunhao
	 * @date 2017-7-28 下午5:01:01
	 * @param response
	 * @param data
	 *            数据源
	 * @param title
	 *            标题
	 * @param columnNames
	 *            列名
	 * @param valueKeys
	 *            值key
	 * @throws Exception
	 */
	public static void createExcle(String uri, List<Map<String, Object>> data, String title, String[] columnNames, String[] valueKeys) throws Exception {
		Paragraph p1 = new Paragraph(title + "\n\n", new Font(20, Font.NORMAL));
		// 居中对齐
		p1.setAlignment(Element.ALIGN_CENTER);

		// 创建一个EXCEL
		Workbook wb = new HSSFWorkbook();
		// 设置单元格自动换行
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setWrapText(true);

		// 创建一个SHEET
		Sheet sheet1 = wb.createSheet(DateUtil.dateToString(new Date(), DateUtil.patternA) + title);
		sheet1.setColumnWidth(0, 3000);
		for (int i = 1; i <= columnNames.length; i++) {
			sheet1.setColumnWidth(i, 4000);
		}
		Row row6 = sheet1.createRow((short) 0);
		for (int i = 0; i < columnNames.length; i++) {
			Cell cell6 = row6.createCell((short) i);
			cell6.setCellValue(columnNames[i]);
		}
		if (data != null && data.size() > 0) {
			for (int i = 0; i < data.size(); i++) {
				Map<String, Object> map = data.get(i);
				// 创建行
				Row rowI = sheet1.createRow((short) (i + 1));

				for (int j = 0; j < valueKeys.length; j++) {
					Cell cell = rowI.createCell((short) j);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(String.valueOf(map.get(valueKeys[j])));
				}
			}
		}

		OutputStream stream = new FileOutputStream(new File(uri));
		wb.write(stream);

	}
}
