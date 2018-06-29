package com.xiangxing.controller.api;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiangxing.controller.admin.PageRequest;
import com.xiangxing.interceptor.TokenManager;
import com.xiangxing.mapper.SchoolMapper;
import com.xiangxing.mapper.StudentHomeworkMapper;
import com.xiangxing.mapper.StudentMapper;
import com.xiangxing.mapper.TeacherMapper;
import com.xiangxing.mapper.ex.CourseMapperEx;
import com.xiangxing.mapper.ex.HomeworkPoMapper;
import com.xiangxing.mapper.ex.ProductPoMapper;
import com.xiangxing.model.School;
import com.xiangxing.model.Student;
import com.xiangxing.model.StudentHomework;
import com.xiangxing.model.ex.CourseEx;
import com.xiangxing.model.ex.HomeworkPo;
import com.xiangxing.model.ex.ProductPo;
import com.xiangxing.vo.api.ApiPageResponse;
import com.xiangxing.vo.api.ApiResponse;
import com.xiangxing.vo.api.LoginInfo;
import com.xiangxing.vo.api.SchoolResponse;
import com.xiangxing.vo.api.StudentVo;

@RequestMapping("/api/student")
@RestController
public class ApiStudentController {

	@Value(value = "${homework_path}")
	private String imagePath;

	@Autowired
	private TeacherMapper teacherMapper;

	@Autowired
	private StudentMapper studentMapper;

	@Autowired
	CourseMapperEx courseMapperEx;

	@Autowired
	ProductPoMapper productPoMapper;

	@Autowired
	HomeworkPoMapper homeworkPoMapper;

	@Autowired
	StudentHomeworkMapper studentHomeworkMapper;

	@RequestMapping("/myCourses")
	public ApiResponse myCourses(PageRequest pageRequest) {
		LoginInfo info = TokenManager.getNowUser();
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);

		List courses = courseMapperEx.courseListByStudentId(info.getId());
		long total = page.getTotal();
		return new ApiPageResponse<CourseEx>(total, courses);

	}

	@RequestMapping("/myProducts")
	public ApiResponse myProducts(PageRequest pageRequest) {
		LoginInfo info = TokenManager.getNowUser();
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);

		List product = productPoMapper.getListByStudentId(info.getId());
		long total = page.getTotal();
		return new ApiPageResponse<ProductPo>(total, product);

	}

	@Autowired
	SchoolMapper schoolMapper;

	@RequestMapping("/mySchool")
	public ApiResponse mySchool() {
		LoginInfo info = TokenManager.getNowUser();

		School school = schoolMapper.selectByPrimaryKey(studentMapper.selectByPrimaryKey(info.getId()).getSchoolId());
		SchoolResponse response = new SchoolResponse();
		try {
			BeanUtils.copyProperties(response, school);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;

	}

	@RequestMapping("/myInfo")
	public ApiResponse myInfo() {
		LoginInfo info = TokenManager.getNowUser();
		Student student = studentMapper.selectByPrimaryKey(info.getId());
		StudentVo s = new StudentVo();
		try {
			BeanUtils.copyProperties(s, student);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;

	}

	@RequestMapping("/myHomeworks")
	public ApiResponse myHomeworks(PageRequest pageRequest, Long courseId, Integer status) {
		LoginInfo info = TokenManager.getNowUser();
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);

		List homeworks = homeworkPoMapper.list(info.getId(), courseId, status);
		long total = page.getTotal();
		return new ApiPageResponse<HomeworkPo>(total, homeworks);

	}

	@RequestMapping("/uploadHomework")
	public ApiResponse uploadHomework(Long id, String base64Image) {
		LoginInfo info = TokenManager.getNowUser();

		byte[] imageByte = Base64Utils.decodeFromString(base64Image);
		BufferedImage image;
		FileOutputStream fileOutputStream = null;
		try {
			String dir = imagePath + File.separator + info.getId();
			if (!new File(dir).exists())
				new File(dir).mkdirs();
			String path = dir + File.separator + id + ".jpg";
			fileOutputStream = new FileOutputStream(path);
			image = ImageIO.read(new ByteArrayInputStream(imageByte));
			int srcWidth = image.getWidth(); // 源图宽度
			int srcHeight = image.getHeight(); // 源图高度
			ImageIO.write(image, "jpg", fileOutputStream);
			StudentHomework studentHomework = studentHomeworkMapper.selectByPrimaryKey(id);
			studentHomework.setWidth(srcWidth);
			studentHomework.setHeight(srcHeight);
			studentHomework.setPath(path);
			studentHomework.setFinishTime(new Date());
			studentHomework.setStatus(2);
			studentHomeworkMapper.updateByPrimaryKeySelective(studentHomework);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != fileOutputStream)
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return new ApiResponse();

	}

}
