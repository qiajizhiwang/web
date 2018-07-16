package com.xiangxing.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class PdfUtils {

	public static void main(String[] args) {
		Map map = new HashMap();
		map.put("studentName", "1");
		new PdfUtils().createPdf("D:/c.pdf", PdfUtils.class.getClassLoader().getResource("templates/pdf").getPath(),
				"apply.ftl", map);
	}

	public static void createPdf(String pdfFile, String templateBaseDir, String templateFile, Map globalMap) {
		PdfWriter mPdfWriter = null;
		Document document = null;
		try {
			BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);

			document = new Document(PageSize.A4, 10, 10, 10, 10);
			mPdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
			document.open();

			String html = loadFtlHtml(new File(templateBaseDir), templateFile, globalMap);
			XMLWorkerHelper.getInstance().parseXHtml(mPdfWriter, document,
					new ByteArrayInputStream(html.getBytes("UTF-8")), Charset.forName("UTF-8"),
					new ChinaFontProvide());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			document.close();
			mPdfWriter.close();
		}
	}

	public static void createMiniPdf(String pdfFile, String templateBaseDir, String templateFile, Map globalMap) {
		PdfWriter mPdfWriter = null;
		Document document = null;
		try {
			BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
			Rectangle rectangle = new Rectangle(264f, 368.5f);
			document = new Document(rectangle, 0, 0, 0, 0);
			mPdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
			document.open();

			String html = loadFtlHtml(new File(templateBaseDir), templateFile, globalMap);
			XMLWorkerHelper.getInstance().parseXHtml(mPdfWriter, document,
					new ByteArrayInputStream(html.getBytes("UTF-8")), Charset.forName("UTF-8"),
					new XMLWorkerFontProvider());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			document.close();
			mPdfWriter.close();
		}
	}

	public static String loadFtlHtml(File baseDir, String fileName, Map globalMap) {

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
		try {
			cfg.setDirectoryForTemplateLoading(baseDir);
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);// .RETHROW
			cfg.setClassicCompatible(true);
			Template temp = cfg.getTemplate(fileName);

			StringWriter stringWriter = new StringWriter();
			temp.process(globalMap, stringWriter);

			return stringWriter.toString();
		} catch (IOException | TemplateException e) {
			e.printStackTrace();
			throw new RuntimeException("load fail file");
		}
	}

	/**
	 * 
	 * 提供中文
	 * 
	 */
	public static final class ChinaFontProvide implements FontProvider {

		@Override
		public Font getFont(String arg0, String arg1, boolean arg2, float arg3, int arg4, BaseColor arg5) {
			BaseFont bfChinese = null;
			try {
				bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Font FontChinese = new Font(bfChinese, 12, Font.NORMAL);
			return FontChinese;
		}

		@Override
		public boolean isRegistered(String arg0) {
			return false;
		}
	}
}
