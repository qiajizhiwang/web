package com.xiangxing.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

public class FileUtil {
	public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
		File targetFile = new File(filePath);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		FileOutputStream out = new FileOutputStream(filePath + fileName);
		out.write(file);
		out.flush();
		out.close();
	}

	/**
	 * 
	 * 输出文件
	 * 
	 * @param outputFile
	 *            输出文件路径
	 * @param fileType
	 *            输出文件类型
	 * @param fileTitle
	 *            输出文件名称
	 * @param response
	 *            响应
	 * @throws MyException
	 * @throws Exception
	 * @since <IVersion>
	 */
	public static void doFile(String outputFile, String fileType, String fileTitle, HttpServletResponse response) throws Exception {
		doFile(new File(outputFile), fileType, fileTitle, response);
	}

	/**
	 * 
	 * 输出文件
	 * 
	 * @param file
	 *            输出文件路径
	 * @param fileType
	 *            输出文件类型
	 * @param fileTitle
	 *            输出文件名称
	 * @param response
	 *            响应
	 * @throws MyException
	 * @throws Exception
	 * @since <IVersion>
	 */
	public synchronized static void doFile(File file, String fileType, String fileTitle, HttpServletResponse response) throws Exception {
		String CONTENT_TYPE = "application/pdf;charset=GBK";
		OutputStream out = null;
		InputStream is = null;
		try {
			// displayName = new String(fileTitle.getBytes("GB2312"),
			// "ISO8859_1");
			// MimeUtility.encodeWord(fileTitle);
			// urlencode有问题 符号不能转义回来
			// String fileName = URLEncoder.encode(fileTitle,
			// "UTF-8");//修复浏览器下载问题 by maoyi
			// 按网上很多人提供的解决方案：将文件名编码成ISO8859-1似乎是有效的解决方案
			String fileName = new String(fileTitle.getBytes("gb2312"), "ISO8859-1");
			String s = "attachment; filename=" + fileName + fileType;
			response.setContentType(CONTENT_TYPE);
			response.setHeader("Content-Disposition", s);
			out = response.getOutputStream();
			is = new BufferedInputStream(new FileInputStream(file));
			byte[] buf = new byte[1024];
			int count = 0;
			while ((count = is.read(buf)) > 0) {
				out.write(buf, 0, count);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (IOException e) {
			}
		}
	}
}
