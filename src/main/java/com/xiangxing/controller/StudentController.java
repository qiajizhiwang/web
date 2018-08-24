package com.xiangxing.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.controller.admin.BaseController;
import com.xiangxing.controller.admin.PageRequest;
import com.xiangxing.controller.admin.PageResponse;
import com.xiangxing.mapper.EntryFormMapper;
import com.xiangxing.mapper.StudentCourseMapper;
import com.xiangxing.mapper.StudentMapper;
import com.xiangxing.mapper.ex.CourseMapperEx;
import com.xiangxing.mapper.ex.StudentPoMapper;
import com.xiangxing.model.EntryForm;
import com.xiangxing.model.Student;
import com.xiangxing.model.StudentCourse;
import com.xiangxing.model.StudentExample;
import com.xiangxing.model.User;
import com.xiangxing.model.ex.CourseEx;
import com.xiangxing.model.ex.StudentPo;
import com.xiangxing.utils.DateUtil;
import com.xiangxing.utils.FileUtil;
import com.xiangxing.utils.HanyuPinyinHelper;
import com.xiangxing.utils.MD5Util;
import com.xiangxing.utils.StringUtil;
import com.xiangxing.vo.api.ApiResponse;

import jxl.Sheet;
import jxl.Workbook;

@Controller
@RequestMapping("/student")
public class StudentController extends BaseController {

	private static final Logger logger = LogManager.getLogger(StudentController.class);

	@Autowired
	private StudentMapper studentMapper;

	@Autowired
	private StudentPoMapper studentPoMapper;

	@Autowired
	private CourseMapperEx courseMapperEx;

	@Autowired
	StudentCourseMapper studentCourseMapper;
	@Autowired
	EntryFormMapper entryFormMapper;

	@Value("${upload_file_path}")
	private String upload_file_path;
	@Value("${xlsTemplates_path}")
	private String xlsTemplates_path;

	// 最大文件大小 30MB
	long maxSize = 31457280;

	@RequestMapping("/student")
	public String student(Model model) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		List courses = new ArrayList<>();
		if (me.getType() == 1) {
			courses = courseMapperEx.courseList(null, me.getSchoolId(), null);
		}
		model.addAttribute("courses", courses);
		return "student";
	}

	@RequestMapping("/saveStudent")
	@ResponseBody
	public ApiResponse savestudent(StudentPo student) {
		student.setPassword(MD5Util.MD5Encode(student.getPassword()));
		student.setBirthday(DateUtil.stringToDate(student.getShowBirthday()));
		student.setCreateTime(new Date());
		studentMapper.insertSelective(student);
		return new ApiResponse();
	}

	@RequestMapping("/studentList")
	@ResponseBody
	public PageResponse<StudentPo> studentList(PageRequest pageRequest, String name) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);

		List<StudentPo> students = studentPoMapper.list(name, me.getSchoolId(), null);
		for (StudentPo studentPo : students) {
			studentPo.setShowBirthday(DateUtil.dateToString(studentPo.getBirthday(), DateUtil.patternA));
			studentPo.setPassword(null);
		}
		long total = page.getTotal();
		return new PageResponse<StudentPo>(total, students);

	}

	@RequestMapping("/applyList")
	@ResponseBody
	public PageResponse<CourseEx> applyList(PageRequest pageRequest, Long studentId) {
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);

		List<CourseEx> courseList = courseMapperEx.courseListByStudentId(studentId);
		long total = page.getTotal();
		return new PageResponse<CourseEx>(total, courseList);

	}

	@RequestMapping("/editStudent")
	@ResponseBody
	public ApiResponse editstudent(StudentPo student, Long studentId) {
		student.setId(studentId);
		if (StringUtil.isNotEmpty(student.getPassword())) {
			student.setPassword(MD5Util.MD5Encode(student.getPassword()));
		}
		else{
			student.setPassword(null);
		}
		student.setBirthday(DateUtil.stringToDate(student.getShowBirthday()));
		studentMapper.updateByPrimaryKeySelective(student);
		return new ApiResponse();
	}

	@RequestMapping("/destroyStudent")
	@ResponseBody
	public ApiResponse destroystudent(Long studentId) {
		studentMapper.deleteByPrimaryKey(studentId);
		return new ApiResponse();
	}

	@RequestMapping("/saveApply")
	@ResponseBody
	public ApiResponse saveApply(StudentCourse studentCourse) {
		try {
			studentCourse.setCreateTime(new Date());
			studentCourse.setPeriod(0);
			studentCourseMapper.insert(studentCourse);
			return new ApiResponse();
		} catch (Exception e) {
			return ApiResponse.getErrorResponse("学生可能已经报过这门课");
		}

	}

	@RequestMapping("/saveEntryForm")
	@ResponseBody
	public ApiResponse saveEntryForm(EntryForm entryForm) {
		try {
			entryForm.setStudentId(entryForm.getId());
			entryForm.setId(null);
			entryForm.setCreateTime(new Date());
			entryFormMapper.insertSelective(entryForm);
			return new ApiResponse();
		} catch (Exception e) {
			return ApiResponse.getErrorResponse("异常");
		}

	}

	/***
	 * 下载excel
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "downloadExcel")
	public void downloadExcle() throws Exception {
		FileUtil.doFile(xlsTemplates_path + File.separator + "学生信息导入模板.xls", ".xls", "学生信息导入模板", response);

	}

	@RequestMapping("/uploadExcel")
	public void uploadExcel(MultipartFile file) {
		User me = (User) SecurityUtils.getSubject().getPrincipal();
		JSONObject jsonObject = new JSONObject();
		if (me.getType() == 0) {
			jsonObject.put("msg", "管理员无权限，必须指定学校！");
			writeToErrorResponse(jsonObject);
			return;
		}

		if (file.isEmpty()) {
			jsonObject.put("msg", "请选择文件！");
			writeToErrorResponse(jsonObject);
			return;
		}

		String filePath = uploadFile(file);

		List<Student> students = new ArrayList<Student>();
		InputStream is = null;
		Workbook workBook = null;
		try {
			is = new FileInputStream(filePath);
			// 实例化一个工作簿对象
			workBook = Workbook.getWorkbook(is);
			// 获取该工作表中的第一个工作表
			Sheet sheet = workBook.getSheet(0);
			// 获取该工作表的行数，以供下面循环使用
			int rowSize = sheet.getRows();

			List<String> phones = new ArrayList<>();
			for (int i = 3; i < rowSize; i++) {
				Student student = new Student();
				// 编号
				String id = sheet.getCell(0, i).getContents().replace(" ", "");
				// 手机号
				String phone = sheet.getCell(1, i).getContents().replace(" ", "");
				if (phones.contains(phone)) {
					jsonObject.put("msg", String.format("账号%s重复！", phone));
					writeToErrorResponse(jsonObject);
					return;
				}
				if (phone.length()>11) {
					jsonObject.put("msg", String.format("账号%s格式错误！", phone));
					writeToErrorResponse(jsonObject);
					return;
				}
				phones.add(phone);
				if (StringUtil.isEmpty(phone)) {
					break;
				}
				StudentExample example = new StudentExample();
				example.createCriteria().andPhoneEqualTo(phone);
				List<Student> studentList = studentMapper.selectByExample(example);
				if (studentList.size() > 0) {
					jsonObject.put("msg", String.format("账号%s已存在！", phone));
					writeToErrorResponse(jsonObject);
					return;
				}
				student.setPhone(phone);
				student.setPassword(MD5Util.MD5Encode("000000"));
				// 姓名
				String name = sheet.getCell(2, i).getContents().replace(" ", "");
				if (StringUtil.isEmpty(name)) {
					jsonObject.put("msg", String.format("账号%s姓名不能为空！", phone));
					writeToErrorResponse(jsonObject);
					return;
				}
				student.setName(name);
				// pinyin
				student.setPinyin(HanyuPinyinHelper.toHanyuPinyin(name));
				// 生日
				String birthday = sheet.getCell(3, i).getContents().replace(" ", "").replaceAll("\"", "");
				Date stringToDate = DateUtil.stringToDate(birthday, DateUtil.patternG);
				if (StringUtil.isEmpty(birthday) || null == stringToDate) {
					jsonObject.put("msg", String.format("账号%s生日不能为空！", phone));
					writeToErrorResponse(jsonObject);
					return;
				}
				student.setBirthday(stringToDate);
				// 性别
				String gender = sheet.getCell(4, i).getContents().replace(" ", "");
				if (StringUtil.isEmpty(gender)) {
					jsonObject.put("msg", String.format("账号%s性别不能为空！", phone));
					writeToErrorResponse(jsonObject);
					return;
				}
				student.setGender(gender);
				// 民族
				String nation = sheet.getCell(5, i).getContents().replace(" ", "");
				student.setNation(nation);
				// 国家
				String state = sheet.getCell(6, i).getContents().replace(" ", "");
				student.setState(state);
				// 专业
				String major = sheet.getCell(7, i).getContents().replace(" ", "");
				student.setMajor(major);
				// 年级
				String grade = sheet.getCell(8, i).getContents().replace(" ", "");
				student.setGrade(grade);
				// 班级
				String class_grade = sheet.getCell(9, i).getContents().replace(" ", "");
				student.setClassGrade(class_grade);
				// 家庭地址
				String house_address = sheet.getCell(10, i).getContents().replace(" ", "");
				student.setHouseAddress(house_address);
				// 父母电话
				String home_telephone = sheet.getCell(11, i).getContents().replace(" ", "");
				student.setHomeTelephone(home_telephone);
				// 身份证号码
				String id_card = sheet.getCell(12, i).getContents().replace(" ", "");
				student.setIdCard(id_card);
				student.setSchoolId(me.getSchoolId());
				student.setStatus(1);
				student.setCreateTime(new Date());
				students.add(student);
			}

			for (Student student : students) {
				// 保存数据
				studentMapper.insertSelective(student);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workBook.close();
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			File delfile = new File(filePath);
			if (delfile.exists()) {
				delfile.delete();
			}
		}

		writeToOkResponse();
	}

	private String uploadFile(MultipartFile file) {
		// 定义允许上传的文件扩展名

		if (!ServletFileUpload.isMultipartContent(request)) {
			System.out.println("请选择文件。");
			return null;
		}

		// 创建文件夹
		String saveUrl = upload_file_path + "excle/";
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
		return saveUrl + newFileName;
	}
}
