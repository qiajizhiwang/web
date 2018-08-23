package com.xiangxing.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.controller.admin.PageRequest;
import com.xiangxing.controller.admin.PageResponse;
import com.xiangxing.mapper.AdvertMapper;
import com.xiangxing.model.Advert;
import com.xiangxing.model.AdvertExample;
import com.xiangxing.model.AdvertExample.Criteria;
import com.xiangxing.model.User;
import com.xiangxing.utils.FileUtil;
import com.xiangxing.vo.api.ApiResponse;

/**
 * 广告管理
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/advert")
public class AdvertController {

	private static final Logger logger = LogManager.getLogger(AdvertController.class);
	// 文件保存目录URL
	@Value("${upload_file_path}")
	private String upload_file_path;

	@Value("${sys_url}")
	private String sys_url;

	@Autowired
	private AdvertMapper advertMapper;

	// 最大文件大小 30MB
	long maxSize = 31457280;

	@RequestMapping
	public String advert() {
		return "advert";
	}

	@RequestMapping("/saveAdvert")
	@ResponseBody
	public ApiResponse saveAdvert(HttpServletRequest request, Advert advert, MultipartFile file) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		if (me.getType() == 0) {
			AdvertExample advertExample = new AdvertExample();
			advertExample.createCriteria().andTypeEqualTo(1);
			if (advertMapper.countByExample(advertExample) >1 ){
				return ApiResponse.getErrorResponse("广告数超出");
			}
			advert.setType(1);
		} else {
			AdvertExample advertExample = new AdvertExample();
			advertExample.createCriteria().andTypeEqualTo(2).andSchoolIdEqualTo(me.getSchoolId());
			if (advertMapper.countByExample(advertExample) >0 ){
				return ApiResponse.getErrorResponse("广告数超出");
			}
			advert.setType(2);
			advert.setSchoolId(me.getSchoolId());
		}
		// 定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");

		if (!ServletFileUpload.isMultipartContent(request)) {
			System.out.println("请选择文件。");
			return ApiResponse.getErrorResponse("文件不存在");
		}

		// 创建文件夹
		String saveUrl = upload_file_path + "image/";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		saveUrl += ymd + "/";

		File dirFile = new File(saveUrl);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		if (file.getSize() > maxSize) {
			logger.error("上传文件大小超过限制。");
			return ApiResponse.getErrorResponse("上传文件大小超过限制。");
		}
		// 检查扩展名
		String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)
				.toLowerCase();

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
		try {
			FileUtil.uploadFile(file.getBytes(), saveUrl, newFileName);
		} catch (Exception e) {
			System.out.println("上传文件失败。");
			return ApiResponse.getErrorResponse("上传文件失败。");
		}
		advert.setPath(saveUrl + newFileName);
		advertMapper.insertSelective(advert);
		return new ApiResponse();
	}

	@RequestMapping("/advertList")
	@ResponseBody
	public PageResponse<Advert> advertList(PageRequest pageRequest, HttpServletRequest httpServletRequest) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		Long schoolId = null;
		if (me.getType() == 1) {
			schoolId = me.getSchoolId();
		}
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		AdvertExample advertExample = new AdvertExample();
		Criteria criteria = advertExample.createCriteria();
		if (null != schoolId)
			criteria.andSchoolIdEqualTo(schoolId);
		List<Advert> advertExs = advertMapper.selectByExample(advertExample);
		for (Advert advertEx : advertExs) {
			advertEx.setPath(httpServletRequest.getContextPath() + "/initImage?imageUrl=" + advertEx.getPath());
		}
		long total = page.getTotal();
		return new PageResponse<Advert>(total, advertExs);

	}

	@RequestMapping("/editAdvert")
	@ResponseBody
	public ApiResponse editAdvert(HttpServletRequest request, Advert advert, Long id, MultipartFile file) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		Advert advert1 = advertMapper.selectByPrimaryKey(id);
		
		if (me.getType() == 0) {
			if (advert1.getType() == 2) {
				return ApiResponse.getErrorResponse("权限不足");
			}
			advert.setType(1);

		} else {
			if (advert1.getType() == 1) {
				return ApiResponse.getErrorResponse("权限不足");
			}
			advert.setType(2);
			advert.setSchoolId(me.getSchoolId());
		}
		advert.setId(advert1.getId());
		if (null != file) {
			// 定义允许上传的文件扩展名
			HashMap<String, String> extMap = new HashMap<String, String>();
			extMap.put("image", "gif,jpg,jpeg,png,bmp");

			if (!ServletFileUpload.isMultipartContent(request)) {
				System.out.println("请选择文件。");
				return ApiResponse.getErrorResponse("文件不存在");
			}

			// 创建文件夹
			String saveUrl = upload_file_path + "image/";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String ymd = sdf.format(new Date());
			saveUrl += ymd + "/";

			File dirFile = new File(saveUrl);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}

			if (file.getSize() > maxSize) {
				logger.error("上传文件大小超过限制。");
				return ApiResponse.getErrorResponse("文件不存在");
			}
			// 检查扩展名
			String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)
					.toLowerCase();

			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
			try {
				FileUtil.uploadFile(file.getBytes(), saveUrl, newFileName);
			} catch (Exception e) {
				System.out.println("上传文件失败。");
				return ApiResponse.getErrorResponse("文件不存在");
			}
			advert.setPath(saveUrl + newFileName);
		}
		advertMapper.updateByPrimaryKeySelective(advert);
		return new ApiResponse();
	}

	@RequestMapping("/destroyAdvert")
	@ResponseBody
	public ApiResponse destroyadvert(Long id) {
		advertMapper.deleteByPrimaryKey(id);
		return new ApiResponse();
	}

}
