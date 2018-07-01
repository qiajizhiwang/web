package com.xiangxing.controller.api;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.controller.admin.PageRequest;
import com.xiangxing.interceptor.TokenManager;
import com.xiangxing.mapper.CourseMapper;
import com.xiangxing.mapper.HomeworkMapper;
import com.xiangxing.mapper.ProductMapper;
import com.xiangxing.mapper.StudentCourseMapper;
import com.xiangxing.mapper.StudentHomeworkMapper;
import com.xiangxing.mapper.StudentMapper;
import com.xiangxing.mapper.TeacherMapper;
import com.xiangxing.mapper.ex.TeacherPoMapper;
import com.xiangxing.model.Course;
import com.xiangxing.model.CourseExample;
import com.xiangxing.model.Product;
import com.xiangxing.model.Student;
import com.xiangxing.model.StudentCourseExample;
import com.xiangxing.model.ex.ProductPo;
import com.xiangxing.vo.api.ApiPageResponse;
import com.xiangxing.model.Homework;
import com.xiangxing.model.Product;
import com.xiangxing.model.StudentCourse;
import com.xiangxing.model.StudentCourseExample;
import com.xiangxing.model.StudentHomework;
import com.xiangxing.vo.api.ApiResponse;
import com.xiangxing.vo.api.LoginInfo;
import com.xiangxing.vo.api.ProductVo;
import com.xiangxing.vo.api.TeacherRequest;

@RequestMapping("/api/teacher")
@RestController
public class ApiTeacherController {

	@Value(value = "${product_path}")
	private String imagePath;

	@Autowired
	HomeworkMapper homeworkMapper;

	@Autowired
	StudentHomeworkMapper studentHomeworkMapper;

	@Autowired
	private TeacherMapper teacherMapper;
	@Autowired
	private TeacherPoMapper teacherPoMapper;

	@Autowired
	private StudentMapper studentMapper;
	@Autowired
	private CourseMapper courseMapper;

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private StudentCourseMapper studentCourseMapper;

	@RequestMapping("/myCourses")
	public ApiResponse myCourses(PageRequest pageRequest) {
		LoginInfo info = TokenManager.getNowUser();
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);

		CourseExample example = new CourseExample();
		example.createCriteria().andTeacherIdEqualTo(info.getId()).andStatusNotEqualTo(2);
		List<Course> courses = courseMapper.selectByExample(example);

		long total = page.getTotal();

		// 过滤字段
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(Course.class, "id", "name", "schoolTime");
		return new ApiPageResponse<Course>(total, JSON.parseArray(JSONObject.toJSONString(courses, filter), Course.class));

	}

	@RequestMapping("/myStudents")
	public ApiPageResponse<Student> myStudents(TeacherRequest teacherRequest) {
		LoginInfo info = TokenManager.getNowUser();
		Page<?> page = PageHelper.startPage(teacherRequest.getPage(), teacherRequest.getRows(), true);
		List<Student> students = teacherPoMapper.myStudents(info.getId(), teacherRequest.getCourseId());
		long total = page.getTotal();
		return new ApiPageResponse<Student>(total, students);

	}

	@RequestMapping("/studentProducts")
	public ApiPageResponse<ProductPo> studentProducts(TeacherRequest teacherRequest) {
		LoginInfo info = TokenManager.getNowUser();
		Page<?> page = PageHelper.startPage(teacherRequest.getPage(), teacherRequest.getRows(), true);
		List<ProductPo> productPos = teacherPoMapper.studentProducts(info.getId(), teacherRequest.getCourseId(), teacherRequest.getStudentId());
		long total = page.getTotal();
		return new ApiPageResponse<ProductPo>(total, productPos);

	}

	@RequestMapping("/uploadProduct")
	public ApiResponse uploadProduct(ProductVo productVo) {
		LoginInfo info = TokenManager.getNowUser();
		StudentCourseExample example = new StudentCourseExample();
		example.createCriteria().andCourseIdEqualTo(productVo.getCourseId()).andStudentIdEqualTo(productVo.getStudentId());
		Long studentCourseId = studentCourseMapper.selectByExample(example).get(0).getId();
		Product product = new Product();
		try {
			BeanUtils.copyProperties(product, productVo);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		product.setStudentCourseId(studentCourseId);
		byte[] imageByte = Base64Utils.decodeFromString(productVo.getBase64Image());
		BufferedImage image;
		try {
			String dir = imagePath + File.separator + studentCourseId;
			if (!new File(dir).exists())
				new File(dir).mkdirs();
			String path = dir + File.separator + UUID.randomUUID().toString() + ".jpg";
			FileOutputStream fileOutputStream = new FileOutputStream(path);
			image = ImageIO.read(new ByteArrayInputStream(imageByte));
			ImageIO.write(image, "jpg", fileOutputStream);
			int srcWidth = image.getWidth(); // 源图宽度
			int srcHeight = image.getHeight(); // 源图高度
			product.setHeight(srcHeight);
			product.setWidth(srcWidth);
			product.setPath(path);
			productMapper.insert(product);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ApiResponse();

	}

	@RequestMapping("/publishHomework")
	public ApiResponse publishHomework(String name, long courseId, String studentIds) {
		if (StringUtils.isEmpty(studentIds))
			return new ApiResponse();
		LoginInfo info = TokenManager.getNowUser();
		Homework homework = new Homework();
		homework.setCourseId(courseId);
		homework.setCreateTime(new Date());
		homework.setName(name);
		homeworkMapper.insert(homework);
		StudentCourseExample example = new StudentCourseExample();
		example.createCriteria().andCourseIdEqualTo(courseId);
		// List<StudentCourse> students =
		// studentCourseMapper.selectByExample(example);
		String[] studentsList = studentIds.split(",");
		for (String studentId : studentsList) {
			StudentHomework studentHomework = new StudentHomework();
			studentHomework.setStatus(1);
			studentHomework.setStudentId(Long.valueOf(studentId));
			studentHomework.setHomeworkId(homework.getId());
			studentHomeworkMapper.insert(studentHomework);
		}
		return new ApiResponse();

	}

}
