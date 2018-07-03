package com.xiangxing.controller.api;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.util.UuidUtil;
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
import com.xiangxing.mapper.CourseSignMapper;
import com.xiangxing.mapper.HomeworkMapper;
import com.xiangxing.mapper.MessageMapper;
import com.xiangxing.mapper.MessageQueueMapper;
import com.xiangxing.mapper.NoticeDetailMapper;
import com.xiangxing.mapper.NoticeMapper;
import com.xiangxing.mapper.ProductMapper;
import com.xiangxing.mapper.StudentCourseMapper;
import com.xiangxing.mapper.StudentHomeworkMapper;
import com.xiangxing.mapper.TeacherMapper;
import com.xiangxing.mapper.ex.CourseSignPoMapper;
import com.xiangxing.mapper.ex.TeacherPoMapper;
import com.xiangxing.model.Advert;
import com.xiangxing.model.Course;
import com.xiangxing.model.CourseExample;
import com.xiangxing.model.CourseSign;
import com.xiangxing.model.Homework;
import com.xiangxing.model.Message;
import com.xiangxing.model.MessageExample;
import com.xiangxing.model.MessageQueue;
import com.xiangxing.model.MessageQueueExample;
import com.xiangxing.model.Notice;
import com.xiangxing.model.NoticeDetail;
import com.xiangxing.model.Product;
import com.xiangxing.model.Student;
import com.xiangxing.model.StudentCourse;
import com.xiangxing.model.StudentCourseExample;
import com.xiangxing.model.StudentHomework;
import com.xiangxing.model.ex.CourseSignPo;
import com.xiangxing.model.ex.ProductPo;
import com.xiangxing.utils.DateUtil;
import com.xiangxing.vo.api.ApiPageResponse;
import com.xiangxing.vo.api.ApiResponse;
import com.xiangxing.vo.api.CourseSignResponse;
import com.xiangxing.vo.api.LoginInfo;
import com.xiangxing.vo.api.ProductVo;
import com.xiangxing.vo.api.TeacherRequest;

@RequestMapping("/api/teacher")
@RestController
public class ApiTeacherController {

	@Value(value = "${product_path}")
	private String imagePath;

	@Value(value = "${message_path}")
	private String messagePath;

	@Autowired
	HomeworkMapper homeworkMapper;

	@Autowired
	StudentHomeworkMapper studentHomeworkMapper;

	@Autowired
	private TeacherMapper teacherMapper;
	@Autowired
	private TeacherPoMapper teacherPoMapper;

	@Autowired
	private CourseSignMapper courseSignMapper;
	@Autowired
	private CourseMapper courseMapper;

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private StudentCourseMapper studentCourseMapper;

	@Autowired
	private CourseSignPoMapper courseSignPoMapper;

	@Autowired
	NoticeMapper noticeMapper;

	@Autowired
	NoticeDetailMapper noticeDetailMapper;

	/**
	 * 获取课程
	 * 
	 * @param pageRequest
	 * @return
	 */
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
		return new ApiPageResponse<Course>(total,
				JSON.parseArray(JSONObject.toJSONString(courses, filter), Course.class));

	}

	/**
	 * 获取学生
	 * 
	 * @param teacherRequest
	 * @return
	 */
	@RequestMapping("/myStudents")
	public ApiPageResponse<Student> myStudents(TeacherRequest teacherRequest) {
		LoginInfo info = TokenManager.getNowUser();
		Page<?> page = PageHelper.startPage(teacherRequest.getPage(), teacherRequest.getRows(), true);
		List<Student> students = teacherPoMapper.myStudents(info.getId(), teacherRequest.getCourseId());
		long total = page.getTotal();
		return new ApiPageResponse<Student>(total, students);

	}

	/**
	 * 获取学生作品
	 * 
	 * @param teacherRequest
	 * @return
	 */
	@RequestMapping("/studentProducts")
	public ApiPageResponse<ProductPo> studentProducts(TeacherRequest teacherRequest,
			HttpServletRequest httpServletRequest) {
		LoginInfo info = TokenManager.getNowUser();
		Page<?> page = PageHelper.startPage(teacherRequest.getPage(), teacherRequest.getRows(), true);
		List<ProductPo> productPos = teacherPoMapper.studentProducts(info.getId(), teacherRequest.getCourseId(),
				teacherRequest.getStudentId());
		for (ProductPo productPo : productPos) {
			productPo.setPath(httpServletRequest.getContextPath() + "/initImage?imageUrl=" + productPo.getPath());
		}
		long total = page.getTotal();
		return new ApiPageResponse<ProductPo>(total, productPos);

	}

	@RequestMapping("/uploadProduct")
	public ApiResponse uploadProduct(ProductVo productVo) {
		LoginInfo info = TokenManager.getNowUser();
		StudentCourseExample example = new StudentCourseExample();
		example.createCriteria().andCourseIdEqualTo(productVo.getCourseId())
				.andStudentIdEqualTo(productVo.getStudentId());
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

	/**
	 * 课程签到
	 * 
	 * @param courseId
	 * @param students
	 * @return
	 */
	@RequestMapping("/courseSign")
	public ApiResponse courseSign(Long courseId, Long[] studentIds) {
		LoginInfo info = TokenManager.getNowUser();

		List<Long> asList = Arrays.asList(studentIds);
		// List<Long> asList = new ArrayList<>();
		// for (String string : asLista) {
		// asList.add(Long.valueOf(string));
		// }
		// 获取学生课程
		StudentCourseExample example = new StudentCourseExample();
		example.createCriteria().andCourseIdEqualTo(courseId);
		List<StudentCourse> studentCourses = studentCourseMapper.selectByExample(example);

		CourseSign record = null;
		Notice notice = new Notice();
		notice.setType(2);
		notice.setText("您的孩子已签到");
		notice.setSender(info.getId());
		notice.setCreateTime(new Date());
		notice.setSenderName(teacherMapper.selectByPrimaryKey(info.getId()).getName());
		noticeMapper.insertSelective(notice);

		for (StudentCourse studentCourse : studentCourses) {
			// 重复签到忽略
			try {
				record = new CourseSign();
				record.setStudentCourseId(studentCourse.getId());
				if (asList.contains(studentCourse.getStudentId())) {
					record.setSignFlag(1l);
					NoticeDetail noticeDetail = new NoticeDetail();
					noticeDetail.setNoticeId(notice.getId());
					noticeDetail.setStatus(1);
					noticeDetail.setReceiver(studentCourse.getStudentId());
					noticeDetailMapper.insert(noticeDetail);
				}
				record.setSignTime(DateUtil.stringToDate(DateUtil.dateToString(new Date(), DateUtil.patternA)));
				courseSignMapper.insertSelective(record);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return new ApiResponse();

	}

	/**
	 * 获取学生签到信息
	 * 
	 * @param courseId
	 * @param students
	 * @return
	 */
	@RequestMapping("/getCourseSignInfo")
	public ApiResponse getCourseSignInfo(Long courseId, Long studentId, Long singTime) {
		Date singDate = null;
		if (null != singTime && 0 != singTime) {
			singDate = DateUtil.stringToDate(DateUtil.dateToString(new Date(singTime), DateUtil.patternA));
		}
		List<CourseSignPo> courseSignPos = courseSignPoMapper.getCourseSignInfo(courseId, studentId, singDate);
		CourseSignResponse courseSignResponse = new CourseSignResponse();
		courseSignResponse.setCourseSignPos(courseSignPos);
		return courseSignResponse;

	}

	@Autowired
	MessageMapper messageMapper;

	@Autowired
	MessageQueueMapper messageQueueMapper;

	@RequestMapping("/getMessages")
	public ApiPageResponse<Message> getMessages(PageRequest pageRequest, Long studentId,
			HttpServletRequest httpServletRequest) {
		LoginInfo info = TokenManager.getNowUser();
		Page<?> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getRows(), true);
		MessageQueueExample messageQueueExample = new MessageQueueExample();
		messageQueueExample.createCriteria().andStudentEqualTo(studentId).andTeacherEqualTo(info.getId());
		List<MessageQueue> messageQueues = messageQueueMapper.selectByExample(messageQueueExample);
		MessageQueue messageQueue = null;
		if (CollectionUtils.isEmpty(messageQueues)) {
			messageQueue = new MessageQueue();
			messageQueue.setStudent(studentId);
			messageQueue.setTeacher(info.getId());
			messageQueueMapper.insert(messageQueue);
		} else {
			messageQueue = messageQueues.get(0);
		}
		MessageExample messageExample = new MessageExample();
		messageExample.createCriteria().andQueueIdEqualTo(messageQueue.getId());
		messageExample.setOrderByClause("id desc");
		List<Message> messages = messageMapper.selectByExample(messageExample);
		for (Message message : messages) {
			message.setPath(httpServletRequest.getContextPath() + "/initImage?imageUrl=" + message.getPath());
		}
		long total = page.getTotal();
		return new ApiPageResponse<Message>(total, messages);
	}

	@RequestMapping("/sendMessage")
	public ApiResponse sendMessage(String text, Long studentId, String base64Image) {
		LoginInfo info = TokenManager.getNowUser();
		MessageQueueExample messageQueueExample = new MessageQueueExample();
		messageQueueExample.createCriteria().andStudentEqualTo(studentId).andTeacherEqualTo(info.getId());
		List<MessageQueue> messageQueues = messageQueueMapper.selectByExample(messageQueueExample);
		MessageQueue messageQueue = null;
		if (CollectionUtils.isEmpty(messageQueues)) {
			messageQueue = new MessageQueue();
			messageQueue.setStudent(studentId);
			messageQueue.setTeacher(info.getId());
			messageQueueMapper.insert(messageQueue);
		} else {
			messageQueue = messageQueues.get(0);
		}
		Message message = new Message();
		message.setCreateTime(new Date());
		message.setOrigin(1);
		message.setQueueId(messageQueue.getId());
		if (StringUtils.isEmpty(base64Image)) {
			message.setType(1);
			message.setText(text);
		} else {
			byte[] imageByte = Base64Utils.decodeFromString(base64Image);
			BufferedImage image;
			FileOutputStream fileOutputStream = null;
			try {
				String dir = messagePath + File.separator + messageQueue.getId();
				if (!new File(dir).exists())
					new File(dir).mkdirs();
				String path = dir + File.separator + UUID.randomUUID() + ".png";
				fileOutputStream = new FileOutputStream(path);
				image = ImageIO.read(new ByteArrayInputStream(imageByte));
				ImageIO.write(image, "png", fileOutputStream);
				message.setType(2);
				message.setPath(path);
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

		}
		messageMapper.insert(message);
		return null;

	}

}
