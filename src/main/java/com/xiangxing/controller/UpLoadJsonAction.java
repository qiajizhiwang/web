package com.xiangxing.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.xiangxing.controller.admin.BaseController;
import com.xiangxing.utils.FileUtil;

@Controller
public class UpLoadJsonAction extends BaseController {

	// 文件保存目录URL
	@Value("${upload_file_path}")
	private String upload_file_path;

	@Value("${sys_url}")
	private String sys_url;

	@RequestMapping("/uploadJson")
	public void upLoadImage(MultipartHttpServletRequest request, HttpServletResponse response) throws FileUploadException, IOException {// 文件保存目录路径
		PrintWriter out = response.getWriter();

		// 定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		extMap.put("flash", "swf,flv");
		extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
		extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");

		// 最大文件大小 30MB
		long maxSize = 31457280;

		response.setContentType("text/html; charset=UTF-8");

		if (!ServletFileUpload.isMultipartContent(request)) {
			out.println(getError("请选择文件。"));
			return;
		}

		String dirName = request.getParameter("dir");
		if (dirName == null) {
			dirName = "image";
		}
		if (!extMap.containsKey(dirName)) {
			out.println(getError("目录名不正确。"));
			return;
		}
		// 创建文件夹
		String saveUrl = upload_file_path + dirName + "/";
		String ymd = new SimpleDateFormat("yyyyMMdd").format(new Date());
		saveUrl += ymd + "/";

		File dirFile = new File(saveUrl);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		List<MultipartFile> files = request.getFiles("imgFile");

		Iterator<MultipartFile> itr = files.iterator();
		while (itr.hasNext()) {
			MultipartFile file = itr.next();
			// 检查文件大小

			if (file.getSize() > maxSize) {
				out.println(getError("上传文件大小超过限制。"));
				return;
			}
			// 检查扩展名
			String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();

			if (!Arrays.asList(extMap.get(dirName).split(",")).contains(fileExt)) {
				out.println(getError("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。"));
				return;

			}
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
			try {
				FileUtil.uploadFile(file.getBytes(), saveUrl, newFileName);
			} catch (Exception e) {
				out.println(getError("上传文件失败。"));
				return;
			}
			JSONObject obj = new JSONObject();
			obj.put("error", 0);
			obj.put("url", sys_url + "initImage?imageUrl=" + saveUrl + newFileName);
			out.println(obj.toJSONString());
		}
	}

	// 上传报错的提示方法
	private String getError(String message) {
		JSONObject obj = new JSONObject();
		obj.put("error", 1);
		obj.put("message", message);
		return obj.toJSONString();
	}
}
