package com.xiangxing.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xiangxing.controller.admin.BaseController;

@Controller
public class ImageController extends BaseController {
	@RequestMapping(value = "/initImage")
	@ResponseBody
	public void initImage(String imageUrl) throws Exception {
		File file = new File(imageUrl);
		if (!file.exists()) {
			JSONObject jsonObject = new JSONObject();
			writeToErrorResponse(jsonObject);
			return;
		}
		InputStream inStream = new FileInputStream(file);
		// 得到图片的二进制数据，以二进制封装得到数据，具有通用性
		byte[] data = readInputStream(inStream);
		// 写入数据
		OutputStream os = response.getOutputStream();
		os.write(data);
		// 关闭输出流
		os.flush();
		os.close();
	}

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// 创建一个Buffer字符串
		byte[] buffer = new byte[1024];
		// 每次读取的字符串长度，如果为-1，代表全部读取完毕
		int len = 0;
		// 使用一个输入流从buffer里把数据读取出来
		while ((len = inStream.read(buffer)) != -1) {
			// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
			outStream.write(buffer, 0, len);
		}
		// 关闭输入流
		inStream.close();
		// 把outStream里的数据写入内存
		return outStream.toByteArray();
	}
}
