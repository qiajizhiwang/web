package com.xiangxing.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.controller.admin.BaseController;
import com.xiangxing.controller.admin.PageRequest;
import com.xiangxing.controller.admin.PageResponse;
import com.xiangxing.mapper.HeadquartersMapper;
import com.xiangxing.mapper.SchoolImageMapper;
import com.xiangxing.mapper.SchoolMapper;
import com.xiangxing.model.School;
import com.xiangxing.model.SchoolExample;
import com.xiangxing.model.SchoolImage;
import com.xiangxing.model.SchoolImageExample;
import com.xiangxing.model.User;
import com.xiangxing.model.AdvertExample.Criteria;
import com.xiangxing.utils.FileUtil;

@Controller
@RequestMapping("/school")
public class SchoolController extends BaseController {

	private static final Logger logger = LogManager.getLogger(SchoolController.class);
	@Autowired
	private SchoolMapper schoolMapper;
	@Autowired
	private SchoolImageMapper schoolImageMapper;
	
	@Autowired
	HeadquartersMapper headquartersMapper;

	@Value("${upload_file_path}")
	private String upload_file_path;

	// 最大文件大小 30MB
	long maxSize = 31457280;

	@RequestMapping("/school")
	public String school() {
		return "school";
	}

	@RequestMapping("/saveSchool")
	public void saveSchool(School school) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		if (me.getType() == 1) {
			logger.info("权限不足！");
			writeToErrorResponse(new JSONObject());
			return;
		}
		
		int count = headquartersMapper.selectByPrimaryKey(school.getHeadquartersId()).getCount();
		SchoolExample example = new SchoolExample();
		example.createCriteria().andHeadquartersIdEqualTo(school.getHeadquartersId());
		int nowCount = schoolMapper.countByExample(example);
		if(count <= nowCount) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("errorMsg", "校区数量超限，请联系管理员");
			writeToErrorResponse(jsonObject);
			return;
		}
		school.setCreateTime(new Date());
		schoolService.addSchool(school);
		writeToOkResponse();
	}

	@RequestMapping("/schoolList")
	@ResponseBody
	public PageResponse<School> schoolList(PageRequest pageRequest, String name) {

		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		SchoolExample schoolExample = new SchoolExample();

		User me = (User) SecurityUtils.getSubject().getPrincipal();
		List<School> schools = null;
		if (me.getType() == 1) {
			School school = schoolMapper.selectByPrimaryKey(me.getSchoolId());
			schools = new ArrayList<>();
			schools.add(school);
		}
		
		else {
			com.xiangxing.model.SchoolExample.Criteria criteria = schoolExample.createCriteria();
			if (me.getType() == 2) {
				criteria.andHeadquartersIdEqualTo(me.getHeadquartersId());
			}
			if (StringUtils.isNotBlank(name)) {
				name = "%" + name + "%";
				criteria.andNameLike(name);

			}
			schools = schoolMapper.selectByExample(schoolExample);
		}
		long total = page.getTotal();
		return new PageResponse<School>(total, schools);

	}

	@RequestMapping("/editSchool")
	public void editSchool(School school, Long id) {
		schoolService.editSchool(school, id);
		writeToOkResponse();
	}

	@RequestMapping("/destroySchool")
	public void destroySchool(Long id) {
		schoolService.destroySchool(id);
		writeToOkResponse();
	}

	@RequestMapping("/comboboxData")
	@ResponseBody
	public String comboboxData(PageRequest pageRequest, String name) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		List<School> schools = null;
		if (me.getType() == 1) {
			School school = schoolMapper.selectByPrimaryKey(me.getSchoolId());
			schools = new ArrayList<>();
			schools.add(school);
		} else {
			SchoolExample schoolExample = new SchoolExample();
			schools = schoolMapper.selectByExample(schoolExample);
		}
		return JSON.toJSONString(schools);

	}

	@RequestMapping("/validateCode")
	@ResponseBody
	public boolean validateCode(String code) {
		boolean validateFlag = true;
		Object schoolCode = request.getSession().getAttribute("schoolCode");
		if (schoolCode != null && code.equals(String.valueOf(schoolCode))) {
			return validateFlag;
		}
		SchoolExample schoolExample = new SchoolExample();
		schoolExample.createCriteria().andCodeEqualTo(code);
		List<School> schools = schoolMapper.selectByExample(schoolExample);
		if (schools.size() > 0) {
			validateFlag = false;
		}
		return validateFlag;

	}

	@RequestMapping("/editSchoolCode")
	public void editSchoolCode(String code) {
		request.getSession().setAttribute("schoolCode", code);
		writeToOkResponse();

	}

	@RequestMapping("/uploadImg")
	public void uploadImg(Long schoolId, MultipartFile file1, MultipartFile file2, MultipartFile file3) {

		if (!file1.isEmpty()) {
			String path = uploadImg(file1);
			SchoolImageExample example = new SchoolImageExample();
			example.createCriteria().andSchoolIdEqualTo(schoolId).andSortEqualTo(1l);
			List<SchoolImage> images = schoolImageMapper.selectByExample(example);
			if (images.size() > 0) {
				// 修改
				SchoolImage schoolImage = new SchoolImage();
				schoolImage.setId(images.get(0).getId());
				schoolImage.setPath(path);
				schoolImage.setCreateTime(new Date());
				schoolImageMapper.updateByPrimaryKeySelective(schoolImage);
			} else {
				// 新增
				SchoolImage schoolImage = new SchoolImage();
				schoolImage.setSchoolId(schoolId);
				schoolImage.setPath(path);
				schoolImage.setCreateTime(new Date());
				schoolImage.setSort(1l);
				schoolImageMapper.insertSelective(schoolImage);
			}
		}
		if (!file2.isEmpty()) {
			String path = uploadImg(file2);
			SchoolImageExample example = new SchoolImageExample();
			example.createCriteria().andSchoolIdEqualTo(schoolId).andSortEqualTo(2l);
			List<SchoolImage> images = schoolImageMapper.selectByExample(example);
			if (images.size() > 0) {
				// 修改
				SchoolImage schoolImage = new SchoolImage();
				schoolImage.setId(images.get(0).getId());
				schoolImage.setPath(path);
				schoolImage.setCreateTime(new Date());
				schoolImageMapper.updateByPrimaryKeySelective(schoolImage);
			} else {
				// 新增
				SchoolImage schoolImage = new SchoolImage();
				schoolImage.setSchoolId(schoolId);
				schoolImage.setPath(path);
				schoolImage.setCreateTime(new Date());
				schoolImage.setSort(2l);
				schoolImageMapper.insertSelective(schoolImage);
			}
		}
		if (!file3.isEmpty()) {
			String path = uploadImg(file3);
			SchoolImageExample example = new SchoolImageExample();
			example.createCriteria().andSchoolIdEqualTo(schoolId).andSortEqualTo(3l);
			List<SchoolImage> images = schoolImageMapper.selectByExample(example);
			if (images.size() > 0) {
				// 修改
				SchoolImage schoolImage = new SchoolImage();
				schoolImage.setId(images.get(0).getId());
				schoolImage.setPath(path);
				schoolImage.setCreateTime(new Date());
				schoolImageMapper.updateByPrimaryKeySelective(schoolImage);
			} else {
				// 新增
				SchoolImage schoolImage = new SchoolImage();
				schoolImage.setSchoolId(schoolId);
				schoolImage.setPath(path);
				schoolImage.setCreateTime(new Date());
				schoolImage.setSort(3l);
				schoolImageMapper.insertSelective(schoolImage);
			}
		}

		writeToOkResponse();
	}

	private String uploadImg(MultipartFile file) {
		// 定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");

		if (!ServletFileUpload.isMultipartContent(request)) {
			System.out.println("请选择文件。");
			return null;
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
			return null;
		}
		// 检查扩展名
		String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
		try {
			FileUtil.uploadFile(file.getBytes(), saveUrl, newFileName);
		} catch (Exception e) {
			System.out.println("上传文件失败。");
		}
		return saveUrl+newFileName;
	}

}
